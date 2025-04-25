package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.CacheSistemaNfseDTO;
import br.com.webpublico.domain.dto.ConsultaGenericaNfseDTO;
import br.com.webpublico.service.CacheSistemaService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/cache-sistema")
public class CacheSistemaResource implements Serializable {

    @Autowired
    private CacheSistemaService cacheSistemaService;

    @RequestMapping(value = "/list", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CacheSistemaNfseDTO>> buscarCaches(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws URISyntaxException {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<CacheSistemaNfseDTO> caches = cacheSistemaService.buscarCaches(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(caches, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(caches.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        cacheSistemaService.removeCache(id);
    }
}
