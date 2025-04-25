package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0430NfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifRegistro0430Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/arquivo-desif-registro-0430")
public class ArquivoDesifRegistro0430Resource extends AbstractResource<ArquivoDesifRegistro0430NfseDTO> {

    @Inject
    ArquivoDesifRegistro0430Service service;

    @Override
    protected AbstractService getService() {
        return service;
    }
}
