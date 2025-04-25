package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ArquivoComposicaoNfseDTO;
import br.com.webpublico.domain.dto.DetentorArquivoComposicaoNfseDTO;
import br.com.webpublico.repository.mapper.DetentorArquivoComposicaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class DetentorArquivoComposicaoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public DetentorArquivoComposicaoNfseDTO findById(Long id) {
        List<DetentorArquivoComposicaoNfseDTO> query = jdbcTemplate.query(" SELECT ID, ARQUIVOCOMPOSICAO_ID " +
                        " FROM DETENTORARQUIVOCOMPOSICAO where id = ? ", new Object[]{id},
                new DetentorArquivoComposicaoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

}
