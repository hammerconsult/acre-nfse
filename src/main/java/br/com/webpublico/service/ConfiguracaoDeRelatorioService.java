package br.com.webpublico.service;

import br.com.webpublico.config.UrlsProperties;
import br.com.webpublico.domain.dto.ConfiguracaoDeRelatorioNfseDTO;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ConfiguracaoDeRelatorioJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class ConfiguracaoDeRelatorioService extends AbstractService<ConfiguracaoDeRelatorioNfseDTO> {

    @Autowired
    private ConfiguracaoDeRelatorioJDBCRepository repository;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Autowired
    private UrlsProperties urlsProperties;

    @Override
    public ConfiguracaoDeRelatorioJDBCRepository getRepository() {
        return repository;
    }

    @Transactional
    public ConfiguracaoDeRelatorioNfseDTO getConfiguracaoDeRelatorio() throws NfseOperacaoNaoPermitidaException {
        ConfiguracaoDeRelatorioNfseDTO configuracao = repository.findByAtributo(" lower(obj.chave) ",
                dbUsername.toLowerCase());
        if (configuracao == null) {
            throw new NfseOperacaoNaoPermitidaException("Configuração de relatório não encontrada.");
        }
        configuracao.setUrlWebpublico(urlsProperties.getNfse() + "/");
        return configuracao;
    }
}
