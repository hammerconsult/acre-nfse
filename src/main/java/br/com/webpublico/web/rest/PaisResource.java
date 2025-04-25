package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.PaisNfseDTO;
import br.com.webpublico.service.PaisService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Paises.
 */
@RestController
@RequestMapping("/api")
public class PaisResource {

    private final Logger log = LoggerFactory.getLogger(PaisResource.class);

    @Inject
    private PaisService paisService;

    /**
     * GET  /paises -> get all the pais.
     */
    @RequestMapping(value = "/paises",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PaisNfseDTO>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                    @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit, Sort.by("id"));
        return paisService.findAll(pageable);
    }

    /**
     * GET  /paises/:id -> get the "id" pais.
     */
    @RequestMapping(value = "/paises/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaisNfseDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Pais : {}", id);
        return paisService.findOne(id);
    }

    @RequestMapping(value = "/paises_por_codigo/{codigo}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaisNfseDTO> getPorCodigo(@PathVariable String codigo) {
        return Optional.ofNullable(paisService.findByCodigo(codigo))
            .map(municipio -> new ResponseEntity<>(
                municipio,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.OK));
    }

    /**
     * SEARCH  /_search/paiss/:query -> search for the pais corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/paises/{query}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PaisNfseDTO>> search(@PathVariable String query) {
        return paisService.findByQuery(query);
    }
}
