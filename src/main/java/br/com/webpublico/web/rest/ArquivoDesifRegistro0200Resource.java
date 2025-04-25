package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0200NfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifRegistro0200Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/arquivo-desif-registro-0200")
public class ArquivoDesifRegistro0200Resource extends AbstractResource<ArquivoDesifRegistro0200NfseDTO> {

    @Inject
    ArquivoDesifRegistro0200Service service;

    @Override
    protected AbstractService getService() {
        return service;
    }
}
