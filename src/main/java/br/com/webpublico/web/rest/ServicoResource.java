package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ServicoNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ServicoService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Servico.
 */
@RestController
@RequestMapping("/api/servico")
public class ServicoResource extends AbstractResource<ServicoNfseDTO> {

    @Autowired
    private ServicoService service;

    @Override
    protected AbstractService getService() {
        return service;
    }

    @RequestMapping(value = "/por-codigo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServicoNfseDTO> getByCodigo(@RequestParam String codigo) {
        return new ResponseEntity<>(service.findByCodigo(codigo), HttpStatus.OK);
    }

    @RequestMapping(value = "/por-prestador",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ServicoNfseDTO>> getByPrestador(@RequestHeader Long prestador) {
        return new ResponseEntity<>(service.findByIdCadastro(prestador), HttpStatus.OK);
    }
}
