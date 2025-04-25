package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.TipoDocumentoServicoDeclaradoNfseDTO;
import br.com.webpublico.repository.mapper.TipoDocumentoServicoDeclaradoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class TipoDocumentoServicoDeclaradoJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public TipoDocumentoServicoDeclaradoNfseDTO findById(Long id) {
        List<TipoDocumentoServicoDeclaradoNfseDTO> query = jdbcTemplate.query(" select id, descricao, subserie, ativo " +
                " from tipodocservicodeclarado " +
                " where id = ? ", new Object[]{id}, new TipoDocumentoServicoDeclaradoMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public List<TipoDocumentoServicoDeclaradoNfseDTO> findAll() {
        return jdbcTemplate.query(" select id, descricao, subserie, ativo " +
                " from tipodocservicodeclarado ", new TipoDocumentoServicoDeclaradoMapper());
    }
}
