package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.NoticiaNfseDTO;
import br.com.webpublico.service.NoticiaNfseService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import com.beust.jcommander.internal.Lists;
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
import java.util.List;


@RestController
@RequestMapping("/api/externo/")
public class NoticiaResource {

    private final Logger log = LoggerFactory.getLogger(NoticiaResource.class);

    @Inject
    private NoticiaNfseService service;

    @RequestMapping(value = "ultimas-noticias",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NoticiaNfseDTO>> buscarUltimasNoticias() {
        return new ResponseEntity<>(service.buscarUltimasNoticias(), HttpStatus.OK);
    }

    @RequestMapping(value = "noticia/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoticiaNfseDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "noticias",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NoticiaNfseDTO>> buscarNoticias(@RequestParam(value = "page", required = false) Integer offset,
                                                               @RequestParam(value = "per_page", required = false) Integer limit) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        Page<NoticiaNfseDTO> noticias = service.consultarPaginado(pageable, Lists.newArrayList(), null);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(noticias, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(noticias.getContent(), headers, HttpStatus.OK);
    }
}
