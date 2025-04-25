package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.LoteRpsNfseDTO;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import br.com.webpublico.repository.mapper.IntegerMapper;
import br.com.webpublico.repository.mapper.StringMapper;
import br.com.webpublico.repository.setter.LoteRpsSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LoteRpsJDBCRepository implements Serializable {

    private static final String SQL_FIELDS = " SELECT " +
            " L.ID," +
            " L.PRESTADOR_ID," +
            " L.SITUACAO, " +
            " L.NUMERO, " +
            " L.PROTOCOLO, " +
            " L.DATARECEBIMENTO, " +
            " L.VERSAOSISTEMA, " +
            " L.VERSAOABRASF, " +
            " COALESCE(L.REPROCESSAR, 0) AS REPROCESSAR," +
            " L.HOMOLOGACAO ";
    private static final String SQL_FROM = " FROM LOTERPS L " +
            " INNER JOIN CADASTROECONOMICO CE ON CE.ID = L.PRESTADOR_ID" +
            " INNER JOIN PESSOAJURIDICA PJ ON PJ.ID = CE.PESSOA_ID ";


    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public boolean hasLote(String numero, Long prestadorId) {
        List<Integer> query = jdbcTemplate.query(" SELECT 1 AS VALUE FROM LOTERPS WHERE NUMERO = ? AND PRESTADOR_ID = ? ",
                new Object[]{numero, prestadorId}, new IntegerMapper());
        return query != null && !query.isEmpty();
    }

    public void inserir(LoteRpsNfseDTO lote) {
        lote.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO LOTERPS (ID, PRESTADOR_ID, NUMERO, SITUACAO," +
                " DATARECEBIMENTO, PROTOCOLO, REPROCESSAR, VERSAOSISTEMA, VERSAOABRASF, HOMOLOGACAO)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ", new LoteRpsSetter(lote, Boolean.TRUE));

        jdbcTemplate.batchUpdate(" INSERT INTO LOTERPSLOG (ID, LOTERPS_ID, LOG)" +
                " VALUES (?, ?, ?) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, idJDBCRepository.getId());
                preparedStatement.setLong(2, lote.getId());
                preparedStatement.setString(3, lote.getLog());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }

    public LoteRpsNfseDTO findById(Long id) {
        List<LoteRpsNfseDTO> query = jdbcTemplate.query(SQL_FIELDS + SQL_FROM +
                        " WHERE L.ID = ? ",
                new Object[]{id},
                new LoteRpsNfseDTO());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<LoteRpsNfseDTO> consultarLotesRps(Pageable pageable, List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder().append(SQL_FIELDS).append(SQL_FROM);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));
        sql.append(" order by l.id desc ");
        parameters.addValue("offset", pageable.getOffset());
        parameters.addValue("limit", pageable.getPageSize());
        sql.append(" offset :offset rows fetch first :limit rows only ");

        return namedParameterJdbcTemplate.query(sql.toString(), parameters, new LoteRpsNfseDTO());
    }

    public Integer contarLotesRps(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(l.id) as value ").append(SQL_FROM);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        List<Integer> query = namedParameterJdbcTemplate.query(sql.toString(),
                parameters, new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0;
    }

    public String buscarXml(Long idLoteRps) {
        String sql = " select x.xml as value from loterps l " +
                "  inner join xmlwsnfse x on x.id = l.xmlwsnfse_id " +
                " where l.id = :idLoterps ";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idLoterps", idLoteRps);

        List<String> query = namedParameterJdbcTemplate.query(sql,
                parameters, new StringMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public String buscarLog(Long idLoteRps) {
        String sql = " select log as value from loterpslog where loterps_id = :id_loterps ";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id_loterps", idLoteRps);

        List<String> query = namedParameterJdbcTemplate.query(sql,
                parameters, new StringMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }
}
