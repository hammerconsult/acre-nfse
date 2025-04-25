package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ManualDTO;
import br.com.webpublico.service.ManualService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by wellington on 17/10/17.
 */
@RestController
@RequestMapping("/api/externo")
public class ManualResource {

    private final Logger log = LoggerFactory.getLogger(ManualResource.class);

    @Inject
    private ManualService manualService;

    @GetMapping("/manuais-para-exibicao")
    @Timed
    public ResponseEntity<List<ManualDTO>> buscarManuaisParaExibicao() {
        return new ResponseEntity<>(manualService.buscarManuaisParaExibicao(), HttpStatus.OK);
    }

    @GetMapping("/manuais-por-tag")
    @Timed
    public ResponseEntity<List<ManualDTO>> buscarManuaisPorTag(@RequestParam(value = "tag", required = false) String tag) {
        return new ResponseEntity<>(manualService.buscarManuaisPorTag(tag), HttpStatus.OK);
    }

    @GetMapping("/manuais-por-tipo")
    @Timed
    public ResponseEntity<List<ManualDTO>> buscarManuaisPorTipo(@RequestParam Long tipo) {
        return new ResponseEntity<>(manualService.buscarManuaisPorTipo(tipo), HttpStatus.OK);
    }
}
