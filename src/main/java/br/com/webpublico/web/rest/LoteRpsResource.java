package br.com.webpublico.web.rest;

import br.com.webpublico.config.UrlsProperties;
import br.com.webpublico.domain.dto.ConsultaGenericaNfseDTO;
import br.com.webpublico.domain.dto.GenericoNfseDTO;
import br.com.webpublico.domain.dto.LoteRpsNfseDTO;
import br.com.webpublico.service.LoteRpsService;
import br.com.webpublico.service.RpsService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoteRpsResource {

    @Inject
    LoteRpsService loteRpsService;
    @Inject
    UrlsProperties urlsProperties;
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/lote-rps/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoteRpsNfseDTO> getLote(@PathVariable Long id) {
        return new ResponseEntity<>(loteRpsService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/buscar-lotes-rps",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LoteRpsNfseDTO>> buscarLotesRps(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<LoteRpsNfseDTO> lotes = loteRpsService.consultarLotesRps(pageable, consultaGenerica.getParametrosQuery());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(lotes, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(lotes.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/reprocessar-lote-rps",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void reprocessarLoteRps(@RequestBody LoteRpsNfseDTO lote) {
        restTemplate.exchange(urlsProperties.getRpsService() +
                        "/api/lote-rps/reprocessar-lote-rps?idLote=" + lote.getId(),
                HttpMethod.GET, null, String.class);
    }

    @RequestMapping(value = "/inverter-reprocessar-lote-rps",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity inverterReprocessarLoteRps(@RequestBody LoteRpsNfseDTO lote) {
        restTemplate.exchange(urlsProperties.getRpsService() +
                        "/api/lote-rps/inverter-reprocessar-lote-rps?idLote=" + lote.getId(),
                HttpMethod.GET, null, String.class);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/disparar-reprocessamento",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity dispararReprocessamento() {
        restTemplate.exchange(urlsProperties.getRpsService() +
                        "/api/lote-rps/disparar-reprocessamento",
                HttpMethod.GET, null, String.class);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/lote-rps/get-xml",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenericoNfseDTO> buscarXml(@RequestParam Long idLoteRps) {
        GenericoNfseDTO dto = new GenericoNfseDTO();
        dto.setValue(loteRpsService.buscarXml(idLoteRps));
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/lote-rps/get-log",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GenericoNfseDTO> buscarLog(@RequestParam Long idLoteRps) {
        GenericoNfseDTO dto = new GenericoNfseDTO();
        dto.setValue(loteRpsService.buscarLog(idLoteRps));
        return ResponseEntity.ok(dto);
    }
}
