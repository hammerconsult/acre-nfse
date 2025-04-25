package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.FaleConoscoNfseDTO;
import br.com.webpublico.repository.setter.FaleConoscoSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class FaleConoscoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    IdJDBCRepository idJDBCRepository;

    public FaleConoscoNfseDTO inserir(FaleConoscoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" INSERT INTO FALECONOSCONFSE (ID, SITUACAO, TIPO, ASSUNTO, DATAENVIO, NOME, EMAIL, TELEFONE, MENSAGEM) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)  ", new FaleConoscoSetter(dto));
        return dto;
    }
}
