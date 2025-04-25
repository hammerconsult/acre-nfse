package br.com.webpublico.repository;

import br.com.webpublico.DateUtils;
import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.PrestadorUsuarioNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoCadastralNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoNotaFiscalServicoNfseDTO;
import br.com.webpublico.repository.mapper.*;
import br.com.webpublico.repository.setter.PrestadorServicoSetter;
import br.com.webpublico.web.rest.dto.SubstitutoTributarioDTO;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class CadastroEconomicoJDBCRepository implements Serializable {

    private static final String SQL_FIELDS = " SELECT " +
            "   CE.ID AS ID, " +
            "   CE.INSCRICAOCADASTRAL AS INSCRICAOCADASTRAL," +
            "   CE.PESSOA_ID AS PESSOA_ID," +
            "   COALESCE(CE.ENVIAEMAILNFSETOMADOR, 0) AS ENVIAEMAILNFSETOMADOR, " +
            "   COALESCE(CE.ENVIAEMAILNFSECONTADOR, 0) AS ENVIAEMAILNFSECONTADOR," +
            "   COALESCE(CE.ENVIAEMAILCANCELANFSETOMADOR, 0) AS ENVIAEMAILCANCELANFSETOMADOR, " +
            "   COALESCE(CE.ENVIAEMAILCANCELANFSECONTADOR, 0) AS ENVIAEMAILCANCELANFSECONTADOR," +
            "   CE.NOMEPARACONTATO AS NOMEPARACONTATO," +
            "   CE.TELEFONEPARACONTATO AS TELEFONEPARACONTATO," +
            "   CE.RESUMOSOBREEMPRESA AS RESUMOSOBREEMPRESA," +
            "   CE.CHAVEACESSO AS CHAVEACESSO," +
            "   CE.NATUREZAJURIDICA_ID AS NATUREZAJURIDICA_ID, " +
            "   EF.ID AS ENQUADRAMENTO_ID, " +
            "   SCE.SITUACAOCADASTRAL SITUACAO," +
            "   CE.ABERTURA," +
            "   CE.ENCERRAMENTO ";
    private static final String SQL_FROM = "    FROM CADASTROECONOMICO CE" +
            " INNER JOIN ENQUADRAMENTOFISCAL EF ON EF.CADASTROECONOMICO_ID = CE.ID AND EF.FIMVIGENCIA IS NULL " +
            " LEFT JOIN PESSOAFISICA PF ON PF.ID = CE.PESSOA_ID " +
            " LEFT JOIN PESSOAJURIDICA PJ ON PJ.ID = CE.PESSOA_ID" +
            " INNER JOIN SITUACAOCADASTROECONOMICO SCE ON SCE.ID = (SELECT MAX(REL.SITUACAOCADASTROECONOMICO_ID) " +
            "                                                          FROM CE_SITUACAOCADASTRAL REL " +
            "                                                       WHERE REL.CADASTROECONOMICO_ID = CE.ID) ";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private EnquadramentoFiscalJDBCRepository enquadramentoFiscalJDBCRepository;
    @Autowired
    private PessoaJDBCRepository pessoaJDBCRepository;

    public PrestadorServicoNfseDTO findById(Long id) {
        List<PrestadorServicoNfseDTO> query = jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                " WHERE CE.ID = ? ", new Object[]{id}, new CadastroEconomicoMapper());
        if (query != null && !query.isEmpty()) {
            return getPrestadorServicoNfseDTO(query);
        }
        return null;
    }


    public PrestadorUsuarioNfseDTO findFirstUserCadastroEconomico(Long idUsuarioWeb) {
        List<PrestadorUsuarioNfseDTO> query = jdbcTemplate.query(" SELECT U_CE.ID AS ID, " +
                " USU.ID AS USUARIOWEB_ID, " +
                " COALESCE(U_CE.CONTADOR, 0) AS CONTADOR, " +
                " CASE WHEN U_CE.SITUACAO = 'APROVADO' THEN 1 ELSE 0 END AS PERMITIDO, " +
                " U_CE.FUNCAO AS FUNCAO, " +
                " U_CE.CADASTROECONOMICO_ID AS CADASTROECONOMICO_ID, " +
                " USU.EMAIL as email, " +
                " COALESCE(pf.NOME, PJ.RAZAOSOCIAL) as nome, " +
                " COALESCE(pf.CPF, PJ.CNPJ) as cpf," +
                " U_CE.BLOQUEADOEMISSAONFSE," +
                " U_CE.TELEFONEDESBLOQUEIO," +
                " (SELECT LISTAGG(P.PERMISSAO, ',') WITHIN GROUP ( ORDER BY P.PERMISSAO ) AS ROLES " +
                "    FROM PERMISSAOCMCNFSE P " +
                "  WHERE P.USERNFSECADASTROECONOMICO_ID = U_CE.ID) AS ROLES, " +
                " USU.PASSWORDTEMPORARY " +
                " FROM USERNFSECADASTROECONOMICO U_CE " +
                " INNER JOIN USUARIOWEB USU ON USU.ID = U_CE.USUARIONFSE_ID " +
                " INNER JOIN cadastroeconomico ce ON ce.ID = U_CE.cadastroeconomico_id " +
                " LEFT JOIN PESSOAFISICA PF ON PF.ID = ce.PESSOA_ID " +
                " LEFT JOIN PESSOAJURIDICA PJ ON PJ.ID = ce.PESSOA_ID " +
                " WHERE U_CE.USUARIONFSE_ID = ? ", new Object[]{idUsuarioWeb}, new UserCadastroEconomicoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    private PrestadorServicoNfseDTO getPrestadorServicoNfseDTO(List<PrestadorServicoNfseDTO> query) {
        PrestadorServicoNfseDTO prestadorServicoNfseDTO = query.stream().findFirst().get();
        prestadorServicoNfseDTO.setPessoa(pessoaJDBCRepository.findById(prestadorServicoNfseDTO.getPessoa().getId()));
        prestadorServicoNfseDTO.setEnquadramentoFiscal(enquadramentoFiscalJDBCRepository.findById(prestadorServicoNfseDTO.getEnquadramentoFiscal().getId()));
        prestadorServicoNfseDTO.setEmiteNfs(TipoNotaFiscalServicoNfseDTO.ELETRONICA.equals(prestadorServicoNfseDTO.getEnquadramentoFiscal().getTipoNotaFiscal()));
        return prestadorServicoNfseDTO;
    }

    public PrestadorServicoNfseDTO findByInscricaoMunicipal(String inscricaoMunicipal) {
        List<PrestadorServicoNfseDTO> query = jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                        " WHERE CE.INSCRICAOCADASTRAL = ? " +
                        "   AND (SCE.SITUACAOCADASTRAL = ? OR SCE.SITUACAOCADASTRAL = ?) ",
                new Object[]{inscricaoMunicipal, SituacaoCadastralNfseDTO.ATIVA_REGULAR.name(), SituacaoCadastralNfseDTO.ATIVA_NAO_REGULAR.name()}, new CadastroEconomicoMapper());
        if (query != null && !query.isEmpty()) {
            return getPrestadorServicoNfseDTO(query);
        }
        return null;
    }

    public PrestadorServicoNfseDTO findFirstByCnpj(String cnpj) {
        List<PrestadorServicoNfseDTO> query = jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                        " WHERE PJ.CNPJ = ? ",
                new Object[]{cnpj},
                new CadastroEconomicoMapper());
        if (query != null && !query.isEmpty()) {
            return getPrestadorServicoNfseDTO(query);
        }
        return null;
    }

    public List<PrestadorServicoNfseDTO> findByCnpjAtivo(String cnpj) {
        return jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                        " WHERE PJ.CNPJ = ? AND (SCE.SITUACAOCADASTRAL = ? OR SCE.SITUACAOCADASTRAL = ?) ",
                new Object[]{cnpj, SituacaoCadastralNfseDTO.ATIVA_REGULAR.name(), SituacaoCadastralNfseDTO.ATIVA_NAO_REGULAR.name()},
                new CadastroEconomicoMapper());
    }

    public Page<PrestadorServicoNfseDTO> findByFiltro(Pageable pageable, String filtro) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("OFFSET", pageable.getOffset());
        parameters.addValue("LIMIT", pageable.getPageSize());
        parameters.addValue("ATIVOR", "ATIVA_REGULAR");
        parameters.addValue("ATIVONR", "ATIVA_NAO_REGULAR");
        parameters.addValue("FILTRO", "%" + StringUtils.removerMascaraCpfCnpj(filtro.trim().toLowerCase()) + "%");

        String sql = SQL_FIELDS + SQL_FROM +
                " WHERE (TRIM(LOWER(PF.NOME)) LIKE :FILTRO OR " +
                "         TRIM(PF.CPF) LIKE :FILTRO OR " +
                "         TRIM(LOWER(PJ.RAZAOSOCIAL)) LIKE :FILTRO OR " +
                "         TRIM(LOWER(PJ.NOMEFANTASIA)) LIKE :FILTRO OR " +
                "         TRIM(PJ.CNPJ) LIKE :FILTRO OR " +
                "         CE.INSCRICAOCADASTRAL LIKE :FILTRO)" +
                " OFFSET :OFFSET ROWS FETCH FIRST :LIMIT ROWS ONLY ";
        List<PrestadorServicoNfseDTO> cadastros = namedParameterJdbcTemplate.query(sql,
                parameters, new CadastroEconomicoMapper());

        String sqlCount = " SELECT COUNT(1) " + SQL_FROM +
                " WHERE (SCE.situacaoCadastral = :ATIVOR OR SCE.situacaoCadastral = :ATIVONR) " +
                "    AND (TRIM(LOWER(PF.NOME)) LIKE :FILTRO OR " +
                "         TRIM(PF.CPF) LIKE :FILTRO OR " +
                "         TRIM(LOWER(PJ.RAZAOSOCIAL)) LIKE :FILTRO OR " +
                "         TRIM(LOWER(PJ.NOMEFANTASIA)) LIKE :FILTRO OR " +
                "         TRIM(PJ.CNPJ) LIKE :FILTRO OR " +
                "         CE.INSCRICAOCADASTRAL LIKE :FILTRO) ";

        int total = namedParameterJdbcTemplate.query(sqlCount,
                parameters, new ResultSetExtractor<Integer>() {
                    @Override
                    public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        resultSet.next();
                        return resultSet.getInt(1);
                    }
                });

        return new PageImpl<>(cadastros, pageable, total);
    }

    public Page<PrestadorServicoNfseDTO> findByUserNfseFiltro(Pageable pageable, UserNfseDTO user, String filtro) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("USER_ID", user.getId());
        parameters.addValue("OFFSET", pageable.getOffset());
        parameters.addValue("LIMIT", (pageable.getPageNumber() + 1) * pageable.getPageSize());

        String join = " INNER JOIN USERNFSECADASTROECONOMICO U_CE ON U_CE.CADASTROECONOMICO_ID = CE.ID and u_ce.situacao = 'APROVADO'" +
                " WHERE U_CE.USUARIONFSE_ID = :USER_ID ";

        if (!Strings.isNullOrEmpty(filtro)) {
            join += " AND (TRIM(LOWER(PF.NOME)) LIKE :FILTRO OR " +
                    "      TRIM(PF.CPF) LIKE :FILTRO OR " +
                    "      TRIM(LOWER(PJ.RAZAOSOCIAL)) LIKE :FILTRO OR " +
                    "      TRIM(LOWER(PJ.NOMEFANTASIA)) LIKE :FILTRO OR " +
                    "      TRIM(PJ.CNPJ) LIKE :FILTRO OR " +
                    "      CE.INSCRICAOCADASTRAL LIKE :FILTRO) ";
            parameters.addValue("FILTRO", "%" + StringUtils.removerMascaraCpfCnpj(filtro.trim().toLowerCase()) + "%");
        }
        String sql = SQL_FIELDS + SQL_FROM + join +
                " OFFSET :OFFSET ROWS FETCH FIRST :LIMIT ROWS ONLY ";

        List<PrestadorServicoNfseDTO> cadastros = namedParameterJdbcTemplate.query(sql, parameters, new CadastroEconomicoMapper());

        String sqlCount = " SELECT COUNT(1) " + SQL_FROM + join;

        int total = namedParameterJdbcTemplate.queryForObject(sqlCount, parameters, Integer.class);
        return new PageImpl<>(cadastros, pageable, total);
    }

    public PrestadorUsuarioNfseDTO findByUserAndPrestador(Long userId, Long prestadorId) {
        List<PrestadorUsuarioNfseDTO> query = jdbcTemplate.query(" SELECT U_CE.ID AS ID, " +
                " USU.ID AS USUARIOWEB_ID, " +
                " COALESCE(U_CE.CONTADOR, 0) AS CONTADOR, " +
                " CASE WHEN U_CE.SITUACAO = 'APROVADO' THEN 1 ELSE 0 END AS PERMITIDO, " +
                " U_CE.FUNCAO AS FUNCAO, " +
                " U_CE.CADASTROECONOMICO_ID AS CADASTROECONOMICO_ID, " +
                " USU.EMAIL as email, " +
                " COALESCE(pf.NOME, PJ.RAZAOSOCIAL) as nome, " +
                " COALESCE(pf.CPF, PJ.CNPJ) as cpf, " +
                " U_CE.BLOQUEADOEMISSAONFSE," +
                " U_CE.TELEFONEDESBLOQUEIO," +
                " (SELECT LISTAGG(P.PERMISSAO, ',') WITHIN GROUP ( ORDER BY P.PERMISSAO ) AS ROLES " +
                "    FROM PERMISSAOCMCNFSE P " +
                "  WHERE P.USERNFSECADASTROECONOMICO_ID = U_CE.ID) AS ROLES, " +
                " USU.PASSWORDTEMPORARY " +
                " FROM USERNFSECADASTROECONOMICO U_CE " +
                " INNER JOIN USUARIOWEB USU ON USU.ID = U_CE.USUARIONFSE_ID " +
                " INNER JOIN cadastroeconomico ce ON ce.ID = U_CE.cadastroeconomico_id " +
                " LEFT JOIN PESSOAFISICA PF ON PF.ID = ce.PESSOA_ID " +
                " LEFT JOIN PESSOAJURIDICA PJ ON PJ.ID = ce.PESSOA_ID " +
                " WHERE U_CE.USUARIONFSE_ID = ? and ce.id = ? ", new Object[]{userId, prestadorId}, new UserCadastroEconomicoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;

    }


    public List<String> findRolesEmpresa(Long idUserCadastroEconomico) {
        return jdbcTemplate.query(" SELECT P.PERMISSAO AS VALUE " +
                "   FROM PERMISSAOCMCNFSE P " +
                "WHERE P.USERNFSECADASTROECONOMICO_ID = ? ", new Object[]{idUserCadastroEconomico}, new StringMapper());
    }

    public List<SubstitutoTributarioDTO> buscarSubstitutosTributarios(String cpf_cnpj, String inscricao, String nome_razaosocial) {
        String sql = "select coalesce(PF.CPF, PJ.CNPJ) as cpf_cnpj, " +
                "       CE.INSCRICAOCADASTRAL as inscricao, " +
                "       coalesce(PF.NOME, PJ.RAZAOSOCIAL) as nome_razaosocial, " +
                "       EF.SUBSTITUTOTRIBUTARIO as substituto " +
                "from CADASTROECONOMICO CE " +
                "         inner join ENQUADRAMENTOFISCAL EF on CE.ID = EF.CADASTROECONOMICO_ID " +
                "         inner join PESSOA on CE.PESSOA_ID = PESSOA.ID " +
                "         left join PESSOAFISICA PF on PESSOA.ID = PF.ID " +
                "         left join PESSOAJURIDICA PJ on PESSOA.ID = PJ.ID " +
                "         INNER JOIN SITUACAOCADASTROECONOMICO SCE ON SCE.ID = (SELECT MAX(s_cad.ID) " +
                "                                                                   from CADASTROECONOMICO cad " +
                "                                                                            inner join CE_SITUACAOCADASTRAL cs " +
                "                                                                               on cad.ID = cs.CADASTROECONOMICO_ID " +
                "                                                                            inner join situacaocadastroeconomico s_cad " +
                "                                                                               on cs.SITUACAOCADASTROECONOMICO_ID = s_cad.ID " +
                "                                                                    where cad.ID = ce.id) " +
                "where ef.FIMVIGENCIA is null and SCE.SITUACAOCADASTRAL IN ('" + SituacaoCadastralNfseDTO.ATIVA_REGULAR.name() + "', '" + SituacaoCadastralNfseDTO.ATIVA_NAO_REGULAR.name() + "') ";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (cpf_cnpj != "") {
            sql += "  and coalesce(PF.CPF, PJ.CNPJ) = :cpfCnpj";
            parameters.addValue("cpfCnpj", cpf_cnpj.replaceAll("[^a-zA-Z0-9]", ""));
        }
        if (inscricao != "") {
            sql += "  and CE.INSCRICAOCADASTRAL = :inscricao";
            parameters.addValue("inscricao", inscricao);
        }
        if (nome_razaosocial != "") {
            sql += "  and coalesce(PF.NOME, PJ.RAZAOSOCIAL) LIKE :nome_razaosocial";
            parameters.addValue("nome_razaosocial", "%" + nome_razaosocial.toUpperCase() + "%");
        }
        return namedParameterJdbcTemplate.query(sql, parameters, new SubstitutoTributarioMapper());
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public Long buscarQuantidadePrestadoresAutorizados() {
        String sql = " select count(1) as value " +
                "   from cadastroeconomico ce " +
                "  inner join enquadramentofiscal eq on eq.cadastroeconomico_id = ce.id " +
                "  inner join situacaocadastroeconomico sit on sit.id = (select max(s.situacaocadastroeconomico_id) " +
                "                                                               from ce_situacaocadastral s " +
                "                                                               where s.cadastroeconomico_id = ce.id) " +
                " where eq.tiponotafiscalservico = 'ELETRONICA' " +
                "   and sit.situacaocadastral in ('" + SituacaoCadastralNfseDTO.ATIVA_REGULAR.name() + "', '" + SituacaoCadastralNfseDTO.ATIVA_NAO_REGULAR.name() + "') " +
                "   and eq.fimvigencia is null ";
        List<Long> query = jdbcTemplate.query(sql, new LongMapper());
        if (query != null) {
            return query.get(0);
        }
        return 0L;
    }

    public void ativarOrDesativarUserCadastroEconomico(Long idUserNfseCadastroEconomico,
                                                       Long idUserResponsavel) {
        jdbcTemplate.update(" update usernfsecadastroeconomico set " +
                        "    situacao = " +
                        "    case situacao " +
                        "        when 'APROVADO' then 'PENDENTE'" +
                        "        else 'APROVADO'" +
                        "    end," +
                        "    usuarioresponsavel_id = :id_userresponsavel " +
                        "where id = :id_usercadastroeconomico ",
                new Object[]{idUserResponsavel, idUserNfseCadastroEconomico});
    }

    public void atualizarTributosFederais(Long prestadorId,
                                          Long tributosFederaisId) {
        jdbcTemplate.update(" update cadastroeconomico set tributosfederais_id = ? " +
                        " where id = ? ",
                new Object[]{tributosFederaisId, prestadorId});
    }

    public void update(PrestadorServicoNfseDTO prestador) {
        jdbcTemplate.batchUpdate(" update cadastroeconomico set chaveacesso = ?, nomeparacontato = ?, " +
                        " telefoneparacontato = ?, resumosobreempresa = ? " +
                        " where id = ? ",
                new PrestadorServicoSetter(prestador));
    }

    public void insertRBT12(Long prestadorId, Integer ano, Integer mes) {
        Date referencia = DateUtils.getData(1, mes, ano);
        jdbcTemplate.batchUpdate(" insert into rbt12 (id, cadastroeconomico_id, ano, mes, valor) " +
                        " values (hibernate_sequence.nextval, ?, ?, ?, func_rbt12(?, ?)) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setLong(1, prestadorId);
                        preparedStatement.setLong(2, ano);
                        preparedStatement.setLong(3, mes);
                        preparedStatement.setDate(4, DateUtils.toSQLDate(referencia));
                        preparedStatement.setLong(5, prestadorId);
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    public BigDecimal getRBT12(Long prestadorId, Integer ano, Integer mes) {
        BigDecimal rbt12 = null;
        try {
            rbt12 = jdbcTemplate.queryForObject(" select r.valor " +
                            "   from rbt12 r " +
                            " where r.cadastroeconomico_id = ?" +
                            "   and r.ano = ? " +
                            "   and r.mes = ? " +
                            " fetch first 1 row only ",
                    new Object[]{prestadorId, ano, mes},
                    (resultSet, i) -> resultSet.getBigDecimal("valor"));
        } catch (EmptyResultDataAccessException e) {
        }
        if (rbt12 == null) {
            insertRBT12(prestadorId, ano, mes);
            return getRBT12(prestadorId, ano, mes);
        }
        return rbt12;
    }
}
