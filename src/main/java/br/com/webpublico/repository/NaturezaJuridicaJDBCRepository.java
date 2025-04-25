package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.NaturezaJuridicaNfseDTO;
import br.com.webpublico.repository.mapper.NaturezaJuridicaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class NaturezaJuridicaJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public NaturezaJuridicaNfseDTO findById(Long id) {
        List<NaturezaJuridicaNfseDTO> query = jdbcTemplate.query(" SELECT N.ID AS ID," +
                " N.DESCRICAO AS DESCRICAO " +
                " FROM NATUREZAJURIDICA N " +
                " WHERE N.ID = ? ", new Object[]{id}, new NaturezaJuridicaMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

}
