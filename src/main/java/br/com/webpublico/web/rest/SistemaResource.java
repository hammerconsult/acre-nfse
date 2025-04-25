package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.SistemaInfoNfseDTO;
import br.com.webpublico.service.SistemaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.Serializable;

@RestController
@RequestMapping("/")
public class SistemaResource implements Serializable {

    @Inject
    private SistemaService sistemaService;

    @RequestMapping(value = "/api/sistema/versao",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SistemaInfoNfseDTO> getVersaoSistema() {
        return new ResponseEntity<>(sistemaService.getSistemaInfo(), HttpStatus.OK);
    }
}
