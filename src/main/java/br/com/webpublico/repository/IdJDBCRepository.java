package br.com.webpublico.repository;

import br.com.webpublico.repository.mapper.LongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class IdJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public synchronized Long getId() {
        return jdbcTemplate.query(" SELECT HIBERNATE_SEQUENCE.NEXTVAL AS VALUE FROM DUAL ",
                new LongMapper()).stream().findFirst().get();
    }
}
