package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.TemplateNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import br.com.webpublico.repository.mapper.TemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class TemplateJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private IdJDBCRepository idJDBCRepository;

    public TemplateNfseDTO findByTipo(TipoTemplateNfse tipo) {
        List<TemplateNfseDTO> query = jdbcTemplate.query(" SELECT id, conteudo from TemplateNfse temp where temp.tipoTemplate = ?", new Object[]{tipo.name()},
                new TemplateMapper());
        if(!query.isEmpty()){
            return query.get(0);
        }
        return null;
    }
}
