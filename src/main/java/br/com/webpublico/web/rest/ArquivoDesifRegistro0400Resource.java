package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0400NfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifRegistro0400Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/arquivo-desif-registro-0400")
public class ArquivoDesifRegistro0400Resource extends AbstractResource<ArquivoDesifRegistro0400NfseDTO> {

    @Inject
    ArquivoDesifRegistro0400Service service;

    @Override
    protected AbstractService getService() {
        return service;
    }
}
