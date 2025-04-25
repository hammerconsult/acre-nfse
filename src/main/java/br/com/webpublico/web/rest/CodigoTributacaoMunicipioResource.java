package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoMunicipalNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.CodigoTributacaoMunicipalService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/codigo-tributacao-municipio")
public class CodigoTributacaoMunicipioResource extends AbstractResource<CodigoTributacaoMunicipalNfseDTO> {

    @Autowired
    CodigoTributacaoMunicipalService service;

    @Override
    protected AbstractService getService() {
        return service;
    }

    @GetMapping(value = "/buscar-por-id-codigo-tributacao",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CodigoTributacaoMunicipalNfseDTO> findByIdCodigoTributacao(@RequestParam Long idCodigoTributacao) throws Exception {
        CodigoTributacaoMunicipalNfseDTO byIdCodigoTributacao = service.findByIdCodigoTributacao(idCodigoTributacao);
        return new ResponseEntity<>(byIdCodigoTributacao, HttpStatus.OK);
    }

}
