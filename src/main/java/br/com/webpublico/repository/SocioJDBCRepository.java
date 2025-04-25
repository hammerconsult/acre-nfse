package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.SocioNfseDTO;
import br.com.webpublico.repository.mapper.SocioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class SocioJDBCRepository implements Serializable {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<SocioNfseDTO> findByIdCadastro(Long idCadastro) {
        return jdbcTemplate.query(" SELECT SCE.PROPORCAO AS PROPORCAO," +
                        " SCE.SOCIO_ID AS SOCIO_ID " +
                        " FROM SOCIEDADECADASTROECONOMICO SCE " +
                        " WHERE SCE.CADASTROECONOMICO_ID = ? ", new Object[]{idCadastro},
                new SocioMapper());
    }
}
