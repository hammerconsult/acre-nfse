package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.repository.mapper.DetalheItemDeclaracaoServicoMapper;
import br.com.webpublico.repository.setter.DetalheItemDeclaracaoServicoSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class DetalheItemDeclaracaoServicoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public List<DetalheItemDeclaracaoServicoNfseDTO> findByItem(Long idItem) {
        return jdbcTemplate.query(" SELECT ID, QUANTIDADE, VALORSERVICO, DESCRICAO " +
                        " FROM DETALHEITEMDECSERVICO " +
                        " WHERE ITEMDECLARACAOSERVICO_ID = ? ",
                new Object[]{idItem},
                new DetalheItemDeclaracaoServicoMapper());
    }

    public void inserirDealhesDeclaracaoServico(ItemDeclaracaoServicoNfseDTO dto) {
        if (dto == null || dto.getDetalhes() == null || dto.getDetalhes().isEmpty()) {
            return;
        }
        for (DetalheItemDeclaracaoServicoNfseDTO detalhe : dto.getDetalhes()) {
            detalhe.setId(idJDBCRepository.getId());
            jdbcTemplate.batchUpdate("INSERT INTO DETALHEITEMDECSERVICO (ID, QUANTIDADE, VALORSERVICO, DESCRICAO, ITEMDECLARACAOSERVICO_ID) " +
                            " VALUES (?,?,?,?,?)",
                    new DetalheItemDeclaracaoServicoSetter(dto, detalhe));
        }

    }

    public void delete(DetalheItemDeclaracaoServicoNfseDTO detalhe) {
        jdbcTemplate.update(" delete from detalheitemdecservico where id = ? ", detalhe.getId());
    }
}
