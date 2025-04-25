package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.PaisNfseDTO;
import br.com.webpublico.domain.dto.PessoaNfseDTO;
import br.com.webpublico.repository.mapper.PaisMapper;
import br.com.webpublico.repository.mapper.PessoaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class PaisJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public PaisNfseDTO findByCodigo(String codigo) {
        List<PaisNfseDTO> query = jdbcTemplate.query(" SELECT ID, " +
                        " CODIGO, NOME, SIGLA " +
                        " FROM PAIS " +
                        " WHERE CODIGO = ? ",
                new Object[]{codigo}, new PaisMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }
}
