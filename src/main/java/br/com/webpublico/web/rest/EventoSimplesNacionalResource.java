package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.LinhaEventoSimplesNacionalNfseDTO;
import br.com.webpublico.service.EventoSimplesNacionalService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;


@RestController
@RequestMapping("/api")
public class EventoSimplesNacionalResource {

    private final Logger log = LoggerFactory.getLogger(EventoSimplesNacionalResource.class);

    @Inject
    private EventoSimplesNacionalService eventoSimplesNacionalService;


    @RequestMapping(value = "/evento-simples-nacional/por-empresa",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LinhaEventoSimplesNacionalNfseDTO>> getall(@RequestParam(value = "filtro", required = false) String filtro,
                                                                          @RequestHeader Long prestador) {
        return eventoSimplesNacionalService.buscarEventosPorEmpresa(prestador, filtro);
    }
}
