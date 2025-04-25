package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.ConsultaGenericaNfseDTO;
import br.com.webpublico.service.AbstractService;
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

import java.util.List;

public abstract class AbstractResource<T extends AbstractEntity> {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    protected abstract AbstractService getService();

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<T> findById(@PathVariable Long id) throws Exception {
        T byId = (T) getService().findById(id);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @PostMapping(value = "/consultar",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<T>> consultar(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<T> registros = getService().consultarPaginado(pageable,
                consultaGenerica.getParametrosQuery(), consultaGenerica.getOrderBy());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(registros, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(registros.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<T> save(@RequestBody T dto) throws Exception {
        dto = (T) getService().save(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> remove(@PathVariable Long id) throws Exception {
        getService().remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
