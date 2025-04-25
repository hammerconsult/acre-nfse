package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.DesifNfseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class DesifJDBCRepository implements Serializable {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public void insert(DesifNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO DESIF (ID, CADASTROECONOMICO_ID, DECLARACAOPRESTACAOSERVICO_ID) " +
                " VALUES (?, ?, ?) ", dto);
    }
}
