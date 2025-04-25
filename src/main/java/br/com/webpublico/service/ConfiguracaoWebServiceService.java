package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConfiguracaoWebServiceNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoWebServiceNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ConfiguracaoWebServiceJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfiguracaoWebServiceService extends AbstractService<CodigoTributacaoNfseDTO> {

    @Autowired
    ConfiguracaoWebServiceJDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public ConfiguracaoWebServiceNfseDTO findByTipo(TipoWebServiceNfseDTO tipo) {
        return repository.findByAtributo("obj.tipo", tipo.name());
    }
}
