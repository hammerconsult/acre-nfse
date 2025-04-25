package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ConsultaGenericaNfseDTO;
import br.com.webpublico.domain.dto.TomadorServicoDTO;
import br.com.webpublico.service.TomadorService;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tomador.
 */
@RestController
@RequestMapping("/api")
public class TomadorResource {

    private final Logger log = LoggerFactory.getLogger(TomadorResource.class);

    @Inject
    private TomadorService tomadorService;

    @RequestMapping(value = "/tomadors/consultar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TomadorServicoDTO>> consultar(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<TomadorServicoDTO> page = tomadorService.consultarTomadores(pageable,
                consultaGenerica.getParametrosQuery(), consultaGenerica.getOrderBy());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tomadors/:id -> get the "id" tomador.
     */
    @RequestMapping(value = "/tomadors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TomadorServicoDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Tomador : {}", id);
        return new ResponseEntity<>(tomadorService.findById(id), HttpStatus.OK);
    }


    @RequestMapping(value = "/tomador_pessoa_repo/{cpfCnpj}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TomadorServicoDTO> getInPessoaRepo(@PathVariable String cpfCnpj) {
        log.debug("REST request to get Tomador in Pessoa Repo: {}", cpfCnpj);
        try {
            return Optional.ofNullable(tomadorService.findInPessoaRepoByCpfCnpj(cpfCnpj))
                    .map(tomador -> new ResponseEntity<>(
                            tomador,
                            HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            log.debug("Não foi encontrada nenhuma pessoa {}", cpfCnpj);
        }
        return null;
    }

    @RequestMapping(value = "/tomador_por_cpfCnpj/{cpfCnpj}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TomadorServicoDTO> getPorCpfCnpj(
            @PathVariable String cpfCnpj,
            @RequestHeader Long prestador) {
        log.debug("REST request to get Tomador in Pessoa Repo: {}", cpfCnpj);
        try {
            return Optional.ofNullable(tomadorService.findByCnpjAndPrestador(cpfCnpj, prestador))
                    .map(tomador -> new ResponseEntity<>(
                            tomador,
                            HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            log.debug("Não foi encontrada nenhuma pessoa, CPF/CNPJ {} ", cpfCnpj);
        }
        return null;
    }

    @RequestMapping(value = "/tomadors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TomadorServicoDTO> save(@RequestBody TomadorServicoDTO tomador) throws URISyntaxException {
        TomadorServicoDTO result = tomadorService.salvar(tomador);
        return ResponseEntity.created(new URI("/api/tomadors/" + tomador.getId())).body(result);
    }


    /**
     * DELETE  /tomadors/:id -> delete the "id" tomador.
     */
    @RequestMapping(value = "/tomadors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Tomador : {}", id);
        tomadorService.delete(id);
    }

    /**
     * SEARCH  /_search/tomadors/:query -> search for the tomador corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/tomadors/{query}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TomadorServicoDTO>> search(@PathVariable String query, @RequestHeader Long prestador) {
        return tomadorService.findByQuery(query, prestador);
    }
}
