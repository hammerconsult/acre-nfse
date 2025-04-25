package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConfiguracaoGovBrDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ConfiguracaoGovBrJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracaoGovBrService extends AbstractService<ConfiguracaoGovBrDTO> {

    @Autowired
    private ConfiguracaoGovBrJDBCRepository repository;
    private ConfiguracaoGovBrDTO configuracaoGovBr;

    @Override
    public AbstractJDBCRepository<ConfiguracaoGovBrDTO> getRepository() {
        return repository;
    }

    public ConfiguracaoGovBrDTO getConfiguracaoGovBr() {
        if (configuracaoGovBr == null) {
            configuracaoGovBr = repository.getConfiguracaoGovBr();
        }
        return configuracaoGovBr;
    }
}
