package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.CosifNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.CosifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/cosif")
public class CosifResource extends AbstractResource<CosifNfseDTO> {

    @Autowired
    CosifService service;

    @GetMapping(value = "/buscar-por-conta",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CosifNfseDTO> buscarPorConta(@RequestParam String conta) {
        return ResponseEntity.ok(service.findByConta(conta));
    }
    
    @Override
    protected AbstractService getService() {
        return service;
    }
}
