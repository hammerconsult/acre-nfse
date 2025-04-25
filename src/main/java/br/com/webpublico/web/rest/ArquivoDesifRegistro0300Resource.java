package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0300NfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifRegistro0300Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/arquivo-desif-registro-0300")
public class ArquivoDesifRegistro0300Resource extends AbstractResource<ArquivoDesifRegistro0300NfseDTO> {

    @Inject
    ArquivoDesifRegistro0300Service service;

    @Override
    protected AbstractService getService() {
        return service;
    }
}
