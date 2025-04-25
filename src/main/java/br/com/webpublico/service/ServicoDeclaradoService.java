package br.com.webpublico.service;

import br.com.webpublico.DateUtils;
import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.OrigemEmissaoNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoServicoDeclaradoNfseDTO;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import br.com.webpublico.exception.OperacaoNaoPermitidaException;
import br.com.webpublico.repository.ServicoDeclaradoJDBCRepository;
import br.com.webpublico.tributario.consultadebitos.ConsultaParcela;
import br.com.webpublico.tributario.consultadebitos.ResultadoParcela;
import br.com.webpublico.tributario.consultadebitos.services.ConsultaDebitosService;
import br.com.webpublico.util.GeradorExcelRelatorioDocumento;
import br.com.webpublico.web.rest.util.PaginationUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class ServicoDeclaradoService extends AbstractWPService<ServicoDeclaradoNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(ServicoDeclaradoService.class);

    @Autowired
    private CancelamentoService cancelamentoService;
    @Autowired
    private DeclaracaoPrestacaoServicoService declaracaoPrestacaoServicoService;
    @Autowired
    private ServicoDeclaradoJDBCRepository servicoDeclaradoJDBCRepository;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private CadastroEconomicoService cadastroEconomicoService;
    @Autowired
    private TipoDocumentoServicoDeclaradoService tipoDocumentoServicoDeclaradoService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ConfiguracaoService configuracaoService;
    private ConsultaDebitosService consultaDebitosService;

    @PostConstruct
    public void init() {
        if (consultaDebitosService == null) {
            consultaDebitosService = ConsultaDebitosService.buildFromJdbcTemplate(servicoDeclaradoJDBCRepository.getJdbcTemplate());
        }
    }

    public ServicoDeclaradoNfseDTO save(ServicoDeclaradoNfseDTO dto) throws Exception {
        tratarDatas(dto);
        validar(dto);
        preencherCamposAutomaticos(dto);
        declaracaoPrestacaoServicoService.inserir(dto);
        return servicoDeclaradoJDBCRepository.inserir(dto);
    }

    private void tratarDatas(ServicoDeclaradoNfseDTO dto) {
        dto.setEmissao(DateUtils.getData(dto.getEmissaoString(), "dd/MM/yyyy"));
    }


    public ServicoDeclaradoSearchNfseDTO buscarServicoDeclarado(Long prestador,
                                                                Integer ano,
                                                                TipoServicoDeclaradoNfseDTO tipo,
                                                                Long numero,
                                                                String cpfCnpj) throws Exception {
        List<ParametroQueryCampo> parametros = Lists.newArrayList();
        parametros.add(new ParametroQueryCampo("sd.cadastroeconomico_id", Operador.IGUAL, prestador));
        parametros.add(new ParametroQueryCampo("extract(year from sd.emissao)", Operador.IGUAL, ano));
        parametros.add(new ParametroQueryCampo("sd.numero", Operador.IGUAL, numero));
        parametros.add(new ParametroQueryCampo("dec.situacao", Operador.DIFERENTE, SituacaoDeclaracaoNfseDTO.CANCELADA.name()));
        if (TipoServicoDeclaradoNfseDTO.SERVICO_PRESTADO.equals(tipo)) {
            parametros.add(new ParametroQueryCampo("dpt.cpfcnpj", Operador.IGUAL, StringUtils.removerMascaraCpfCnpj(cpfCnpj)));
        } else {
            parametros.add(new ParametroQueryCampo("dpp.cpfcnpj", Operador.IGUAL, StringUtils.removerMascaraCpfCnpj(cpfCnpj)));
        }
        List<ServicoDeclaradoSearchNfseDTO> servicos = servicoDeclaradoJDBCRepository.consultarServicosDeclarados(null,
                Lists.newArrayList(new ParametroQuery(parametros)), null);
        return servicos.stream().findFirst().orElse(null);
    }

    private void validar(ServicoDeclaradoNfseDTO dto) throws Exception {
        OperacaoNaoPermitidaException onpe = new OperacaoNaoPermitidaException();
        if (dto.getNumero() == null)
            onpe.adicionarMensagem("O campo Número é obrigatório.");
        if (dto.getEmissao() == null)
            onpe.adicionarMensagem("O campo Emissão é obrigatório.");
        if (dto.getTipoDocumentoServicoDeclarado() == null)
            onpe.adicionarMensagem("O campo Tipo de Documento é obrigatório.");
        if (dto.getTipoServicoDeclarado() == null)
            onpe.adicionarMensagem("O Tipo de Serviço é obrigatório.");
        if (dto.getTipoServicoDeclarado() != null) {
            switch (dto.getTipoServicoDeclarado()) {
                case SERVICO_TOMADO: {
                    if (Strings.isNullOrEmpty(dto.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().getCpfCnpj())) {
                        onpe.adicionarMensagem("O campo CPF ou CNPJ do Prestador de Serviço é obrigatório.");
                    }
                    if (Strings.isNullOrEmpty(dto.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().getNomeRazaoSocial())) {
                        onpe.adicionarMensagem("O campo Nome ou Razão Social do Prestador de Serviço é obrigatório.");
                    }
                    break;
                }
                case SERVICO_PRESTADO: {
                    if (Strings.isNullOrEmpty(dto.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador().getCpfCnpj())) {
                        onpe.adicionarMensagem("O campo CPF ou CNPJ do Tomador de Serviço é obrigatório.");
                    }
                    if (Strings.isNullOrEmpty(dto.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador().getNomeRazaoSocial())) {
                        onpe.adicionarMensagem("O campo Nome ou Razão Social do Tomador de Serviço é obrigatório.");
                    }
                    break;
                }
            }
        }
        if (CollectionUtils.isEmpty(dto.getDeclaracaoPrestacaoServico().getItens())) {
            onpe.adicionarMensagem("É necessário adicionar ao menos um serviço.");
        }
        if (onpe.getMensagens().isEmpty()) {
            String cpfCnpj = TipoServicoDeclaradoNfseDTO.SERVICO_PRESTADO.equals(dto.getTipoServicoDeclarado()) ?
                    dto.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador().getCpfCnpj() :
                    dto.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().getCpfCnpj();
            String nomeRazaoSocial = TipoServicoDeclaradoNfseDTO.SERVICO_PRESTADO.equals(dto.getTipoServicoDeclarado()) ?
                    dto.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador().getNomeRazaoSocial() :
                    dto.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().getNomeRazaoSocial();
            String responsavel = TipoServicoDeclaradoNfseDTO.SERVICO_PRESTADO.equals(dto.getTipoServicoDeclarado()) ?
                    "tomador" :
                    "prestador";
            ServicoDeclaradoSearchNfseDTO servicoDeclarado = buscarServicoDeclarado(dto.getCadastroEconomico().getId(),
                    DateUtils.getAno(dto.getEmissao()),
                    dto.getTipoServicoDeclarado(),
                    dto.getNumero(), cpfCnpj);
            if (servicoDeclarado != null) {
                onpe.adicionarMensagem("O serviço declarado de número " + dto.getNumero() + " para o ano " + DateUtils.getAno(dto.getEmissao())
                        + " e " + responsavel + " " + cpfCnpj + " - " + nomeRazaoSocial + " já está registrado.");
            }
        }
        onpe.lancarExcecao();
    }

    private void preencherCamposAutomaticos(ServicoDeclaradoNfseDTO dto) {
        dto.getDeclaracaoPrestacaoServico().setCompetencia(dto.getEmissao());
    }

    public ServicoDeclaradoNfseDTO cancelar(CancelamentoNfseDTO cancelamento) {
        cancelamento.setTipoDocumento(TipoDocumentoCancelamentoNfseDTO.SERVICO_DECLARADO);
        cancelamento = cancelamentoService.salvarCancelamento(cancelamento,
                cancelamento.getServicoDeclarado().getDeclaracaoPrestacaoServico(),
                cancelamento.getServicoDeclarado().getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador(),
                cancelamento.getServicoDeclarado().getDeclaracaoPrestacaoServico().getSituacao());
        if (cancelamento != null) {
            cancelamento.getServicoDeclarado().getDeclaracaoPrestacaoServico().setCancelamento(cancelamento);
            declaracaoPrestacaoServicoService.atribuirCancelamento(cancelamento.getServicoDeclarado().getDeclaracaoPrestacaoServico());
        }
        return findById(cancelamento.getServicoDeclarado().getId());
    }

    public ServicoDeclaradoNfseDTO findById(Long id) {
        ServicoDeclaradoNfseDTO dto = servicoDeclaradoJDBCRepository.findById(id);
        preencher(dto);
        return dto;
    }

    private void preencher(ServicoDeclaradoNfseDTO dto) {
        dto.setCadastroEconomico(cadastroEconomicoService.findById(dto.getCadastroEconomico().getId()));
        dto.setTipoDocumentoServicoDeclarado(tipoDocumentoServicoDeclaradoService.findById(dto.getTipoDocumentoServicoDeclarado().getId()));
        dto.setDeclaracaoPrestacaoServico(declaracaoPrestacaoServicoService.findById(dto.getDeclaracaoPrestacaoServico().getId()));
    }


    public void delete(Long id) {
        try {
            ServicoDeclaradoNfseDTO servicoDeclarado = findById(id);
            servicoDeclaradoJDBCRepository.delete(servicoDeclarado);
            declaracaoPrestacaoServicoService.delete(servicoDeclarado.getDeclaracaoPrestacaoServico());
        } catch (Exception e) {
            throw new NfseOperacaoNaoPermitidaException("O registro possui depêndencias e não pode ser excluído.");
        }
    }

    public ServicoDeclaradoNfseDTO novo(Long empresaId, String tipoServicoDeclarado) {
        ServicoDeclaradoNfseDTO dto = new ServicoDeclaradoNfseDTO();
        dto.setEmissaoString(DateUtils.getDataFormatada(new Date(), "dd/MM/yyyy"));
        dto.setCadastroEconomico(cadastroEconomicoService.findById(empresaId));
        dto.setTipoServicoDeclarado(TipoServicoDeclaradoNfseDTO.valueOf(tipoServicoDeclarado));
        dto.setDeclaracaoPrestacaoServico(new DeclaracaoPrestacaoServicoNfseDTO());
        dto.getDeclaracaoPrestacaoServico().setCompetencia(new Date());
        dto.getDeclaracaoPrestacaoServico().setSituacao(SituacaoDeclaracaoNfseDTO.EMITIDA);
        dto.getDeclaracaoPrestacaoServico().setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.IDENTIFICADO);
        dto.getDeclaracaoPrestacaoServico().setOrigemEmissao(OrigemEmissaoNfseDTO.WEB);
        if (TipoServicoDeclaradoNfseDTO.SERVICO_TOMADO.equals(dto.getTipoServicoDeclarado())) {
            dto.getDeclaracaoPrestacaoServico().setDadosPessoaisTomador(dto.getCadastroEconomico().getPessoa().getDadosPessoais());
            dto.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador().setId(null);
            dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.RETENCAO);
        } else {
            dto.getDeclaracaoPrestacaoServico().setDadosPessoaisPrestador(dto.getCadastroEconomico().getPessoa().getDadosPessoais());
            dto.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().setId(null);
            dto.getDeclaracaoPrestacaoServico().setNaturezaOperacao(ExigibilidadeNfseDTO.TRIBUTACAO_MUNICIPAL);
        }
        dto.getDeclaracaoPrestacaoServico().setIssRetido(true);
        dto.getDeclaracaoPrestacaoServico().setTotalServicos(BigDecimal.ZERO);
        dto.getDeclaracaoPrestacaoServico().setDeducoesLegais(BigDecimal.ZERO);
        dto.getDeclaracaoPrestacaoServico().setBaseCalculo(BigDecimal.ZERO);
        dto.getDeclaracaoPrestacaoServico().setIssCalculado(BigDecimal.ZERO);
        dto.getDeclaracaoPrestacaoServico().setItens(new ArrayList());
        return dto;
    }

    public List<ServicoDeclaradoNfseDTO> findByEmpresaCompetenciaSemDeclarar(Long empresaId, int mes, int ano) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/servico-declarado/por-competencia-prestador";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("empresaId", empresaId)
                .queryParam("mes", mes)
                .queryParam("ano", ano)
                .toUriString();
        ResponseEntity<List<ServicoDeclaradoNfseDTO>> exchange = restTemplate.exchange(url,
                HttpMethod.GET, null, getResponseTypeList());
        return exchange.getBody();
    }


    public List<ServicoDeclaradoNfseDTO> findByEmpresaDeclaracao(Long empresaId, Long declaracaoId) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/declaracao-servico-tomado-por-declaracao-prestador";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("empresaId", empresaId)
                .queryParam("declaracaoId", declaracaoId)
                .toUriString();
        ResponseEntity<List<ServicoDeclaradoNfseDTO>> exchange = restTemplate.exchange(url,
                HttpMethod.GET, null, getResponseTypeList());
        return exchange.getBody();
    }


    public List<ServicoDeclaradoNfseDTO> findAllNaoDeclaradas(Long empresaId) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/servico-declarado/nao-declarada";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("empresaId", empresaId)
                .toUriString();
        ResponseEntity<List<ServicoDeclaradoNfseDTO>> exchange = restTemplate.exchange(url,
                HttpMethod.GET, null, getResponseTypeList());
        return exchange.getBody();
    }

    @Override
    public String getTableName() {
        return "ServicoDeclarado";
    }

    @Override
    public String getDefaltSearchFields() {
        return "numero";
    }

    @Override
    public ParameterizedTypeReference<List<ServicoDeclaradoNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<ServicoDeclaradoNfseDTO>>() {
        };
    }

    public ParameterizedTypeReference<List<NotaFiscalSearchDTO>> getResponseNfseTypeList() {
        return new ParameterizedTypeReference<List<NotaFiscalSearchDTO>>() {
        };
    }


    @Override
    public ParameterizedTypeReference<ServicoDeclaradoNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<ServicoDeclaradoNfseDTO>() {
        };
    }

    public ParameterizedTypeReference<List<LoteImportacaoDocumentoRecebidoNfseDTO>> getResponseTypeLoteList() {
        return new ParameterizedTypeReference<List<LoteImportacaoDocumentoRecebidoNfseDTO>>() {
        };
    }

    public ParameterizedTypeReference<LoteImportacaoDocumentoRecebidoNfseDTO> getResponseTypeLote() {
        return new ParameterizedTypeReference<LoteImportacaoDocumentoRecebidoNfseDTO>() {
        };
    }


    public ResponseEntity<LoteImportacaoDocumentoRecebidoNfseDTO> importar(LoteImportacaoDocumentoRecebidoNfseDTO lote) {
        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/servico-declarado/importar-xml").toUriString();
        return restTemplate.postForEntity(url, lote, LoteImportacaoDocumentoRecebidoNfseDTO.class);
    }

    public ResponseEntity<List<LoteImportacaoDocumentoRecebidoNfseDTO>> buscarLotePorPrestadorServico(Long empresaId, String filtro, Pageable pageable) {
        if (pageable == null) {
            pageable = PaginationUtil.generatePageRequest(0, 100);
        }
        String url = urlsProperties.getWebpublicoPathNfse() + "/servico-declarado/lote-por-empresa";
        url = UriComponentsBuilder.fromUriString(url).queryParam("empresaId", empresaId).queryParam("filtro", filtro).toUriString();
        UriComponentsBuilder uriComponentsBuilder = PaginationUtil.addPageableParam(url, pageable);
        return restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, null, getResponseTypeLoteList());
    }

    public ResponseEntity<LoteImportacaoDocumentoRecebidoNfseDTO> findOneLote(Long id) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/servico-declarado/lote/" + id;
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeLote());
    }

    private List<ServicoDeclaradoNfseDTO> buscarDeclaracoesServicoTomado(List<ParametroQuery> parametros, int pagina) {
        HttpEntity<List<ParametroQuery>> requestEntity = new HttpEntity(parametros, getHeadersDefault());

        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/servico-declarado/consultar")
                .queryParam("pagina", pagina).toUriString();

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, getResponseTypeList()).getBody();
    }

    public HttpHeaders getHeadersDefault() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return requestHeaders;
    }

    public Page<ServicoDeclaradoSearchNfseDTO> buscarServicosDeclarados(Pageable pageable, List<ParametroQuery> parametros, String orderBy) throws Exception {
        List<ServicoDeclaradoSearchNfseDTO> servicoDeclaradoSearchNfseDTOS = servicoDeclaradoJDBCRepository.consultarServicosDeclarados(pageable,
                parametros, orderBy);
        Integer count = servicoDeclaradoJDBCRepository.contarServicos(parametros);

        return new PageImpl<>(servicoDeclaradoSearchNfseDTOS, pageable, count);
    }

    public byte[] gerarBytesRelatorioServicosDeclarados(List<ServicoDeclaradoSearchNfseDTO> servicosDeclarados,
                                                        String filtros,
                                                        boolean excel) {
        byte[] bytes = new byte[0];
        try {
            String jrxml = "RelatorioServicosDeclarados.jrxml";
            HashMap<String, Object> parametros = new HashMap<>();
            reportService.parametrosDefault(parametros);
            parametros.put("FILTROS", filtros);
            return reportService.gerarRelatorio(jrxml, parametros, servicosDeclarados, excel);
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de servicos declarados {}", e);
        }
        return bytes;
    }

    public void gerarRelatorioServicosDeclarados(HttpServletResponse response, EmissaoRelatorioServicoDeclaradoDTO emissaoRelatorioServicoDeclarado) {
        try {
            List<ServicoDeclaradoSearchNfseDTO> servicosDeclarados = servicoDeclaradoJDBCRepository.consultarServicosDeclarados(null,
                    emissaoRelatorioServicoDeclarado.getConsultaServicoDeclarado().getParametrosQuery(),
                    emissaoRelatorioServicoDeclarado.getConsultaServicoDeclarado().getOrderBy());
            byte[] bytes = gerarBytesRelatorioServicosDeclarados(servicosDeclarados, emissaoRelatorioServicoDeclarado.getCriteriosUtilizados(),
                    false);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=RelatorioServicosDeclarados.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de servicos declarados {} ", e);
        }
    }

    public byte[] gerarBytesRelatorioServicosDeclaradosExcel(PrestadorServicoNfseDTO prestador,
                                                             EmissaoRelatorioServicoDeclaradoDTO emissao) throws Exception {
        String nomePlanilha = "ServicosDeclarados";
        String titulo = "Relatório de Serviços Declarados";
        String contribuinte = prestador.getPessoa().getDadosPessoais().getCpfCnpj() + " - " +
                prestador.getPessoa().getDadosPessoais().getNomeRazaoSocial();
        List<RelatorioDocumentoDTO> documentos = Lists.newArrayList();
        List<ServicoDeclaradoSearchNfseDTO> servicosDeclarados = servicoDeclaradoJDBCRepository.consultarServicosDeclarados(null,
                emissao.getConsultaServicoDeclarado().getParametrosQuery(),
                emissao.getConsultaServicoDeclarado().getOrderBy());
        for (ServicoDeclaradoSearchNfseDTO servicoDeclarado : servicosDeclarados) {
            String cpfCnpj = TipoServicoDeclaradoNfseDTO.SERVICO_TOMADO.name().equals(servicoDeclarado.getTipoServicoDeclarado()) ?
                    servicoDeclarado.getPrestadorCpfCnpj() : servicoDeclarado.getTomadorCpfCnpj();
            String nomeRazaoSocial = TipoServicoDeclaradoNfseDTO.SERVICO_TOMADO.name().equals(servicoDeclarado.getTipoServicoDeclarado()) ?
                    servicoDeclarado.getPrestadorNomeRazaoSocial() : servicoDeclarado.getTomadorNomeRazaoSocial();
            documentos.add(new RelatorioDocumentoDTO(servicoDeclarado.getNumero(), cpfCnpj, nomeRazaoSocial,
                    servicoDeclarado.getEmissao(),
                    SituacaoDeclaracaoNfseDTO.valueOf(servicoDeclarado.getSituacao()).getDescricao(),
                    ExigibilidadeNfseDTO.valueOf(servicoDeclarado.getNaturezaOperacao()).getDescricao(),
                    servicoDeclarado.getTotalServicos(), servicoDeclarado.getIssCalculado()));
        }
        ConfiguracaoNfseDTO configuracao = configuracaoService.getConfiguracaoFromServer();
        GeradorExcelRelatorioDocumento geradorExcel = new GeradorExcelRelatorioDocumento(configuracao.getLogoMunicipio(),
                nomePlanilha, titulo, contribuinte, emissao.getCriteriosUtilizados(),
                documentos);
        File file = geradorExcel.gerar();
        return FileUtils.readFileToByteArray(file);
    }

    public void gerarRelatorioServicosDeclaradosExcel(HttpServletResponse response,
                                                      Long prestador,
                                                      EmissaoRelatorioServicoDeclaradoDTO emissao) {
        try {
            PrestadorServicoNfseDTO prestadorServicoNfseDTO = cadastroEconomicoService.findById(prestador);
            byte[] bytes = gerarBytesRelatorioServicosDeclaradosExcel(prestadorServicoNfseDTO, emissao);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de serviços declarados em excel {} ", e);
        }
    }

    public void imprime(HttpServletResponse response, Long id) throws JRException, IOException {
        ConfiguracaoNfseDTO configuracaoFromServer = configuracaoService.getConfiguracaoFromServer();
        ImpressaoServicoDeclaradoNfseDTO impressao = servicoDeclaradoJDBCRepository.buscarDadosImpressaoServicoDeclarado(id);
        impressao.setHomologacao(!configuracaoFromServer.getProducao());
        HashMap parametros = new HashMap();
        reportService.parametrosDefault(parametros);
        byte[] bytes = reportService.gerarRelatorioPdf("ServicoDeclarado.jrxml", parametros,
                Lists.newArrayList(impressao));
        reportService.imprimirRelatorio(response, bytes, "servicoDeclarado.pdf");
    }

    public List<ResultadoParcela> buscarParcelasServicoDeclarado(Long idServicoDeclarado) {
        ServicoDeclaradoNfseDTO servico = findById(idServicoDeclarado);
        List<Long> idCalculo = servicoDeclaradoJDBCRepository.buscarIdCalculoServicoDeclarado(servico);
        if (idCalculo != null && !idCalculo.isEmpty()) {
            List<ResultadoParcela> resultados = new ConsultaParcela(consultaDebitosService)
                    .addParameter(ConsultaParcela.Campo.CALCULO_ID, ConsultaParcela.Operador.IN, idCalculo)
                    .executaConsulta().getResultados();
            return resultados;
        }
        return Lists.newArrayList();
    }
}

