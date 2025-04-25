package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.CnaeNfseDTO;
import br.com.webpublico.service.CnaeService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cnaes.
 */
@RestController
@RequestMapping("/api")
public class CnaeResource {

    private final Logger log = LoggerFactory.getLogger(CnaeResource.class);

    @Inject
    private CnaeService cnaeService;

    /**
     * GET  /cnaes -> get all the cnae.
     */
    @RequestMapping(value = "/cnaes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CnaeNfseDTO>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                    @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        return cnaeService.findAll(PaginationUtil.generatePageRequest(offset, limit));
    }

    /**
     * GET  /cnaes/:id -> get the "id" cnae.
     */
    @RequestMapping(value = "/cnaes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CnaeNfseDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Cnae : {}", id);
        return cnaeService.findOne(id);
    }

    @RequestMapping(value = "/cnaes_por_codigo/{codigo}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CnaeNfseDTO> getPorCodigo(@PathVariable String codigo) {
        return Optional.ofNullable(cnaeService.findByCodigo(codigo))
            .map(municipio -> new ResponseEntity<>(
                municipio,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.OK));
    }

    /**
     * SEARCH  /_search/cnaes/:query -> search for the cnae corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/cnaes/{query}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CnaeNfseDTO>> search(@PathVariable String query) {
        return cnaeService.findByQuery(query);
    }


    @RequestMapping(value = "/cnaes-por-empresa/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CnaeNfseDTO>> getPorEnpresa(@PathVariable Long id) {
        return cnaeService.findByEmpresa(id);
    }

    @RequestMapping(value = "/cnae-por-servico",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CnaeNfseDTO>> getPorServico(
        @RequestParam(value = "page", required = false) Integer offset,
        @RequestParam(value = "per_page", required = false) Integer limit,
        @RequestParam(value = "filtro", required = false) String filtro) {
        return cnaeService.getPorServico(PaginationUtil.generatePageRequest(offset, limit), filtro);
    }

}
