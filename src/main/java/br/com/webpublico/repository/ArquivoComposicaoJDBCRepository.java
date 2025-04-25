package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ArquivoComposicaoNfseDTO;
import br.com.webpublico.domain.dto.DetentorArquivoComposicaoNfseDTO;
import br.com.webpublico.repository.mapper.ArquivoComposicaoMapper;
import br.com.webpublico.repository.mapper.DetentorArquivoComposicaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ArquivoComposicaoJDBCRepository implements Serializable {

    private static final String SQL_PRINCIPAL = " SELECT A.ID AS ID, A.ARQUIVO_ID AS ARQUIVO_ID, " +
            " A.DATAUPLOAD AS DATAUPLOAD " +
            " FROM ARQUIVOCOMPOSICAO A ";
    @Autowired
    JdbcTemplate jdbcTemplate;

    public ArquivoComposicaoNfseDTO findById(Long id) {
        List<ArquivoComposicaoNfseDTO> query =
                jdbcTemplate.query(SQL_PRINCIPAL +
                                " WHERE A.ID = ? ",
                        new Object[]{id},
                        new ArquivoComposicaoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<ArquivoComposicaoNfseDTO> findByIdDetentor(Long idDetentor) {
        return jdbcTemplate.query(SQL_PRINCIPAL +
                        " WHERE A.DETENTORARQUIVOCOMPOSICAO_ID = ? ",
                new Object[]{idDetentor},
                new ArquivoComposicaoMapper());
    }

}
