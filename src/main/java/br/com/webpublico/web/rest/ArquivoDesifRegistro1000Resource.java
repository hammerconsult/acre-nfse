package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro1000NfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifRegistro1000Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/arquivo-desif-registro-1000")
public class ArquivoDesifRegistro1000Resource extends AbstractResource<ArquivoDesifRegistro1000NfseDTO> {

    @Inject
    ArquivoDesifRegistro1000Service service;

    @Override
    protected AbstractService getService() {
        return service;
    }
    

}
