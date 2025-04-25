package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ConfiguracaoGovBrDTO;
import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.repository.mapper.ConfiguracaoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ConfiguracaoGovBrJDBCRepository extends AbstractJDBCRepository<ConfiguracaoGovBrDTO> {

    @Value("${perfil.aplicacao}")
    private String perfilAplicacao;


    @Override
    public String getSelect() {
        return " select c.id, c.urlprovider, c.clientid, " +
                "       c.secret, c.redirecturi, c.codeverifier ";
    }

    @Override
    public String getFrom() {
        return "    from configuracaogovbr c ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by c.id ";
    }

    @Override
    public RowMapper<ConfiguracaoGovBrDTO> newRowMapper() {
        return new ConfiguracaoGovBrDTO();
    }

    @Override
    public ConfiguracaoGovBrDTO insert(ConfiguracaoGovBrDTO dto) {
        return null;
    }

    @Override
    public ConfiguracaoGovBrDTO update(ConfiguracaoGovBrDTO dto) {
        return null;
    }

    @Override
    public void remove(ConfiguracaoGovBrDTO dto) {
    }

    public ConfiguracaoGovBrDTO getConfiguracaoGovBr() {
        List<ConfiguracaoGovBrDTO> query = jdbcTemplate.query(getSelect() + getFrom() +
                        " where c.sistema = ? " +
                        "   and c.ambiente = ? ", new Object[]{"NFSE", perfilAplicacao},
                new ConfiguracaoGovBrDTO());
        if (query != null && !query.isEmpty()) {
            return query.get(0);
        }
        return null;

    }
}
