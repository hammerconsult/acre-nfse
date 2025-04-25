package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.service.ServicoDeclaradoService;
import br.com.webpublico.service.TipoDocumentoServicoDeclaradoService;
import br.com.webpublico.tributario.consultadebitos.ResultadoParcela;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import net.sf.jasperreports.engine.JRException;
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
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DeclaracaoServicoTomado.
 */
@RestController
@RequestMapping("/api/servico-declarado")
public class ServicoDeclaradoResource {

    private final Logger log = LoggerFactory.getLogger(ServicoDeclaradoResource.class);

    @Inject
    private ServicoDeclaradoService servicoDeclaradoService;
    @Inject
    private TipoDocumentoServicoDeclaradoService tipoDocumentoServicoDeclaradoService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServicoDeclaradoNfseDTO> create(@RequestBody @Valid ServicoDeclaradoNfseDTO servicoDeclarado) throws Exception {
        log.debug("REST request to save ServicoDeclarado : {}", servicoDeclarado);
        if (servicoDeclarado.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new declaracaoServicoTomado cannot already have an ID").body(null);
        }
        ServicoDeclaradoNfseDTO result = servicoDeclaradoService.save(servicoDeclarado);
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/cancelar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServicoDeclaradoNfseDTO> cancelar(@RequestBody @Valid CancelamentoNfseDTO dto) throws URISyntaxException {
        log.debug("REST request to cancelar CancelarNfseDTO : {}", dto);
        ServicoDeclaradoNfseDTO result = servicoDeclaradoService.cancelar(dto);
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServicoDeclaradoNfseDTO> update(@RequestBody @Valid ServicoDeclaradoNfseDTO servicoDeclarado) throws Exception {
        log.debug("REST request to update ServicoDeclarado : {}", servicoDeclarado);
        if (servicoDeclarado.getId() == null) {
            return create(servicoDeclarado);
        }
        ServicoDeclaradoNfseDTO result = servicoDeclaradoService.save(servicoDeclarado);
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServicoDeclaradoNfseDTO> get(@PathVariable Long id) {
        log.debug("REST request to get ServicoDeclarado : {}", id);
        return new ResponseEntity<>(servicoDeclaradoService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/publico/servico-declarado/imprime/{id}",
            method = RequestMethod.GET)
    @Timed
    public void imprime(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get ServicoDeclarado : {}", id);
    }

    @RequestMapping(value = "/new",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServicoDeclaradoNfseDTO> novo(@RequestHeader Long prestador,
                                                        @RequestParam String tipoServicoDeclarado) {
        return Optional.ofNullable(servicoDeclaradoService.novo(prestador, tipoServicoDeclarado))
                .map(declaracaoServicoTomado -> new ResponseEntity<>(
                        declaracaoServicoTomado,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoDeclaradoService.delete(id);
        return ResponseEntity.ok(null);
    }


    @RequestMapping(value = "/emitida-empresa-competencia/{mes}/{ano}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ServicoDeclaradoNfseDTO>> getAllByPrestadorAndCompetencia(@PathVariable int mes, @PathVariable int ano, @RequestHeader Long empresa) {
        log.debug("REST request to get Notas Fiscais por Prestador e Competencia: {}", "Empresa: " + empresa + " Mes: " + mes + " Ano: " + ano);
        List<ServicoDeclaradoNfseDTO> notas = servicoDeclaradoService.findByEmpresaCompetenciaSemDeclarar(empresa, mes, ano);
        return new ResponseEntity<>(notas, HttpStatus.OK);
    }

    @RequestMapping(value = "/empresa-declaracao/{declaracaoId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ServicoDeclaradoNfseDTO>> getAllByPrestadorAndDeclaracao(@PathVariable Long declaracaoId, @RequestHeader Long empresa) {
        log.debug("REST request to get Notas Fiscais por Prestador e Declaracao: {}", empresa, declaracaoId);
        List<ServicoDeclaradoNfseDTO> notas = servicoDeclaradoService.findByEmpresaDeclaracao(empresa, declaracaoId);
        return new ResponseEntity<>(notas, HttpStatus.OK);
    }


    @RequestMapping(value = "/nao-declaradas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ServicoDeclaradoNfseDTO>> getAllByNaoDeclaradas(@RequestHeader Long empresa) {
        log.debug("REST request to get Notas Fiscais por Prestador e Competencia: {}", "Prestador: " + empresa);
        List<ServicoDeclaradoNfseDTO> notas = servicoDeclaradoService.findAllNaoDeclaradas(empresa);
        return new ResponseEntity<>(notas, HttpStatus.OK);
    }


    @RequestMapping(value = "/tipos-documentos-servico-declarado",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TipoDocumentoServicoDeclaradoNfseDTO>> getAllTiposServicoDeclaradoNfseDTO() {
        log.debug("REST request to getAllTiposServicoDeclaradoNfseDTO");
        List<TipoDocumentoServicoDeclaradoNfseDTO> tipos = tipoDocumentoServicoDeclaradoService.findAll();
        return new ResponseEntity<>(tipos, HttpStatus.OK);
    }


    @RequestMapping(value = "/importar-xml",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoteImportacaoDocumentoRecebidoNfseDTO> importar(@RequestBody LoteImportacaoDocumentoRecebidoNfseDTO lote) {
        log.debug("REST request to import lote : {}", lote);
        return servicoDeclaradoService.importar(lote);
    }

    @RequestMapping(value = "/lote-declaracao-servico-tomado",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LoteImportacaoDocumentoRecebidoNfseDTO>> getAllLotes(@RequestParam(value = "page", required = false) Integer offset,
                                                                                    @RequestParam(value = "per_page", required = false) Integer limit,
                                                                                    @RequestParam(value = "filtro", required = false) String filtro,
                                                                                    @RequestHeader Long prestador) {
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        return servicoDeclaradoService.buscarLotePorPrestadorServico(prestador, filtro, pageable);
    }


    @RequestMapping(value = "/lote-declaracao-servico-tomado/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoteImportacaoDocumentoRecebidoNfseDTO> getLote(@PathVariable Long id) {
        log.debug("REST request to get Rps : {}", id);
        return servicoDeclaradoService.findOneLote(id);
    }

    @RequestMapping(value = "/consultar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ServicoDeclaradoSearchNfseDTO>> buscarServicosDeclarados(@RequestBody ConsultaServicoDeclaradoDTO consultaServicoDeclarado) throws URISyntaxException {
        try {

            Pageable pageable = PaginationUtil.generatePageRequest(consultaServicoDeclarado.getOffset(), consultaServicoDeclarado.getLimit());
            Page<ServicoDeclaradoSearchNfseDTO> notas = servicoDeclaradoService
                    .buscarServicosDeclarados(pageable,
                            consultaServicoDeclarado.getParametrosQuery(), consultaServicoDeclarado.getOrderBy());
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(notas, "",
                    pageable.getPageNumber(), pageable.getPageSize());
            return new ResponseEntity<>(notas.getContent(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erro ao consultar serviço declarado", e);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/relatorio-servicos-declarados", method = RequestMethod.POST)
    public void imprimirRelatorioNotasFiscaisEmitidas(HttpServletResponse response,
                                                      @RequestBody EmissaoRelatorioServicoDeclaradoDTO emissaoRelatorioServicoDeclarado) {
        servicoDeclaradoService.gerarRelatorioServicosDeclarados(response, emissaoRelatorioServicoDeclarado);
    }

    @RequestMapping(value = "/relatorio-excel", method = RequestMethod.POST)
    public void relatorioServicosDeclaradosExcel(HttpServletResponse response,
                                                 @RequestHeader Long prestador,
                                                 @RequestBody EmissaoRelatorioServicoDeclaradoDTO emissao) {
        servicoDeclaradoService.gerarRelatorioServicosDeclaradosExcel(response, prestador, emissao);
    }

    @RequestMapping(value = "/imprime/{id}",
            method = RequestMethod.GET)
    @Timed
    public void imprime(HttpServletResponse response, @PathVariable Long id) throws JRException, IOException {
        servicoDeclaradoService.imprime(response, id);
    }

    @RequestMapping(value = "/parcelas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ResultadoParcela>> buscarParcelasServicoDeclarado(@PathVariable Long id) {
        return new ResponseEntity<>(servicoDeclaradoService.buscarParcelasServicoDeclarado(id), HttpStatus.OK);
    }
}
