package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ConstrucaoCivilLocalizacaoNfseDTO;
import br.com.webpublico.repository.mapper.ConstrucaoCivilLocalizacaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ConstrucaoCivilLocalizacaoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public ConstrucaoCivilLocalizacaoNfseDTO findById(Long id) {
        List<ConstrucaoCivilLocalizacaoNfseDTO> query = jdbcTemplate.query("SELECT ID, " +
                        "       CIDADE_ID, " +
                        "       LOGRADOURO, " +
                        "       NUMERO, " +
                        "       BAIRRO, " +
                        "       CEP, " +
                        "       NOMEEMPREENDIMENTO " +
                        "FROM CONSTRUCAOCIVILLOCALIZACAO ",
                new Object[]{id},
                new ConstrucaoCivilLocalizacaoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }
}
