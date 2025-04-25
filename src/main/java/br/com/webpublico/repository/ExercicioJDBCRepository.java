package br.com.webpublico.repository;

import br.com.webpublico.repository.mapper.LongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ExercicioJDBCRepository implements Serializable {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Long getIdByAno(Integer ano) {
        List<Long> query = jdbcTemplate.query(" select id as value from exercicio where ano = ?",
            new Object[]{ano}, new LongMapper());

        if (query != null && query.stream().findFirst().isPresent())
            return query.stream().findFirst().get();

        return null;
    }
}
