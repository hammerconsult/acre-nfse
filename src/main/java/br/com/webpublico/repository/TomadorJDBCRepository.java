package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.TomadorServicoDTO;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.QueryUtil;
import br.com.webpublico.repository.mapper.IntegerMapper;
import br.com.webpublico.repository.mapper.TomadorMapper;
import br.com.webpublico.repository.mapper.TomadorServicoMapper;
import br.com.webpublico.repository.setter.TomadorSetter;
import com.google.common.base.Strings;
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
public class TomadorJDBCRepository implements Serializable {

    private static final String SELECT = " select t.id as id, " +
            " coalesce(dados.nomerazaosocial, ' ') as nomerazaosocial, " +
            " coalesce(dados.nomefantasia,' ') as nomefantasia, " +
            " coalesce(dados.cpfcnpj,' ') as cpfcnpj, " +
            " coalesce(dados.email, ' ') as email, " +
            " coalesce(dados.apelido, ' ') as apelido ";
    private static final String FROM = " from tomadorserviconfse t " +
            "  inner join dadospessoaisnfse dados on dados.id = t.dadospessoaisnfse_id ";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    IdJDBCRepository idJDBCRepository;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void inserirTomador(TomadorServicoDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO TOMADORSERVICONFSE " +
                        " (ID, PRESTADOR_ID, DADOSPESSOAISNFSE_ID, ATIVO) " +
                        " VALUES(?, ?, ?, ?)  ",
                new TomadorSetter(dto));
    }

    public TomadorServicoDTO findById(Long id) {
        if (id != null) {
            List<TomadorServicoDTO> query = jdbcTemplate.query(" SELECT ID," +
                            " PRESTADOR_ID, DADOSPESSOAISNFSE_ID " +
                            " FROM TOMADORSERVICONFSE " +
                            " WHERE ID = ? ", new Object[]{id},
                    new TomadorMapper());
            if (query != null && !query.isEmpty()) {
                return query.stream().findFirst().get();
            }
        }
        return null;
    }

    public TomadorServicoDTO findByCpfCnpjAndPrestador(String cpfCnpj, Long idPrestador) {
        List<TomadorServicoDTO> query = jdbcTemplate.query(" SELECT T.ID," +
                        " T.PRESTADOR_ID, T.DADOSPESSOAISNFSE_ID " +
                        " FROM TOMADORSERVICONFSE T " +
                        " INNER JOIN DADOSPESSOAISNFSE DP ON DP.ID = T.DADOSPESSOAISNFSE_ID " +
                        " WHERE COALESCE(T.ATIVO, 0) = 1 AND DP.CPFCNPJ = ? AND T.PRESTADOR_ID = ? ", new Object[]{cpfCnpj, idPrestador},
                new TomadorMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<TomadorServicoDTO> buscarTomadores(Pageable pageable, List<ParametroQuery> parametros, String orderBy) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(SELECT)
                .append(FROM);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));
        sql.append(" and coalesce(t.ativo, 0) = 1 ");
        if (Strings.isNullOrEmpty(orderBy)) {
            orderBy = " order by t.id desc ";
        }
        sql.append(orderBy);
        if (pageable != null) {
            parameters.addValue("offset", pageable.getOffset());
            parameters.addValue("limit", pageable.getPageSize());
            sql.append(" offset :offset rows fetch first :limit rows only ");
        }
        return namedParameterJdbcTemplate.query(sql.toString(), parameters, new TomadorServicoMapper());
    }

    public Integer contarTomadores(List<ParametroQuery> parametros) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(t.id) as value ").append(FROM);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValues(QueryUtil.montarParametroString(sql, parametros));

        List<Integer> query = namedParameterJdbcTemplate.query(sql.toString(),
                parameters, new IntegerMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return 0;
    }

    public void inativarTomador(Long id) {
        jdbcTemplate.batchUpdate(" UPDATE TOMADORSERVICONFSE SET ATIVO = 0 WHERE ID = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, id);
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });
    }
}
