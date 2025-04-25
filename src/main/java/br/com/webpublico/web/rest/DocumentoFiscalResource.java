package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.DocumentoFiscalDTO;
import br.com.webpublico.service.DocumentoFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documento-fiscal")
public class DocumentoFiscalResource extends AbstractResource<DocumentoFiscalDTO> {

    @Autowired
    DocumentoFiscalService service;

    @Override
    protected DocumentoFiscalService getService() {
        return service;
    }
    
}
