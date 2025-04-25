package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.CartaCorrecaoNfseDTO;
import br.com.webpublico.repository.mapper.CartaCorrecaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class CartaCorrecaoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public CartaCorrecaoNfseDTO findById(Long id) {
        List<CartaCorrecaoNfseDTO> query = jdbcTemplate.query(" SELECT  * from CARTACORRECAONFSE WHERE ID = ? ",
                new Object[]{id},
                new CartaCorrecaoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<CartaCorrecaoNfseDTO> findByNotaFiscalId(Long id) {
        return jdbcTemplate.query(" SELECT  * from CARTACORRECAONFSE WHERE NOTAFISCAL_ID = ? ",
                new Object[]{id},
                new CartaCorrecaoMapper());
    }


}
