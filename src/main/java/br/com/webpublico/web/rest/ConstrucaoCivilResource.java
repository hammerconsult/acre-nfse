package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ConstrucaoCivilNfseDTO;
import br.com.webpublico.service.ConstrucaoCivilService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/construcao-civil")
public class ConstrucaoCivilResource {

    private final Logger log = LoggerFactory.getLogger(ConstrucaoCivilResource.class);

    @Inject
    private ConstrucaoCivilService service;


    @GetMapping(value = "/{id}")
    public ResponseEntity<ConstrucaoCivilNfseDTO> getById(@PathVariable(value = "id") Long id) {
        return service.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        service.delete(id);
    }

    @GetMapping(value = "/from-prestador", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ConstrucaoCivilNfseDTO>> buscarTodasConstrucaoFromPrestador(@RequestParam(value = "page", required = false) Integer offset,
                                                                                           @RequestParam(value = "per_page", required = false) Integer limit,
                                                                                           @RequestParam String filtro,
                                                                                           @RequestHeader Long prestador) {
        return service.buscarTodasConstrucaoFromPrestador(PaginationUtil.generatePageRequest(offset, limit), prestador, filtro);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ConstrucaoCivilNfseDTO> createConstrucaoCivil(@RequestBody @Valid ConstrucaoCivilNfseDTO construcaoCivil) {
        return service.salvar(construcaoCivil);
    }

    @GetMapping(value = "/by-art", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ConstrucaoCivilNfseDTO> buscarConstrucaoByArt(@RequestParam(value = "art") String art) {
        return service.buscarConstrucaoCivilFromArt(art);
    }
}
