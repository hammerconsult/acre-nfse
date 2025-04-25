package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifRegistro0410NfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifRegistro0410Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/arquivo-desif-registro-0410")
public class ArquivoDesifRegistro0410Resource extends AbstractResource<ArquivoDesifRegistro0410NfseDTO> {

    @Inject
    ArquivoDesifRegistro0410Service service;

    @Override
    protected AbstractService getService() {
        return service;
    }
}
