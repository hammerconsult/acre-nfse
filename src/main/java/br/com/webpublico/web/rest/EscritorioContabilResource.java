package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.EscritorioContabilNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.EscritorioContabilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/escritorio-contabil")
public class EscritorioContabilResource extends AbstractResource<EscritorioContabilNfseDTO> {

    @Autowired
    private EscritorioContabilService service;

    @Override
    protected AbstractService getService() {
        return service;
    }
}
