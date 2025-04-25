package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.CodigoTributacaoService;
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
@RequestMapping("/api/codigo-tributacao")
public class CodigoTributacaoResource extends AbstractResource<CodigoTributacaoNfseDTO> {

    @Autowired
    private CodigoTributacaoService service;

    @Override
    protected AbstractService getService() {
        return service;
    }

    @GetMapping(value = "/buscar-por-codigo",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CodigoTributacaoNfseDTO> findByCodigo(@RequestParam String codigo) throws Exception {
        CodigoTributacaoNfseDTO byCodigo = service.findByCodigo(codigo);
        return new ResponseEntity<>(byCodigo, HttpStatus.OK);
    }

}
