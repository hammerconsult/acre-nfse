package br.com.webpublico.repository;

import br.com.webpublico.repository.mapper.IntegerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository
public class FeriadoJDBCRepository implements Serializable {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean isFeriado(Date data) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("DATA", data);

        List<Integer> query = namedParameterJdbcTemplate.query(" SELECT 1 as value FROM FERIADO " +
                        " WHERE TRUNC(DATAFERIADO) = TRUNC(:DATA) ", parameters,
                new IntegerMapper());

        return query != null && !query.isEmpty();
    }
}
