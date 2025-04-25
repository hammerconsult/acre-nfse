package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.NaturezaJuridicaNfseDTO;
import br.com.webpublico.service.NaturezaJuridicaService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;


@RestController
@RequestMapping("/api")
public class NaturezaJuridicaResource {

    private final Logger log = LoggerFactory.getLogger(NaturezaJuridicaResource.class);

    @Inject
    private NaturezaJuridicaService naturezaJuridicaService;


    @RequestMapping(value = "/naturezas-fisicas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NaturezaJuridicaNfseDTO>> getFisicas() {
        return naturezaJuridicaService.buscarNaturezaFisica();
    }

    @RequestMapping(value = "/naturezas-juridicas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NaturezaJuridicaNfseDTO>> getJuridicas() {
        return naturezaJuridicaService.buscarNaturezaJuridica();
    }

}
