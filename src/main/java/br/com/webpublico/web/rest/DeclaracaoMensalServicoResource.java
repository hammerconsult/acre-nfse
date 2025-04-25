package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.enumeration.Mes;
import br.com.webpublico.service.DeclaracaoMensalServicoService;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * REST controller for managing DeclaracaoMensalServico.
 */
@RestController
@RequestMapping("/api")
public class DeclaracaoMensalServicoResource {

    private final Logger log = LoggerFactory.getLogger(DeclaracaoMensalServicoResource.class);


    @Inject
    private DeclaracaoMensalServicoService declaracaoMensalServicoService;


    @RequestMapping(value = "/declaracaoMensalServicos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeclaracaoMensalServicoNfseDTO> create(@RequestBody DeclaracaoMensalServicoNfseDTO dto, @RequestHeader Long prestador) throws URISyntaxException {
        log.debug("REST request to save DeclaracaoMensalServicoEditDTO: {}", dto);
        if (dto.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new declaracaoMensalServico cannot already have an ID").body(null);
        }
        DeclaracaoMensalServicoNfseDTO save = declaracaoMensalServicoService.save(dto);
        return ResponseEntity.created(new URI("/api/declaracaoMensalServicos/" + save.getId())).body(save);
    }


    @RequestMapping(value = "/declaracaoMensalServicos/consultar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DeclaracaoMensalServicoNfseDTO>> buscarDeclaracoes(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<DeclaracaoMensalServicoNfseDTO> declaracoes = declaracaoMensalServicoService.buscarDeclaracaoMensalServico(pageable,
                consultaGenerica.getParametrosQuery(), consultaGenerica.getOrderBy());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(declaracoes, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(declaracoes.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/estatistica-mensal-declarada-por-periodo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EstatisticaMensalNfseDTO> getEstatiscas(@RequestHeader Long prestador,
                                                                  @RequestParam("mes") Integer mes,
                                                                  @RequestParam("ano") Integer ano) {
        EstatisticaMensalNfseDTO estatisticaMensalNfseDTO = declaracaoMensalServicoService
                .buscarServicosPrestadosNaReferencia(mes, ano, prestador);
        return ResponseEntity.ok(estatisticaMensalNfseDTO);
    }

    @RequestMapping(value = "/declaracaoMensalServicos/meses",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Mes>> getMeses() {
        log.debug("REST request to getMeses in DeclaracaoMensalServico");
        List<Mes> meses = Arrays.asList(Mes.values());
        return new ResponseEntity<>(meses, HttpStatus.OK);
    }


    @RequestMapping(value = "/declaracaoMensalServicos/meses_nao_declarados",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReferenciaPeriodoNfseDTO[]> getMesesNaoDeclarados(@RequestHeader Long prestador) {
        log.debug("REST request to getMeses in DeclaracaoMensalServico");
        return declaracaoMensalServicoService.buscarMesesNaoDeclaradosDoPrestador(prestador);
    }

    @RequestMapping(value = "/declaracaoMensalServicos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeclaracaoMensalServicoNfseDTO> get(@PathVariable Long id) {
        return new ResponseEntity<>(declaracaoMensalServicoService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/dms-by-calculo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeclaracaoMensalServicoNfseDTO> getByCalculoId(@RequestParam Long calculoId) {
        return new ResponseEntity<>(declaracaoMensalServicoService.findByCalculoId(calculoId), HttpStatus.OK);
    }

    /**
     * DELETE  /declaracaoMensalServicos/:id -> delete the "id" declaracaoMensalServico.
     */
    @RequestMapping(value = "/declaracaoMensalServicos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete DeclaracaoMensalServico : {}", id);
        declaracaoMensalServicoService.delete(id);
    }

    @RequestMapping(value = "/imprimir-relatorio-dms/{idDms}",
            method = RequestMethod.GET)
    @Timed
    public void imprimirRelatorioDMS(@PathVariable Long idDms, HttpServletResponse response) {
        log.debug("REST request to imprimirRelatorioDMS : {}", idDms);
        declaracaoMensalServicoService.imprimirRelatorioDms(response, idDms);
    }

    @RequestMapping(value = "/imprimir-relatorio-dms/{ano}/{mes}/{tipoMovimento}",
            method = RequestMethod.GET)
    @Timed
    public void imprimirRelatorioDMS(@PathVariable int ano, @PathVariable int mes, @PathVariable String tipoMovimento, @RequestHeader Long prestador, HttpServletResponse response) {
        log.debug("REST request to imprimirRelatorioDMS : {}, {} , {} , {} ", ano, mes, tipoMovimento);
        declaracaoMensalServicoService.imprimirRelatorioDms(response, prestador, ano, mes, tipoMovimento);
    }

    @RequestMapping(value = "/cancelar-declaracao-mensal-servico/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeclaracaoMensalServicoNfseDTO> cancelarDeclaracaoMensalServico(@PathVariable Long id) {
        return ResponseEntity.ok(declaracaoMensalServicoService.cancelar(id));
    }

    @RequestMapping(value = "/imprimir-composicao-guia-nfse/{prestadorId}/{damId}",
            method = RequestMethod.GET)
    @Timed
    public void imprimirRelatorioDMS(@PathVariable Long prestadorId,
                                     @PathVariable Long damId,
                                     HttpServletResponse response) {
        declaracaoMensalServicoService.imprimeComposicaoGuiaNfse(response, prestadorId, damId);
    }

    @RequestMapping(value = "/resumo-por-dms",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResumoDMSDTO> resumoPorDms(@RequestParam(value = "idDMS") long idDMS) {
        ResumoDMSDTO resumo = declaracaoMensalServicoService.buscarResumoPorDMS(idDMS);
        return new ResponseEntity<>(resumo, HttpStatus.OK);
    }
}
