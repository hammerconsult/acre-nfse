package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ConsultaGenericaNfseDTO;
import br.com.webpublico.domain.dto.NFSAvulsaNfseDTO;
import br.com.webpublico.domain.dto.NFSAvulsaSearchNfseDTO;
import br.com.webpublico.service.NFSAvulsaService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing NotaFiscal.
 */
@RestController
@RequestMapping("/")
public class NFSAvulsaResource {

    private final Logger log = LoggerFactory.getLogger(NFSAvulsaResource.class);

    @Inject
    private NFSAvulsaService nfsAvulsaService;

    @RequestMapping(value = "/api/notaFiscalAvulsa",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NFSAvulsaNfseDTO> create(@RequestBody @Valid NFSAvulsaNfseDTO dto) {
        log.debug("REST request to save NotaFiscal : {}", dto);
        if (dto.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new notaFiscalAvulsa cannot already have an ID").body(null);
        }
        NFSAvulsaNfseDTO result = nfsAvulsaService.save(dto);
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/api/notaFiscalAvulsa/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NFSAvulsaNfseDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(nfsAvulsaService.findById(id));
    }


    @RequestMapping(value = "/api/notaFiscalAvulsa/search",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NFSAvulsaSearchNfseDTO>> getAll(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) {
        return nfsAvulsaService.buscarNFSAvulsa(consultaGenerica);
    }

    @RequestMapping(value = "/api/imprimir-nota-fiscal-avulsa/{id}",
            method = RequestMethod.GET)
    @Timed
    public void imprimirNotaFiscalAvulsa(@PathVariable Long id, @RequestHeader Long prestador, HttpServletResponse response) {
        nfsAvulsaService.imprimirNotaFiscalAvulsa(response, prestador, id);
    }
}
