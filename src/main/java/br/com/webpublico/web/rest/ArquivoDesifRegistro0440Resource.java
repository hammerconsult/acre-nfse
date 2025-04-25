package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0440NfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifRegistro0440Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/arquivo-desif-registro-0440")
public class ArquivoDesifRegistro0440Resource extends AbstractResource<ArquivoDesifRegistro0440NfseDTO> {

    @Inject
    ArquivoDesifRegistro0440Service service;

    @Override
    protected AbstractService getService() {
        return service;
    }
}
