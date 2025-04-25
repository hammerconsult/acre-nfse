package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.service.RpsService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Rps.
 */
@RestController
@RequestMapping("/api")
public class RpsResource {

    private final Logger log = LoggerFactory.getLogger(RpsResource.class);

    @Inject
    private RpsService rpsService;


    @RequestMapping(value = "/rps",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RpsNfseDTO>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                   @RequestParam(value = "per_page", required = false) Integer limit,
                                                   @RequestParam(value = "filtro", required = false) String filtro,
                                                   @RequestParam(value = "loteId", required = false) Long loteId,
                                                   @RequestParam(value = "prestadorId", required = false) Long prestadorId) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        Page<RpsNfseDTO> page = rpsService.buscarRps(pageable, loteId, prestadorId, filtro);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/rps-por-cnpj-numero-serie",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RpsNfseDTO> getPorCnpjNumeroSerie(@RequestParam(value = "numero", required = false) String numero,
                                                            @RequestParam(value = "serie", required = false) String serie,
                                                            @RequestParam(value = "cpfCnpj", required = false) String cpfCnpj) {
        return new ResponseEntity<>(rpsService.buscarPorCmcNumeroSerie(cpfCnpj, numero, serie), HttpStatus.OK);
    }

    @RequestMapping(value = "/rps/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RpsNfseDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Rps : {}", id);
        return new ResponseEntity<>(rpsService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/_search/rps/{query}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RpsNfseDTO>> search(@PathVariable String query) {
        return rpsService.findByQuery(query);
    }


    @RequestMapping(value = "/rps/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete NotaFiscal : {}", id);
        rpsService.delete(id);
    }

    @RequestMapping(value = "/rps/importar-xml",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RetornoImportacaoLoteRpsDTO> importar(@RequestBody LoteImportacaoRPSNfseDTO lote) {
        return ResponseEntity.ok(rpsService.importar(lote));
    }

    @RequestMapping(value = "/buscar-rps",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RpsNfseDTO>> buscarNotasFiscais(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) {
        try {
            Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
            Page<RpsNfseDTO> page = rpsService
                    .consultarRps(pageable, consultaGenerica.getParametrosQuery(), consultaGenerica.getOrderBy());
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "",
                    pageable.getPageNumber(), pageable.getPageSize());
            return new ResponseEntity(page.getContent(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @RequestMapping(value = "/imprimir-relatorio-rps",
            method = RequestMethod.POST)
    @Timed
    public void imprimirRelatorioDMS(HttpServletResponse response,
                                     @RequestBody EmissaoRelatorioRpsDTO emissaoRelatorioRps) {
        rpsService.gerarRelatorioRps(response, emissaoRelatorioRps);
    }

}
