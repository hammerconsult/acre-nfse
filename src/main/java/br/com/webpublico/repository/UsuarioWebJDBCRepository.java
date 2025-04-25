package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.PrestadorUsuarioNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.repository.mapper.LongMapper;
import br.com.webpublico.repository.mapper.StringMapper;
import br.com.webpublico.repository.mapper.UserCadastroEconomicoMapper;
import br.com.webpublico.repository.mapper.UsuarioWebMapper;
import com.beust.jcommander.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class UsuarioWebJDBCRepository extends AbstractJDBCRepository<UserNfseDTO> {

    public List<String> findRoles(Long id) {
        return jdbcTemplate.query(" SELECT A.NAME AS VALUE " +
                "   FROM NFSE_USER_AUTHORITY U_A " +
                "  INNER JOIN NFSE_AUTHORITY A ON A.ID = U_A.NFSEAUTHORITY_ID " +
                " WHERE U_A.USER_ID = ? ", new Object[]{id}, new StringMapper());
    }

    public List<PrestadorUsuarioNfseDTO> findUserCadastroEconomico(Long idCadastroEconomico, String situacao) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id_cadastroeconomico", idCadastroEconomico);
        if (!Strings.isStringEmpty(situacao)) {
            parameters.addValue("situacao", situacao);
        }
        String sql = " select " +
                " usucad.id as id, " +
                " usuario.id as usuarioweb_id, " +
                " usuario.email as email, " +
                " coalesce(pf.nome, pj.razaosocial) as nome, " +
                " coalesce(pf.cpf, pj.cnpj) as cpf, " +
                " usucad.situacao as situacao, " +
                " usucad.cadastroeconomico_id as cadastroeconomico_id, " +
                " coalesce(usucad.contador, 0) as contador, " +
                " case when usucad.situacao = 'APROVADO' then 1 else 0 end as permitido," +
                " usucad.funcao as funcao, " +
                " usucad.bloqueadoemissaonfse," +
                " usucad.telefonedesbloqueio," +
                " (select listagg(p.permissao, ',') within group ( order by p.permissao ) as roles " +
                "            from permissaocmcnfse p " +
                "         where p.usernfsecadastroeconomico_id = usucad.id) as roles," +
                " usuario.passwordtemporary " +
                " from usuarioweb usuario " +
                " left join pessoafisica pf on pf.id = usuario.pessoa_id " +
                " left join pessoajuridica pj on pj.id = usuario.pessoa_id " +
                " inner join usernfsecadastroeconomico usucad on usucad.usuarionfse_id = usuario.id " +
                " where usucad.cadastroeconomico_id = :id_cadastroeconomico ";
        sql += !Strings.isStringEmpty(situacao) ? " and usucad.situacao = :situacao " : "";
        return namedParameterJdbcTemplate.query(sql, parameters, new UserCadastroEconomicoMapper());
    }

    public void atualizarPrestadorUsuario(UserNfseDTO user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("USUARIO_ID", user.getId());
        parameters.addValue("PRESTADOR_ID", user.getEmpresa().getId());
        namedParameterJdbcTemplate.update(" UPDATE USUARIOWEB usu SET usu.USERNFSECADASTROECONOMICO_ID = :PRESTADOR_ID " +
                " WHERE ID = :USUARIO_ID ", parameters);
    }

    public UserNfseDTO update(UserNfseDTO dto) {
        jdbcTemplate.batchUpdate(" UPDATE USUARIOWEB SET PASSWORD_HASH = ?, EMAIL = ?," +
                        " ACTIVATED = ?, ACTIVATION_KEY = ?, PESSOA_ID = ?, USERNFSECADASTROECONOMICO_ID = ?," +
                        " RESET_KEY = ? " +
                        " WHERE ID = ? ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1, dto.getPassword());
                        preparedStatement.setString(2, dto.getEmail());
                        preparedStatement.setBoolean(3, dto.isActivated());
                        preparedStatement.setString(4, dto.getActivationKey());
                        if (dto.getPessoaId() != null) {
                            preparedStatement.setLong(5, dto.getPessoaId());
                        } else {
                            preparedStatement.setNull(5, Types.NULL);
                        }
                        if (dto.getEmpresa() != null) {
                            preparedStatement.setLong(6, dto.getEmpresa().getId());
                        } else {
                            preparedStatement.setNull(6, Types.NULL);
                        }
                        preparedStatement.setString(7, dto.getResetKey());
                        preparedStatement.setLong(8, dto.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
        return dto;
    }

    @Override
    public void remove(UserNfseDTO dto) {

    }

    public void zerarTentativaLogin(String login) {
        jdbcTemplate.update(" UPDATE USUARIOWEB SET TENTATIVALOGIN = 0  WHERE LOGIN = ? ", new Object[]{login});
    }

    public void registrarTentativaLogin(String login) {

        jdbcTemplate.update(" UPDATE USUARIOWEB SET TENTATIVALOGIN = COALESCE(TENTATIVALOGIN,0) +1  WHERE LOGIN = ? ",
                new Object[]{login});

        jdbcTemplate.update(" UPDATE USUARIOWEB SET activated = 0 " +
                        " WHERE LOGIN = ? and TENTATIVALOGIN >=  (select max(tentativaAcessoIndevido) from ConfiguracaoNfse)",
                new Object[]{login});
    }

    public void inserirPrestadorUsuario(UserNfseDTO user, PrestadorUsuarioNfseDTO prestadorUsuario) {
        prestadorUsuario.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO USERNFSECADASTROECONOMICO (ID, USUARIONFSE_ID, CADASTROECONOMICO_ID, SITUACAO) " +
                        " VALUES (?, ?, ?, ?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, prestadorUsuario.getId());
                        preparedStatement.setLong(2, user.getId());
                        preparedStatement.setLong(3, prestadorUsuario.getPrestador().getId());
                        preparedStatement.setString(4, prestadorUsuario.isPermitido() ? "APROVADO" : "PENDENTE");
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });

        for (String permissao : prestadorUsuario.getRoles()) {
            jdbcTemplate.batchUpdate(" INSERT INTO PERMISSAOCMCNFSE (USERNFSECADASTROECONOMICO_ID, PERMISSAO) VALUES (?, ?) ",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            preparedStatement.setLong(1, prestadorUsuario.getId());
                            preparedStatement.setString(2, permissao);
                        }

                        @Override
                        public int getBatchSize() {
                            return 1;
                        }
                    });
        }
    }

    public Long buscarQuantidadeUsuariosAtivos() {
        String sql = "select count(1) as value " +
                " from USUARIOWEB usu " +
                " where usu.ACTIVATED = 1 " +
                "   and exists(select 1 " +
                "                     from USERNFSECADASTROECONOMICO uw_ce " +
                "                     where uw_ce.USUARIONFSE_ID = usu.id) ";
        List<Long> query = jdbcTemplate.query(sql, new LongMapper());
        if (query != null) {
            return query.get(0);
        }
        return 0L;
    }

    public boolean isFiscalTributario(String login) {
        Integer value = jdbcTemplate.queryForObject(" select count(1)  " +
                        "   from usuariosistema us" +
                        "  inner join pessoafisica pf on pf.id = us.pessoafisica_id  " +
                        "  inner join vigenciatribusuario vtu on us.id = vtu.usuariosistema_id " +
                        "  inner join tipousuariotribusuario tutu on vtu.id = tutu.vigenciatribusuario_id " +
                        "where current_date between vtu.vigenciainicial and coalesce(vtu.vigenciafinal, current_date) " +
                        "  and tutu.tipousuariotributario in ('FISCAL_TRIBUTARIO', 'SERVIDOR_TRIBUTARIO') " +
                        "  and replace(replace(replace(pf.cpf, '.',''), '-',''), '/','') = " +
                        "      replace(replace(replace(?, '.',''), '-',''), '/','') ", new Object[]{login},
                Integer.class);

        return value != null && value > 0;
    }

    public void insertNfseUserAuthority(UserNfseDTO user, String authority) {
        jdbcTemplate.batchUpdate(" insert into nfse_user_authority (id, user_id, nfseauthority_id) " +
                " values (?, ?, (select id from nfse_authority where name = ?)) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, idJDBCRepository.getId());
                preparedStatement.setLong(2, user.getId());
                preparedStatement.setString(3, authority);
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void removeNfseUserAuthority(UserNfseDTO user, String authority) {
        jdbcTemplate.batchUpdate(" delete from nfse_user_authority " +
                " where user_id = ? " +
                "   and nfseauthority_id = (select id from nfse_authority where name = ?) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setString(2, authority);
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    private void removerVinculoUsuarioPrestador(UserNfseDTO user) {
        jdbcTemplate.batchUpdate(" update usuarioweb set usernfsecadastroeconomico_id = null " +
                " where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, user.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    private void removerPermissoesPrestadoresUsuario(UserNfseDTO user) {
        jdbcTemplate.batchUpdate(" delete from permissaocmcnfse " +
                " where usernfsecadastroeconomico_id in (select unce.id " +
                "                                           from usernfsecadastroeconomico unce" +
                "                                        where unce.usuarionfse_id = ?) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, user.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void removerPrestadoresUsuario(UserNfseDTO user) {
        removerVinculoUsuarioPrestador(user);
        removerPermissoesPrestadoresUsuario(user);
        jdbcTemplate.batchUpdate(" delete from usernfsecadastroeconomico " +
                " where usuarionfse_id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, user.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    @Override
    public String getSelect() {
        return "  SELECT OBJ.ID AS ID, OBJ.LOGIN AS LOGIN,  " +
                " COALESCE(PF.NOME, PJ.RAZAOSOCIAL) AS NOME, " +
                " OBJ.PASSWORD_HASH AS PASSWORD, OBJ.EMAIL AS EMAIL,  " +
                " COALESCE(OBJ.ACTIVATED, 0) AS ACTIVATED, " +
                " CASE WHEN LENGTH(OBJ.LOGIN) = 14 THEN 0 ELSE 1 END AS IS_PESSOAFISICA, " +
                " OBJ.PESSOA_ID AS PESSOA_ID, " +
                " OBJ.USERNFSECADASTROECONOMICO_ID AS USEREMPRESA_ID, " +
                " UCMC.CADASTROECONOMICO_ID AS PRESTADOR_ID," +
                " OBJ.PASSWORDTEMPORARY AS PASSWORDTEMPORARY ";
    }

    @Override
    public String getFrom() {
        return " FROM USUARIOWEB OBJ  " +
                " LEFT JOIN USERNFSECADASTROECONOMICO UCMC on UCMC.ID = OBJ.USERNFSECADASTROECONOMICO_ID AND UCMC.SITUACAO = 'APROVADO'  " +
                " LEFT JOIN PESSOAFISICA PF ON PF.ID = OBJ.PESSOA_ID  " +
                " LEFT JOIN PESSOAJURIDICA PJ ON PJ.ID = OBJ.PESSOA_ID ";
    }

    @Override
    public RowMapper<UserNfseDTO> newRowMapper() {
        return new UsuarioWebMapper();
    }

    @Override
    public UserNfseDTO insert(UserNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("  insert into usuarioweb ( " +
                "                        id, login, password_hash, email, activated, activation_key, " +
                "                        created_date, created_by, pessoa_id) " +
                " values (?, ?, ?, ?, ?, ?, current_date, 'Nfs-e', ?) ", dto);
        if (dto.getRoles() != null) {
            for (String role : dto.getRoles()) {
                insertNfseUserAuthority(dto, role);
            }
        }
        return dto;
    }

    public void atualizarPermissaoPrestadorUsuario(PrestadorUsuarioNfseDTO prestadorUsuario) {
        jdbcTemplate.batchUpdate(" update usernfsecadastroeconomico set contador = ?, situacao = ?, funcao = ? " +
                " where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setBoolean(1, prestadorUsuario.isContador());
                preparedStatement.setString(2, prestadorUsuario.isPermitido() ? "APROVADO" : "PENDENTE");
                preparedStatement.setString(3, prestadorUsuario.getFuncao());
                preparedStatement.setLong(4, prestadorUsuario.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
        removerTodasPermissaoPrestadorUsuario(prestadorUsuario);
        for (String role : prestadorUsuario.getRoles()) {
            adicionarPermissaoPrestadorUsuario(prestadorUsuario.getId(), role);
        }
    }

    private void adicionarPermissaoPrestadorUsuario(Long idPrestadorUsuario, String permissao) {
        jdbcTemplate.batchUpdate(" insert into permissaocmcnfse (usernfsecadastroeconomico_id, permissao) " +
                " values (?, ?) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, idPrestadorUsuario);
                preparedStatement.setString(2, permissao);
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    private void removerTodasPermissaoPrestadorUsuario(PrestadorUsuarioNfseDTO prestadorUsuario) {
        jdbcTemplate.batchUpdate(" delete from permissaocmcnfse " +
                " where usernfsecadastroeconomico_id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, prestadorUsuario.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void removerUserNfseCadastroEconomico(Long id) {
        jdbcTemplate.update(" update usuarioweb set usernfsecadastroeconomico_id = null " +
                        " where usernfsecadastroeconomico_id = :id ", id);
        jdbcTemplate.update(" delete from permissaocmcnfse where usernfsecadastroeconomico_id = :id ", id);
        jdbcTemplate.update(" delete from usernfsecadastroeconomico where id = :id ", id);
    }

    public UserNfseDTO changePassword(UserNfseDTO dto) {
        jdbcTemplate.batchUpdate(" UPDATE USUARIOWEB SET PASSWORD_HASH = ?, PASSWORDTEMPORARY = 0 " +
                        " WHERE ID = ? ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1, dto.getPassword());
                        preparedStatement.setLong(2, dto.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
        return dto;
    }
}
