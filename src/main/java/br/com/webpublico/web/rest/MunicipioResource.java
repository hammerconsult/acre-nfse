package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.service.MunicipioService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Municipio.
 */
@RestController
@RequestMapping("/api")
public class MunicipioResource {

    private final Logger log = LoggerFactory.getLogger(MunicipioResource.class);

    @Inject
    private MunicipioService municipioService;

    /**
     * GET  /municipios -> get all the municipios.
     */
    @RequestMapping(value = "/municipios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MunicipioNfseDTO>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                         @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get a page of Municipios");
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        return municipioService.findAll(pageable);
    }

    /**
     * GET  /municipios/:id -> get the "id" municipio.
     */
    @RequestMapping(value = "/municipios/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MunicipioNfseDTO> get(@PathVariable Long id) {
        log.debug("REST request to get one Municipio {}", id);
        return municipioService.findOne(id);
    }

    @RequestMapping(value = "/municipios_por_codigo/{codigo}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MunicipioNfseDTO> getPorCodigo(@PathVariable String codigo) {
        log.debug("REST request to get Municipio by código : {}", codigo);
        return Optional.ofNullable(municipioService.findByAtribute("codigo", codigo))
            .map(municipio -> new ResponseEntity<>(
                municipio,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.OK));
    }

    /**
     * SEARCH  /_search/municipios/:query -> search for the municipio corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/municipios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MunicipioNfseDTO>> search(@RequestParam(value = "page") Integer offset,
                                                         @RequestParam(value = "per_page") Integer limit,
                                                         @RequestParam(value = "filtro", required = false) String filtro) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        Page<MunicipioNfseDTO> page = municipioService.search(pageable, filtro);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/municipio_por_descricao/{nome}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MunicipioNfseDTO> getPorDescricao(@PathVariable String nome) {
        return Optional.ofNullable(municipioService.findByAtribute("nome", nome))
            .map(municipio -> new ResponseEntity<>(
                municipio,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
