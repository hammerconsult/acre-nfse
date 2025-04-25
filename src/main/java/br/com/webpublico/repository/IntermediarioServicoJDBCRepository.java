package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.IntermediarioServicoNfseDTO;
import br.com.webpublico.repository.mapper.IntermediarioMapper;
import br.com.webpublico.repository.setter.IntermediarioServicoSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class IntermediarioServicoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public void inserirIntermediarioServico(IntermediarioServicoNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate("INSERT INTO INTERMEDIARIOSERVICO " +
                        "(ID, PESSOA_ID, CPFCNPJ, NOMERAZAOSOCIAL) " +
                        "VALUES(?, ?, ?, ?) ",
                new IntermediarioServicoSetter(dto));
    }

    public IntermediarioServicoNfseDTO findById(Long id) {
        List<IntermediarioServicoNfseDTO> query = jdbcTemplate.query(" SELECT ID, " +
                        " PESSOA_ID, " +
                        " CPFCNPJ, " +
                        " NOMERAZAOSOCIAL " +
                        " FROM INTERMEDIARIOSERVICO " +
                        " WHERE ID = ? ",
                new Object[]{id},
                new IntermediarioMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }
}
