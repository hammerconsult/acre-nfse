package br.com.webpublico.repository;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.MensagemContribuinteUsuarioDocumentoNfseDTO;
import br.com.webpublico.domain.dto.MensagemContribuinteUsuarioNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class MensagemContribuinteUsuarioJDBCRepository extends AbstractJDBCRepository<MensagemContribuinteUsuarioNfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " mc.id as mensagem_id, " +
                " mc.tipomensagemcontribuinte as mensagem_tipo, " +
                " mc.emitidaem as mensagem_emitidaem, " +
                " mc.titulo as mensagem_titulo, " +
                " mc.conteudo as mensagem_conteudo, " +
                " mc.detentorarquivocomposicao_id as mensagem_detentor_id, " +
                " obj.id," +
                " obj.lida, " +
                " obj.dataleitura," +
                " obj.resposta ";
    }

    @Override
    public String getFrom() {
        return " from mensagemcontribusuario obj " +
                " inner join mensagemcontribuinte mc on mc.id = obj.mensagemcontribuinte_id " +
                " inner join cadastroeconomico ce on ce.id = obj.cadastroeconomico_id ";
    }

    @Override
    public RowMapper<MensagemContribuinteUsuarioNfseDTO> newRowMapper() {
        return new MensagemContribuinteUsuarioNfseDTO();
    }

    @Override
    public MensagemContribuinteUsuarioNfseDTO insert(MensagemContribuinteUsuarioNfseDTO dto) {
        return dto;
    }

    @Override
    public MensagemContribuinteUsuarioNfseDTO update(MensagemContribuinteUsuarioNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(MensagemContribuinteUsuarioNfseDTO dto) {
    }

    @Override
    public String getOrderByDefault() {
        return " order by mc.emitidaem desc ";
    }

    public MensagemContribuinteUsuarioNfseDTO buscarProximaMensagemNaoLida(Long prestador, Integer prazoAviso) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(getSelect())
                .append(getFrom())
                .append(" where coalesce(obj.lida, 0) = 0 ")
                .append(" and ce.id = :prestador ")
                .append(" and mc.emitidaem + :prazo_aviso >= current_date ");
        sql.append(getOrderByDefault());
        sql.append(" fetch first 1 rows only ");

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("prestador", prestador);
        parameters.addValue("prazo_aviso", prazoAviso);


        List<MensagemContribuinteUsuarioNfseDTO> query = namedParameterJdbcTemplate.query(sql.toString(),
                parameters, new MensagemContribuinteUsuarioNfseDTO());
        if (query != null && !query.isEmpty()) {
            return query.get(0);
        }
        return null;
    }

    public Integer countMensagensNaoLidaNoPrazo(Long prestador, Integer prazoAviso) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("prestador", prestador);
        parameters.addValue("prazo_aviso", prazoAviso);

        StringBuilder sql = new StringBuilder().append(" select count(1) ")
                .append(getFrom())
                .append(" where coalesce(obj.lida, 0) = 0 ")
                .append(" and ce.id = :prestador ")
                .append(" and mc.emitidaem + :prazo_aviso >= current_date ");
        try {
            return namedParameterJdbcTemplate.queryForObject(sql.toString(),
                    parameters, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    public void marcarMensagemComoLida(MensagemContribuinteUsuarioNfseDTO mensagemContribuinteUsuario) {
        if (mensagemContribuinteUsuario == null) {
            return;
        }

        if (mensagemContribuinteUsuario.getDocumentos() != null) {
            for (MensagemContribuinteUsuarioDocumentoNfseDTO documento : mensagemContribuinteUsuario.getDocumentos()) {
                inserirDocumento(documento);
            }
        }

        jdbcTemplate.batchUpdate(" update mensagemcontribusuario set lida = 1, lidapor_id = ?, dataleitura = ?, resposta = ? " +
                " where id = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, mensagemContribuinteUsuario.getLidaPor().getId());
                preparedStatement.setDate(2, DateUtils.toSQLDate(new Date()));
                preparedStatement.setString(3, mensagemContribuinteUsuario.getResposta());
                preparedStatement.setLong(4, mensagemContribuinteUsuario.getId());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public void inserirDocumento(MensagemContribuinteUsuarioDocumentoNfseDTO documento) {
        documento.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into mensagemcontribusudoc (id, usuario_id, documento_id, arquivo_id) " +
                " values (?, ?, ?, ?) ", documento);

    }

    public List<MensagemContribuinteUsuarioDocumentoNfseDTO> buscarDocumentos(MensagemContribuinteUsuarioNfseDTO mensagemContribuinteUsuario) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id_mensagemcontribuinteusuario",
                mensagemContribuinteUsuario.getId());
        String sql = "select " +
                "       mcud.id as id," +
                "       doc.id as documento_id, " +
                "       doc.descricaodocumento as documento_descricao, " +
                "       doc.obrigatorio as documento_obrigatorio, " +
                "       mcud.usuario_id as mensagemcontribuinteusuario_id, " +
                "       mcud.arquivo_id " +
                "   from mensagemcontribusudoc mcud " +
                "  inner join mensagemcontribuintedoc doc on doc.id = mcud.documento_id " +
                " where mcud.usuario_id = :id_mensagemcontribuinteusuario ";
        return namedParameterJdbcTemplate.query(sql, parameters, new MensagemContribuinteUsuarioDocumentoNfseDTO());
    }

    public List<MensagemContribuinteUsuarioDocumentoNfseDTO> buscarDocumentosParaUpload(MensagemContribuinteUsuarioNfseDTO mensagemContribuinteUsuario) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id_mensagemcontribuinteusuario",
                mensagemContribuinteUsuario.getId());
        String sql = "select null as id," +
                "       doc.id as documento_id, " +
                "       doc.descricaodocumento as documento_descricao, " +
                "       doc.obrigatorio as documento_obrigatorio, " +
                "       mcu.id as mensagemcontribuinteusuario_id, " +
                "       null as arquivo_id " +
                "   from mensagemcontribuinte mc " +
                "  inner join mensagemcontribuintedoc doc on doc.mensagemcontribuinte_id = mc.id " +
                "  inner join mensagemcontribusuario mcu on mcu.mensagemcontribuinte_id = mc.id " +
                " where mcu.id = :id_mensagemcontribuinteusuario ";
        return namedParameterJdbcTemplate.query(sql, parameters, new MensagemContribuinteUsuarioDocumentoNfseDTO());
    }
}
