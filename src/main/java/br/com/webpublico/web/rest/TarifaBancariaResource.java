package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.TarifaBancariaNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.TarifaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/tarifa-bancaria")
public class TarifaBancariaResource extends AbstractResource<TarifaBancariaNfseDTO> {

    @Autowired
    private TarifaBancariaService service;

    @GetMapping(value = "/buscar-por-codigo",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TarifaBancariaNfseDTO> buscarPorCodigo(@RequestParam String codigo) {
        return ResponseEntity.ok(service.findByCodigo(codigo));
    }

    @Override
    protected AbstractService getService() {
        return service;
    }
}
