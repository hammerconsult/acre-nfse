package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.domain.dto.exception.ValidacaoNfseWSException;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import br.com.webpublico.service.CancelamentoService;
import br.com.webpublico.service.NotaFiscalSearchService;
import br.com.webpublico.service.NotaFiscalService;
import br.com.webpublico.tributario.consultadebitos.ResultadoParcela;
import br.com.webpublico.web.rest.dto.NotaEmitidaPorMes;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing NotaFiscal.
 */
@RestController
@RequestMapping("/")
public class NotaFiscalResource {

    private final Logger log = LoggerFactory.getLogger(NotaFiscalResource.class);

    @Inject
    private NotaFiscalService service;
    @Inject
    private NotaFiscalSearchService searchService;
    @Inject
    private CancelamentoService cancelamentoService;


    @RequestMapping(value = "/iss/nfse_v2_03.xsd", method = RequestMethod.GET,
            produces = MediaType.TEXT_XML_VALUE)

    public void getXsd203(HttpServletResponse response) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("xsd/nfse_v2_03.xsd");

        response.setContentType(MediaType.TEXT_XML_VALUE);
        StreamUtils.copy(classPathResource.getInputStream(), response.getOutputStream());
    }

    @RequestMapping(value = "/iss/nfse_v1_2.xsd", method = RequestMethod.GET,
            produces = MediaType.TEXT_XML_VALUE)

    public void getxsd12(HttpServletResponse response) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("xsd/nfse_v1_2.xsd");

        response.setContentType(MediaType.TEXT_XML_VALUE);
        StreamUtils.copy(classPathResource.getInputStream(), response.getOutputStream());
    }

    /**
     * POST  /notaFiscals -> Create a new notaFiscal.
     */

    @RequestMapping(value = "/api/notaFiscals",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> create(@RequestBody @Valid NotaFiscalNfseDTO notaFiscal) {
        log.debug("REST request to save NotaFiscal : {}", notaFiscal);
        if (notaFiscal.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new notaFiscal cannot already have an ID").body(null);
        }
        NotaFiscalNfseDTO inserir = service.save(notaFiscal);
        return new ResponseEntity<>(inserir, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/nota-fiscal/calcular-valores",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> calcularValorees(@RequestBody NotaFiscalNfseDTO notaFiscal) {
        notaFiscal = service.calcularValores(notaFiscal);
        return new ResponseEntity<>(notaFiscal, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/cancelar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> cancelar(@RequestBody CancelamentoNfseDTO dto, @RequestHeader Long prestador) {
        log.debug("REST request to cancelar NotaFiscal : {}", dto);
        if (!prestador.equals(dto.getNotaFiscal().getPrestador().getId())) {
            throw new NfseOperacaoNaoPermitidaException("Apenas o prestador de serviço pode cancelar a nota fiscal");
        }
        NotaFiscalNfseDTO notaFiscal = service.cancelar(dto);
        return ResponseEntity.ok().body(notaFiscal);
    }

    @RequestMapping(value = "/api/notaFiscals/cancelamentos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CancelamentoNfseDTO>> cancelar(@PathVariable Long id) {
        List<CancelamentoNfseDTO> cancelamentos = cancelamentoService.findByNotaId(id);
        return ResponseEntity.ok().body(cancelamentos);
    }

    @RequestMapping(value = "/api/notaFiscals/cancelamentos-nao-visualizados",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CancelamentoNfseDTO>> cancelamentos(@RequestHeader Long prestador) {
        List<CancelamentoNfseDTO> cancelamentos = cancelamentoService.findNaoVisualizadosByPrestadorId(prestador);
        return ResponseEntity.ok().body(cancelamentos);
    }

    @RequestMapping(value = "/api/notaFiscals/marcar-cancelamento-visualizado",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CancelamentoNfseDTO> marcarCancelamentoVisualizado(@RequestBody CancelamentoNfseDTO dto) {
        log.debug("REST request to cancelar NotaFiscal : {}", dto);
        CancelamentoNfseDTO cancelamento = service.marcarCancelamentoVisualizado(dto);
        return ResponseEntity.ok().body(cancelamento);
    }

    @RequestMapping(value = "/api/notaFiscals/carta-correcao-impressao/{idCartaCorrecao}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void imprimirCartaCorrecao(@PathVariable Long idCartaCorrecao, HttpServletResponse response) {
        log.debug("REST request to impressao cartaCorrecao: {}", idCartaCorrecao);
        service.imprimirCartaCorrecao(response, idCartaCorrecao);
    }

    @RequestMapping(value = "/api/notaFiscals/carta-correcao",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void salvarCartaCorrecao(@RequestBody @Valid CartaCorrecaoNfseDTO dto) {
        log.debug("REST request to salvar cartaCorrecao: {}", dto);
        service.salvarCartaCorrecao(dto);
    }

    @RequestMapping(value = "/api/notaFiscals/carta-correcao-por-nota/{idNotaFiscal}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CartaCorrecaoNfseDTO>> buscarCartaCorrecaoPorNota(@PathVariable Long idNotaFiscal,
                                                                                 @RequestParam(value = "page", required = false) Integer offset,
                                                                                 @RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        Page<CartaCorrecaoNfseDTO> page = service.buscarCartaCorrecaoPorNotaFiscal(pageable, idNotaFiscal);
        HttpHeaders httpHeaders = PaginationUtil.generatePaginationHttpHeaders(page, "", pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(page.getContent(), httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/autenticar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> autenticar(@RequestBody @Valid AutenticaNfseDTO dto) throws URISyntaxException {
        log.debug("REST request to cancelar NotaFiscal : {}", dto);
        return new ResponseEntity<>(service.autenticar(dto), HttpStatus.OK);
    }

    /**
     * PUT  /notaFiscals -> Updates an existing notaFiscal.
     */
    @RequestMapping(value = "/api/notaFiscals",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> update(@RequestBody @Valid NotaFiscalNfseDTO notaFiscal) throws URISyntaxException {
        log.debug("REST request to update NotaFiscal : {}", notaFiscal);
        if (notaFiscal.getId() == null) {
            return create(notaFiscal);
        }
        return new ResponseEntity<>(service.save(notaFiscal), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/buscar-notas-fiscais",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotaFiscalSearchDTO>> buscarNotasFiscais(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<NotaFiscalSearchDTO> notas = searchService.consultarPaginado(pageable,
                consultaGenerica.getParametrosQuery(), consultaGenerica.getOrderBy());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(notas, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(notas.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/buscar-cancelamentos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CancelamentoNfseDTO>> buscarCancelamentos(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<CancelamentoNfseDTO> notas = cancelamentoService.consultarCancelamentos(pageable,
                consultaGenerica.getParametrosQuery(), consultaGenerica.getOrderBy());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(notas, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(notas.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/buscar-ultimas-dez-notas-fiscais",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotaFiscalSearchDTO>> consultarUtimasDezNotasFiscais(@RequestHeader Long prestador, @RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Page<NotaFiscalSearchDTO> notas = searchService.consultarUtimasDezNotasFiscais(prestador, consultaGenerica);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(notas, "",
                0, 10);
        return new ResponseEntity<>(notas.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notaFiscals/:id -> get the "id" notaFiscal.
     */
    @RequestMapping(value = "/api/publica/nota-fiscal/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> getNotaPublica(@PathVariable Long id) {
        log.debug("REST request to get NotaFiscal : {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> get(@PathVariable Long id) {
        log.debug("REST request to get NotaFiscal : {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscal-por-numero",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> get(@RequestHeader Long prestador, @RequestParam Long numero) {
        return new ResponseEntity<>(service.findByIdPrestadorAndNumero(prestador, numero), HttpStatus.OK);
    }

    @RequestMapping(value = "/publico/notaFiscal/imprime/{id}",
            method = RequestMethod.GET)
    @Timed
    public void imprime(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
        log.debug("REST request to get NotaFiscal : {}", id);
        service.imprimeNota(request, response, id);
    }

    @RequestMapping(value = "/publico/nota-fiscal/imprimir",
            method = RequestMethod.GET)
    @Timed
    public void imprime(@RequestParam String cpfCnpj,
                        @RequestParam String numero,
                        @RequestParam String codigo,
                        HttpServletResponse response, HttpServletRequest request) {
        AutenticaNfseDTO dto = new AutenticaNfseDTO();
        dto.setCodigoAutenticacao(codigo);
        dto.setCpfCnpjPrestador(cpfCnpj);
        dto.setNumeroNfse(numero);
        NotaFiscalNfseDTO nota = service.autenticar(dto);
        service.imprimeNota(request, response, nota.getId());
    }

    @RequestMapping(value = "/publico/notaFiscal/get-bytes/{id}",
            method = RequestMethod.GET)
    @Timed
    public byte[] getBytes(@PathVariable Long id) {
        log.debug("REST request to get NotaFiscal : {}", id);
        try {
            return service.getBytesPdf(id).toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/api/notaFiscals/new",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> novo(@RequestHeader Long prestador) {
        NotaFiscalNfseDTO notaFiscalNfseDTO = service.novoComPrestador(prestador);
        return Optional.ofNullable(notaFiscalNfseDTO)
                .map(notaFiscal -> new ResponseEntity<>(
                        notaFiscal,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/api/notaFiscals/newRpsManual",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> novoRpsManual(@RequestHeader Long prestador) {
        return Optional.ofNullable(service.novoRpsManual(prestador))
                .map(notaFiscal -> new ResponseEntity<>(
                        notaFiscal,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/api/notaFiscals/discriminacoes", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<String>> buscarDiscriminacoesPorPrestador(@RequestHeader Long prestador,
                                                                         @RequestParam(value = "page", required = false) Integer offset,
                                                                         @RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        Page<String> discriminacoes = service.buscarDiscriminacoesPorPrestador(pageable, prestador);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(discriminacoes, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(discriminacoes.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/nota_emita_por_mes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotaEmitidaPorMes>> getAll()
            throws URISyntaxException {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notas-sem-declarar-por-competencia",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotaFiscalSearchDTO>> buscarNotasSemDeclararPorCompetencia(@RequestParam(value = "mes") int mes,
                                                                                          @RequestParam(value = "ano") int ano,
                                                                                          @RequestParam(value = "tipoMovimento") String tipoMovimento,
                                                                                          @RequestParam(value = "somenteComIssDevido") boolean somenteComIssDevido,
                                                                                          @RequestParam(value = "page") int page,
                                                                                          @RequestParam(value = "size") int size,
                                                                                          @RequestHeader Long prestador) {
        List<NotaFiscalSearchDTO> notas = searchService.buscarNotasSemDeclararPorCompetencia(prestador, mes, ano,
                TipoMovimentoMensalNfseDTO.valueOf(tipoMovimento), somenteComIssDevido, page, size);
        return new ResponseEntity<>(notas, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/resumo-sem-declarar-por-competencia",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResumoDMSDTO> buscarResumoSemDeclararPorCompetencia(@RequestParam(value = "mes") int mes,
                                                                              @RequestParam(value = "ano") int ano,
                                                                              @RequestParam(value = "tipoMovimento") String tipoMovimento,
                                                                              @RequestParam(value = "somenteComIssDevido") boolean somenteComIssDevido,
                                                                              @RequestHeader Long prestador) {

        ResumoDMSDTO resumo = searchService.buscarResumoSemDeclararPorCompetencia(prestador, mes, ano,
                TipoMovimentoMensalNfseDTO.valueOf(tipoMovimento), somenteComIssDevido);


        return new ResponseEntity<>(resumo, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/enviar-nota-fiscal",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> enviarEmail(@RequestBody NotaFiscalNfseDTO notaFiscal) {
        log.debug("REST request to send mail NotaFiscal : {}", notaFiscal);
        service.enviarNotaPorEmail(notaFiscal.getEmails(), service.findById(notaFiscal.getId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/consultar-xml",
            method = RequestMethod.POST)
    @Timed
    public ResponseEntity<XmlNotaFiscalDTO> cosultarXml(@RequestBody BaixarXMLNfseDTO baixarXMLNfseDTO) {
        try {
            XmlNotaFiscalDTO xml = service.consultarXmlNotas(baixarXMLNfseDTO);
            return new ResponseEntity<>(xml, HttpStatus.OK);
        } catch (Exception e) {
            log.debug("Erro ao consultarXml na exportação de xml das notas fiscais.", e);
            log.error("Ocorreu um erro ao consultarXml na exportação de xml das notas fiscais. Ative o debug para visualização do erro.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/baixar-xml",
            method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> baixarXml(@RequestBody BaixarXMLNfseDTO baixarXMLNfseDTO) {
        service.preGerarXmlNotaFiscal(baixarXMLNfseDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/cache-geracao-xml",
            method = RequestMethod.GET)
    @Timed
    public ResponseEntity<HashSet<XmlNotaFiscalDTO>> cacheGeracaoXml() {
        return new ResponseEntity<>(service.getXmlNotaFiscalService().getGerandoXMl(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/remove-cache-geracao-xml",
            method = RequestMethod.DELETE)
    @Timed
    public ResponseEntity<HashSet<XmlNotaFiscalDTO>> removeGeracaoXml(@RequestParam String inscricaoCadastral) {
        XmlNotaFiscalDTO xmlNotaFiscalDTO = service.getXmlNotaFiscalService().get(inscricaoCadastral);
        service.getXmlNotaFiscalService().getGerandoXMl().remove(xmlNotaFiscalDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/baixar-xml-nota-fiscal",
            method = RequestMethod.GET)
    @Timed
    public ResponseEntity<ArquivoBase64DTO> baixarXml(@RequestParam Long idNotaFiscal) {
        try {
            ArquivoBase64DTO arquivoBase64DTO = service.gerarXmlNotaFiscal(idNotaFiscal);
            return new ResponseEntity<>(arquivoBase64DTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/baixar-xls",
            method = RequestMethod.GET)
    @Timed
    public ResponseEntity<ArquivoBase64DTO> baixarXls(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) {
        try {
            ArquivoBase64DTO arquivoBase64DTO = service.gerarXlsNotas(consultaGenerica);
            return new ResponseEntity<>(arquivoBase64DTO, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/parcelas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ResultadoParcela>> getParcelasDaNota(@PathVariable Long id) {
        log.debug("REST request to get getParcelasDaNota : {}", id);
        return new ResponseEntity<>(service.getParcelasDaNota(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/por-calculo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotaFiscalSearchDTO>> buscarNotaFiscalPorCalculo(@RequestParam Long calculoId) {
        return new ResponseEntity<>(searchService.findByCalculo(calculoId), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/relatorio-notas-fiscais", method = RequestMethod.POST)
    public void imprimirRelatorioNotasFiscaisEmitidas(HttpServletResponse response,
                                                      @RequestBody EmissaoRelatorioNotaFiscalDTO emissaoRelatorioNotaFiscal) {
        service.gerarRelatorioNotasFiscaisPDF(response, emissaoRelatorioNotaFiscal);
    }

    @RequestMapping(value = "/api/notaFiscals/relatorio-notas-fiscais-excel", method = RequestMethod.POST)
    public void gerarRelatorioNotasFiscaisExcel(HttpServletResponse response,
                                                @RequestHeader Long prestador,
                                                @RequestBody EmissaoRelatorioNotaFiscalDTO emissaoRelatorioNotaFiscal) {
        service.gerarRelatorioNotasFiscaisExcel(response, prestador, emissaoRelatorioNotaFiscal);
    }

    @RequestMapping(value = "/api/publico/nota-fiscal/remove-mongo/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void removerMongo(@PathVariable Long id) {
        service.removeOnMongo(id);
    }

    @GetMapping(value = "/api/notaFiscals/get")
    public ResponseEntity<NotaFiscalSearchDTO> get() {
        NotaFiscalSearchDTO dto = new NotaFiscalSearchDTO();
        dto.setEmissao(new Date());
        log.info("Data: {}", dto.getEmissao());
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/api/notaFiscals/post")
    public ResponseEntity<NotaFiscalSearchDTO> post(@RequestBody NotaFiscalSearchDTO dto) {
        log.info("Data: {}", dto.getEmissao());
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/api/notaFiscals/ultimo-numero-rps")
    public ResponseEntity<IntegerNfseDTO> getUltimoNumeroRps(@RequestHeader Long prestador) {
        Long ultimoNumeroRps = service.getUltimoNumeroRps(prestador);
        return new ResponseEntity(new LongNfseDTO(ultimoNumeroRps), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/notaFiscals/estatistica-mensal-notas-recebidas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EstatisticaMensalNfseDTO> getEstatiscas(@RequestParam("mes") Integer mes,
                                                                  @RequestParam("ano") Integer ano,
                                                                  @RequestParam("cpf") String cpf) {
        EstatisticaMensalNfseDTO dto = service
                .buscarEstatisticaMensalNotasRecebidas(mes, ano, cpf);
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(value = "/api/externo/placar",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlacarNfseDTO> buscarPlacar() {
        return ResponseEntity.ok().body(service.getPlacar());
    }

    @RequestMapping(value = "/api/notas-fiscais-lote-rps",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotaFiscalNfseDTO>> buscarPorIdLoteRPS(@RequestParam Long id) {
        return ResponseEntity.ok().body(service.findByLoteRps(id));
    }

    @RequestMapping(value = "/api/buscar-notas-fiscais-preenchidas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotaFiscalNfseDTO>> buscarNotasFiscaisPreenchidas(@RequestBody ConsultaGenericaNfseDTO consultaGenerica) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenerica.getOffset(), consultaGenerica.getLimit());
        Page<NotaFiscalNfseDTO> page = service.consultarPaginado(pageable,
                consultaGenerica.getParametrosQuery(), consultaGenerica.getOrderBy());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/substituicao-nota-fiscal",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotaFiscalNfseDTO> substituirNotaFiscal(@RequestBody SubstituicaoNotaFiscalNfseDTO substituicao) throws ValidacaoNfseWSException {
        return new ResponseEntity<>(service.substituirNotaFiscal(substituicao), HttpStatus.OK);
    }
}
