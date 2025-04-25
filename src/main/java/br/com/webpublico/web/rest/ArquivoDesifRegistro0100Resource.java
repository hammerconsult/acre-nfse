package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0100NfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifRegistro0100Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/arquivo-desif-registro-0100")
public class ArquivoDesifRegistro0100Resource extends AbstractResource<ArquivoDesifRegistro0100NfseDTO> {

    @Inject
    ArquivoDesifRegistro0100Service service;

    @Override
    protected AbstractService getService() {
        return service;
    }
}
