package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ConsultaLivroFiscalNfseDTO;
import br.com.webpublico.domain.dto.LivroFiscalCompetenciaNfseDTO;
import br.com.webpublico.domain.dto.WebReportDTO;
import br.com.webpublico.service.LivroFiscalService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing DeclaracaoMensalServico.
 */
@RestController
@RequestMapping("/api")
public class LivroFiscalResource {

    private final Logger log = LoggerFactory.getLogger(LivroFiscalResource.class);


    @Inject
    private LivroFiscalService livroFiscalervice;


    @PostMapping(value = "/competencias-livro-fiscal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LivroFiscalCompetenciaNfseDTO>> buscarCompetenciasLivroFiscal(
            @RequestHeader Long prestador,
            @RequestBody ConsultaLivroFiscalNfseDTO consulta) {
        consulta.setPrestadorId(prestador);
        List<LivroFiscalCompetenciaNfseDTO> competencias =
                livroFiscalervice.buscarCompetenciasLivroFiscal(consulta);
        return new ResponseEntity(competencias, HttpStatus.OK);

    }

    @RequestMapping(value = "/imprimir-livro-fiscal",
            method = RequestMethod.POST)
    @Timed
    public ResponseEntity<WebReportDTO> imprimirLivroFiscal(@RequestBody LivroFiscalCompetenciaNfseDTO dto) {
        return ResponseEntity.ok(livroFiscalervice.imprimirLivroFiscal(dto));
    }
}
