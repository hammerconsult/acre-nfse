package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.service.NotaNacionalService;
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

import java.util.List;


@RestController
@RequestMapping("/api/nota-nacional")
public class NotaNacionalResource {

    @Autowired
    private NotaNacionalService notaNacionalService;

    @RequestMapping(value = "/integrar",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> integrarNotaNacional(@RequestParam Long notaFiscalId) {
        notaNacionalService.integrarNotaNacional(notaFiscalId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/reintegrar-notas-por-codigo-erro",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> reintegrarNotasPorCodigoErro(@RequestParam String codigoErro) {
        notaNacionalService.reintegrarNotasPorCodigoErro(codigoErro);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/retornos-dfe-adn", method = RequestMethod.GET)
    public ResponseEntity<List<RetornoDfeAdnDTO>> buscarRetornosDfeAdn(@RequestParam Long notaFiscalId) {
        return ResponseEntity.ok(notaNacionalService.buscarRetornosDfeAdn(notaFiscalId));
    }

    @RequestMapping(value = "/mensagens-dfe-adn", method = RequestMethod.GET)
    public ResponseEntity<List<MensagemDfeAdnDTO>> buscarMensagensDfeAdn(@RequestParam Long retornoId) {
        return ResponseEntity.ok(notaNacionalService.buscarMensagensDfeAdn(retornoId));
    }

    @RequestMapping(value = "/buscar-integracoes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<IntegracaoNotaNacionalDTO>> buscarIntegracoes(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<IntegracaoNotaNacionalDTO> integracoes = notaNacionalService.buscarIntegracoes(pageable, consultaGenerica.getParametrosQuery());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(integracoes, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(integracoes.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/buscar-quantidade-por-erro",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GraficoQuantitativoNotaNacionalDTO>> buscarQuantidadePorErro() {
        return new ResponseEntity<>(notaNacionalService.buscarQuantidadePorErro(), HttpStatus.OK);
    }

    @RequestMapping(value = "/buscar-quantidade-por-status",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GraficoQuantitativoNotaNacionalDTO>> buscarQuantidadePorStatus() {
        return new ResponseEntity<>(notaNacionalService.buscarQuantidadePorStatus(), HttpStatus.OK);
    }

}
