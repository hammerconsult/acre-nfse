package br.com.webpublico.service;

import br.com.webpublico.BarCode;
import br.com.webpublico.BigDecimalUtils;
import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.email.AnexoDTO;
import br.com.webpublico.domain.dto.enums.*;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.domain.dto.exception.TipoValidacao;
import br.com.webpublico.domain.dto.exception.ValidacaoNfseWSException;
import br.com.webpublico.domain.dto.nfse10.TcCancelamentoNfse;
import br.com.webpublico.domain.dto.nfse10.TcConfirmacaoCancelamento;
import br.com.webpublico.domain.dto.nfse10.TcInfPedidoCancelamento;
import br.com.webpublico.domain.dto.nfse10.TcPedidoCancelamento;
import br.com.webpublico.domain.dto.nfse203.SubstituirNfseResposta;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import br.com.webpublico.domain.dto.template.TrocaTagEmailNotaFiscal;
import br.com.webpublico.domain.dto.util.LoteRpsBuild;
import br.com.webpublico.domain.dto.util.NfseXMLV1;
import br.com.webpublico.domain.dto.xmlnota.CompNfse;
import br.com.webpublico.domain.dto.xmlnota.ConsultarNfseLote;
import br.com.webpublico.domain.events.NotaFiscalCreatedEvent;
import br.com.webpublico.repository.*;
import br.com.webpublico.repository.mongo.*;
import br.com.webpublico.tributario.consultadebitos.ConsultaParcela;
import br.com.webpublico.tributario.consultadebitos.ResultadoParcela;
import br.com.webpublico.tributario.consultadebitos.enums.SituacaoParcelaDTO;
import br.com.webpublico.tributario.consultadebitos.enums.TipoCalculoDTO;
import br.com.webpublico.tributario.consultadebitos.services.ConsultaDebitosService;
import br.com.webpublico.util.GeradorExcelRelatorioDocumento;
import br.com.webpublico.util.Util;
import br.com.webpublico.web.rest.util.PaginationUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.mifmif.common.regex.Generex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.LocalDate;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
public class NotaFiscalService extends AbstractService<NotaFiscalNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(NotaFiscalService.class);

    @Autowired
    private NotaFiscalJDBCRepository repository;
    @Autowired
    private NotaFiscalConsultaJDBCRepository repositorySearch;
    @Autowired
    private DeclaracaoPrestacaoServicoService declaracaoPrestacaoServicoService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private CadastroEconomicoService cadastroEconomicoService;
    @Autowired
    private TomadorService tomadorService;
    @Autowired
    private NotaFiscalMongoRepository mongoRepository;
    @Autowired
    private NotaFiscalSearchMongoRepository notaFiscalSearchMongoRepository;
    @Autowired
    private ImagemService imagemService;
    @Autowired
    private ImpressaoNotaFiscalMongoRepository impressaoNotaFiscalMongoRepository;
    @Autowired
    private ArquivoBase64MongoRepository arquivoBase64MongoRepository;
    @Autowired
    private TemplateJDBCRepository templateJDBCRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private XmlBase64MongoRepository xmlBase64MongoRepository;
    @Autowired
    private CartaCorrecaoJDBCRepository cartaCorrecaoJDBCRepository;
    @Autowired
    private RpsJDBCRepository rpsJDBCRepository;
    @Autowired
    private ReportService reportService;
    @Autowired
    private CancelamentoService cancelamentoService;
    @Autowired
    private LogGeralNfseJDBCRepository logGeralNfseJDBCRepository;
    @Autowired
    private XmlNotaFiscalService xmlNotaFiscalService;
    @Autowired
    private CidadeService cidadeService;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private DadosPessoaisService dadosPessoaisService;
    @Autowired
    private TributosFederaisService tributosFederaisService;
    @Autowired
    private ConstrucaoCivilService construcaoCivilService;
    @Autowired
    private FeriadoService feriadoService;
    @Autowired
    private CacheSistemaService cacheSistemaService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ServicoService servicoService;

    private Map<TipoTemplateNfse, TemplateNfseDTO> templates;
    private ConsultaDebitosService consultaDebitosService;
    private Integer diaVencimentoIss;
    private PlacarNfseDTO placar = new PlacarNfseDTO();

    public UserService getUserService() {
        return userService;
    }

    public CadastroEconomicoService getCadastroEconomicoService() {
        return cadastroEconomicoService;
    }

    public CidadeService getCidadeService() {
        return cidadeService;
    }

    public ConfiguracaoService getConfiguracaoService() {
        return configuracaoService;
    }

    @PostConstruct
    public void init() {
        if (consultaDebitosService == null) {
            consultaDebitosService = ConsultaDebitosService.buildFromJdbcTemplate(repository.getJdbcTemplate());
        }
    }

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public XmlNotaFiscalService getXmlNotaFiscalService() {
        return xmlNotaFiscalService;
    }

    public Integer getVencimentoIss() {
        if (diaVencimentoIss == null) {
            diaVencimentoIss = repository.getDiaVencimentoIss();
        }
        return diaVencimentoIss;
    }

    private void validarCriacaoNotaFiscal(NotaFiscalNfseDTO dto) {
        ConfiguracaoNfseDTO configuracaoNfseDTO = configuracaoService.getConfiguracaoFromServer();
        if (dto.getRps() != null && hasNotaParaRps(dto.getRps().getNumero(), dto.getPrestador().getId())) {
            throw new NfseOperacaoNaoPermitidaException("O RPS informado está vinculado a outra nota fiscal já emitida");
        }
        Date hoje = new Date();
        if (dto.getEmissao() == null) {
            throw new NfseOperacaoNaoPermitidaException("A Data de Emissão da Nota Fiscal é obrigatória");
        } else {
            if (DateUtils.dataSemHorario(dto.getEmissao()).after(DateUtils.dataSemHorario(hoje))) {
                throw new NfseOperacaoNaoPermitidaException("A Data de Emissão não pode ser superior a data de hoje " +
                        DateUtils.getDataFormatada(hoje));
            }
            if (DateUtils.getAno(dto.getEmissao()).compareTo(DateUtils.getAno(hoje) - 5) < 0) {
                throw new NfseOperacaoNaoPermitidaException("A Data de Emissão não pode ser anterior a 5 anos.");
            }
            if (!configuracaoNfseDTO.getPermiteEmissaoRetroativaUltimaEmitida() &&
                    dto.getUltimaEmissao() != null && DateUtils.dataSemHorario(dto.getEmissao()).before(
                    DateUtils.dataSemHorario(dto.getUltimaEmissao()))) {
                throw new NfseOperacaoNaoPermitidaException("A Data de Emissão não pode ser inferior a última data de emissão " +
                        DateUtils.getDataFormatada(dto.getUltimaEmissao()));
            }
        }
        if (dto.getRps() != null) {
            Date competencia = DateUtils.getData(
                    DateUtils.getDataFormatada(dto.getDeclaracaoPrestacaoServico().getCompetencia(), "MM/yyyy"),
                    "MM/yyyy");
            Date competenciaLimite = DateUtils.adicionarMeses(DateUtils.getData(DateUtils
                    .getDataFormatada(hoje, "MM/yyyy"), "MM/yyyy"), -13);
            if (DateUtils.dataSemHorario(competencia).before(DateUtils.dataSemHorario(competenciaLimite))) {
                throw new NfseOperacaoNaoPermitidaException("A Competência da nota fiscal não pode ser inferior a " +
                        DateUtils.getDataFormatada(competenciaLimite, "MM/yyyy") + ". ");
            }
        }
        if (dto.getBaseCalculo().compareTo(BigDecimal.ZERO) < 0) {
            throw new NfseOperacaoNaoPermitidaException("A Base de Cálculo não pode ser negativa.");
        }
        if (dto.getValorLiquido().compareTo(BigDecimal.ZERO) < 0) {
            throw new NfseOperacaoNaoPermitidaException("O Valor Líquido da Nota não pode ser negativo.");
        }
        if (dto.getDeclaracaoPrestacaoServico().getItens() != null &&
                !dto.getDeclaracaoPrestacaoServico().getItens().isEmpty()) {
            for (ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico : dto.getDeclaracaoPrestacaoServico().getItens()) {
                validarItemDeclaracaoServico(itemDeclaracaoServico);
            }
        }
    }

    private void validarItemDeclaracaoServico(ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico) {
        NfseOperacaoNaoPermitidaException e = new NfseOperacaoNaoPermitidaException();
        if (itemDeclaracaoServico.getValorServico() == null ||
                itemDeclaracaoServico.getValorServico().compareTo(BigDecimal.ZERO) < 0) {
            e.adicionarMensagem("O Valor do Serviço não pode ser inferior a 0 (zero).");
        }
        if (itemDeclaracaoServico.getQuantidade() == null ||
                itemDeclaracaoServico.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            e.adicionarMensagem("A Quantidede não pode ser inferior ou igual a 0 (zero).");
        }
        if (itemDeclaracaoServico.getDetalhes() != null &&
                !itemDeclaracaoServico.getDetalhes().isEmpty()) {
            for (DetalheItemDeclaracaoServicoNfseDTO detalhe : itemDeclaracaoServico.getDetalhes()) {
                if (detalhe.getQuantidade() == null ||
                        detalhe.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                    e.adicionarMensagem("A Quantidade do item " + detalhe.getDescricao() + " não pode ser inferior ou igual a 0 (zero).");
                }
                if (detalhe.getValorServico() == null ||
                        detalhe.getValorServico().compareTo(BigDecimal.ZERO) < 0) {
                    e.adicionarMensagem("O Valor do item " + detalhe.getDescricao() + " não pode ser inferior a 0 (zero).");
                }
            }
        }
        e.lancarException();
    }

    private boolean hasNotaParaRps(String numero, Long idPrestador) {
        return repository.hasNotaParaRps(numero, idPrestador);
    }

    @Transactional
    @Override
    public NotaFiscalNfseDTO preSave(NotaFiscalNfseDTO dto) {
        recuperarDados(dto);
        CacheSistemaNfseDTO cacheNotaFiscal = new CacheSistemaNfseDTO(TipoCacheSistemaNfseDTO.NOTA_FISCAL,
                "Prestador: " + dto.getPrestador().getInscricaoMunicipal());
        cacheSistemaService.aguardar(cacheNotaFiscal);
        try {
            cacheSistemaService.addCache(cacheNotaFiscal);
            calcularValores(dto);
            preencherCamposAutomaticos(dto);
            validarCriacaoNotaFiscal(dto);
            arredondarValores(dto);
            inserirRpsManual(dto);
            declaracaoPrestacaoServicoService.inserir(dto);
            return dto;
        } finally {
            cacheSistemaService.removeCache(cacheNotaFiscal);
        }
    }

    private void recuperarDados(NotaFiscalNfseDTO dto) {
        if (dto.getPrestador() != null && dto.getPrestador().getId() != null) {
            dto.setPrestador(cadastroEconomicoService.findById(dto.getPrestador().getId()));
        }
        if (dto.getRps() != null && dto.getRps().getId() != null) {
            dto.setRps(rpsJDBCRepository.findById(dto.getRps().getId()));
        }
        for (ItemDeclaracaoServicoNfseDTO item : dto.getDeclaracaoPrestacaoServico().getItens()) {
            item.setMunicipio(item.getMunicipio() != null ? cidadeService.findById(item.getMunicipio().getId()) : null);
            item.setServico(item.getServico() != null ? servicoService.findById(item.getServico().getId()) : null);
        }
    }

    @Transactional
    @Override
    public NotaFiscalNfseDTO posSave(NotaFiscalNfseDTO dto) {
        applicationEventPublisher.publishEvent(new NotaFiscalCreatedEvent(dto));
        return super.posSave(dto);
    }

    private void preencherCamposAutomaticos(NotaFiscalNfseDTO notaFiscal) {
        notaFiscal.setEmissao(new Date());
        if (notaFiscal.getDeclaracaoPrestacaoServico().getCompetencia() == null) {
            notaFiscal.getDeclaracaoPrestacaoServico().setCompetencia(notaFiscal.getEmissao());
        }
        ConfiguracaoNfseDTO configuracaoNfse = configuracaoService.getConfiguracaoFromServer();
        notaFiscal.setInformacoesAdicionais(notaFiscal.gerarInformacoesAdicionais(configuracaoNfse.getMunicipio(),
                buscarVencimentoIss(notaFiscal)));

        if (notaFiscal.getDeclaracaoPrestacaoServico().getOrigemEmissao() == null) {
            notaFiscal.getDeclaracaoPrestacaoServico().setOrigemEmissao(notaFiscal.getRps() != null ? OrigemEmissaoNfseDTO.RPS : OrigemEmissaoNfseDTO.WEB);
        }

    }

    private void inserirRpsManual(NotaFiscalNfseDTO notaFiscal) {
        if (notaFiscal.getRps() == null || notaFiscal.getRps().getId() != null)
            return;
        RpsNfseDTO rps = notaFiscal.getRps();
        rps.setPrestador(notaFiscal.getPrestador());
        rps.setIssCalculado(notaFiscal.getIssCalculado());
        rps.setIssPagar(notaFiscal.getIssPagar());
        rps.setOptanteSimplesNacional(notaFiscal.getDeclaracaoPrestacaoServico().getOptanteSimplesNacional());
        rps.setNaturezaOperacao(notaFiscal.getDeclaracaoPrestacaoServico().getNaturezaOperacao());
        rps.setTotalNota(notaFiscal.getTotalNota());
        rps.setValorLiquido(notaFiscal.getValorLiquido());
        rps.setDescontosCondicionais(notaFiscal.getDescontosCondicionais());
        rps.setDescontosIncondicionais(notaFiscal.getDescontosIncondicionais());
        rps.setDescriminacaoServico(notaFiscal.getDescriminacaoServico());
        rps.setCompetencia(notaFiscal.getDeclaracaoPrestacaoServico().getCompetencia());
        try {
            rps.setDadosPessoaisPrestador(notaFiscal.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().clone());
        } catch (CloneNotSupportedException e) {
        }
        try {
            if (notaFiscal.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador() != null)
                rps.setDadosPessoaisTomador(notaFiscal.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador().clone());
        } catch (CloneNotSupportedException e) {
        }
        rps.setDeducoesLegais(notaFiscal.getDeducoesLegais());
        rps.setTotalServicos(notaFiscal.getTotalServicos());
        rps.setRetencoesFederais(notaFiscal.getRetencoesFederais());
        rps.setIssRetido(notaFiscal.getDeclaracaoPrestacaoServico().getIssRetido());
        rps.setTributosFederais(new TributosFederaisNfseDTO(notaFiscal.getDeclaracaoPrestacaoServico()));
        rps.setBaseCalculo(notaFiscal.getBaseCalculo());
        rps.setModalidadeEmissao(notaFiscal.getModalidade());
        rps.setItens(new ArrayList<>());
        for (ItemDeclaracaoServicoNfseDTO itemDeclaracaoServicoNfseDTO : notaFiscal.getDeclaracaoPrestacaoServico().getItens()) {
            rps.getItens().add(ItemDeclaracaoServicoNfseDTO.copy(itemDeclaracaoServicoNfseDTO, notaFiscal.getDeclaracaoPrestacaoServico()));
        }
        if (rps.getConstrucaoCivil() != null) {
            construcaoCivilService.inserir(rps.getConstrucaoCivil());
        }
        if (rps.getTributosFederais() != null) {
            tributosFederaisService.inserir(rps.getTributosFederais());
        }
        if (rps.getDadosPessoaisPrestador() != null) {
            dadosPessoaisService.inserir(rps.getDadosPessoaisPrestador());
        }
        if (rps.getDadosPessoaisTomador() != null) {
            dadosPessoaisService.inserir(rps.getDadosPessoaisTomador());
        }
        rpsJDBCRepository.inserir(rps);
    }

    private void arredondarValores(NotaFiscalNfseDTO notaFiscal) {
        notaFiscal.setTotalServicos(BigDecimalUtils.arredondar(notaFiscal.getTotalServicos(), 2));
        notaFiscal.setDeducoesLegais(BigDecimalUtils.arredondar(notaFiscal.getDeducoesLegais(), 2));
        notaFiscal.setDescontosCondicionais(BigDecimalUtils.arredondar(notaFiscal.getDescontosCondicionais(), 2));
        notaFiscal.setDescontosIncondicionais(BigDecimalUtils.arredondar(notaFiscal.getDescontosIncondicionais(), 2));
        notaFiscal.setValorLiquido(BigDecimalUtils.arredondar(notaFiscal.getValorLiquido(), 2));
        notaFiscal.setBaseCalculo(BigDecimalUtils.arredondar(notaFiscal.getBaseCalculo(), 2));
        notaFiscal.setIssPagar(BigDecimalUtils.arredondar(notaFiscal.getIssPagar(), 2));
        notaFiscal.setIssCalculado(BigDecimalUtils.arredondar(notaFiscal.getIssCalculado(), 2));
        notaFiscal.setAliquotaServico(BigDecimalUtils.arredondar(notaFiscal.getAliquotaServico(), 2));
        TributosFederaisNfseDTO tributosFederais = notaFiscal.getDeclaracaoPrestacaoServico().getTributosFederais();
        if (tributosFederais != null) {
            tributosFederais.setCofins(BigDecimalUtils.arredondar(tributosFederais.getCofins(), 2));
            tributosFederais.setCsll(BigDecimalUtils.arredondar(tributosFederais.getCsll(), 2));
            tributosFederais.setInss(BigDecimalUtils.arredondar(tributosFederais.getInss(), 2));
            tributosFederais.setIrrf(BigDecimalUtils.arredondar(tributosFederais.getIrrf(), 2));
            tributosFederais.setOutrasRetencoes(BigDecimalUtils.arredondar(tributosFederais.getOutrasRetencoes(), 2));
            tributosFederais.setPis(BigDecimalUtils.arredondar(tributosFederais.getPis(), 2));
            tributosFederais.setCpp(BigDecimalUtils.arredondar(tributosFederais.getCpp(), 2));
        }
        for (ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico : notaFiscal.getDeclaracaoPrestacaoServico().getItens()) {
            itemDeclaracaoServico.setAliquotaServico(BigDecimalUtils.arredondar(itemDeclaracaoServico.getAliquotaServico(), 2));
            itemDeclaracaoServico.setIss(BigDecimalUtils.arredondar(itemDeclaracaoServico.getIss(), 2));
            itemDeclaracaoServico.setBaseCalculo(BigDecimalUtils.arredondar(itemDeclaracaoServico.getBaseCalculo(), 2));
            itemDeclaracaoServico.setValorServico(BigDecimalUtils.arredondar(itemDeclaracaoServico.getValorServico(), 2));
            itemDeclaracaoServico.setDescontosCondicionados(BigDecimalUtils.arredondar(itemDeclaracaoServico.getDescontosCondicionados(), 2));
            itemDeclaracaoServico.setDescontosIncondicionados(BigDecimalUtils.arredondar(itemDeclaracaoServico.getDescontosIncondicionados(), 2));
            itemDeclaracaoServico.setDeducoes(BigDecimalUtils.arredondar(itemDeclaracaoServico.getDeducoes(), 2));
            itemDeclaracaoServico.setQuantidade(BigDecimalUtils.arredondar(itemDeclaracaoServico.getQuantidade(), 2));
            itemDeclaracaoServico.setValorServico(BigDecimalUtils.arredondar(itemDeclaracaoServico.getValorServico(), 2));
        }
    }

    public Date buscarVencimentoIss(NotaFiscalNfseDTO notaFiscal) {
        LocalDate localDate = LocalDate
                .fromDateFields(notaFiscal.getDeclaracaoPrestacaoServico().getCompetencia())
                .plusMonths(1)
                .withDayOfMonth(getVencimentoIss());
        return feriadoService.proximoDiaUtil(localDate.toDate());
    }

    private NotaFiscalNfseDTO findOnMongo(Long id) {
        if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            return null;
        }
        try {
            NotaFiscalNfseDTO one = mongoRepository.findById(id).orElse(null);
            if (one != null) {
                return one;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveOnMongo(NotaFiscalNfseDTO nota) {
        if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            return;
        }
        try {
            if (nota != null) {
                if (nota.getNumero() == null && nota.getId() != null) {
                    nota.setNumero(repository.getNumeroByNota(nota.getId()));
                }
                mongoRepository.save(nota);
                notaFiscalSearchMongoRepository.save(nota.toSearch());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salvarCartaCorrecao(CartaCorrecaoNfseDTO dto) {
        repository.salvarCartaCorrecao(dto);
    }

    public Page<CartaCorrecaoNfseDTO> buscarCartaCorrecaoPorNotaFiscal(Pageable pageable, Long idNotaFiscal) {
        Page<CartaCorrecaoNfseDTO> page = repository.buscarCartaCorrecaoPorNotaFiscal(pageable, idNotaFiscal);
        if (page.getContent() != null) {
            for (CartaCorrecaoNfseDTO carta : page.getContent()) {
                if (carta.getTomadorServicoNfse() != null)
                    carta.setTomadorServicoNfse(tomadorService.findById(carta.getTomadorServicoNfse().getId()));
                carta.setPrestadorServicoNfseDTO(cadastroEconomicoService.findById(carta.getPrestadorServicoNfseDTO().getId()));
            }
        }
        return page;
    }

    public NotaFiscalNfseDTO autenticar(AutenticaNfseDTO dto) {
        return repository.buscarNfsePorAutenticacao(dto);
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void pagarNotasFiscais() {
        List<NotaFiscalNfseDTO> notas = repository.buscarNotasASeremPagas();
        for (NotaFiscalNfseDTO nota : notas) {
            repository.pagarNotaFiscal(nota);
            removeOnMongo(nota.getId());
        }
    }

    public void imprimeNota(HttpServletRequest request, HttpServletResponse response, Long id) {
        try {

            byte[] bytes = getBytesPdf(id).toByteArray();


            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=Nota_Fiscal.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
            try {
                UserNfseDTO user = userService.getUserWithAuthorities();
                if (user != null) {
                    logGeralNfseJDBCRepository.inserir("Impressão de Nota Fiscal", user.getNome(), "IMPRESSAO_NFSE", id, request);
                }
            } catch (Exception e) {
                log.debug("Erro ao gravar o historico de impressão da nota {}.", id, e);
                log.error("Erro ao gravar o historico de impressão da nota {}", id);
            }
        } catch (Exception e) {
            log.debug("Erro ao imprimir a nota {}.", id, e);
            log.error("Erro ao imprimir a nota {}", id);
        }
    }

    public void imprimirCartaCorrecao(HttpServletResponse response, Long id) {
        try {
            ByteArrayOutputStream dadosByte = new ByteArrayOutputStream();
            CartaCorrecaoNfseDTO cartaCorrecao = cartaCorrecaoJDBCRepository.findById(id);
            JasperPrint jasperPrints = gerarJasperPrintCartaCorrecao(findById(cartaCorrecao.getIdNotaFiscal()), cartaCorrecao);

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrints);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, dadosByte);
            exporter.exportReport();

            byte[] bytes = dadosByte.toByteArray();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Erro ao imprimir", e);
        }
    }


    private List<JasperPrint> gerarJasperNfse(Long idNotaFiscal, CartaCorrecaoNfseDTO cartaCorrecaoNfse) throws
            JRException {
        List<JasperPrint> jasperPrints = Lists.newArrayList();

        NotaFiscalNfseDTO notaFiscal = findById(idNotaFiscal);
        jasperPrints.add(gerarJasperPrintNfse(notaFiscal));

        if (cartaCorrecaoNfse != null) {
            jasperPrints.add(gerarJasperPrintCartaCorrecao(notaFiscal, cartaCorrecaoNfse));
        }

        return jasperPrints;
    }

    public JasperPrint gerarJasperPrintNfse(NotaFiscalNfseDTO notaFiscal) throws JRException {
        PrestadorServicoNfseDTO cadastroEconomico = cadastroEconomicoService.findOne(notaFiscal.getPrestador().getId()).getBody();
        ImagemUsuarioNfseDTO imagemPrestador = cadastroEconomicoService.getImagemPrestador(cadastroEconomico.getId());

        String nomeRelatorio = "NotaFiscalEletronica.jrxml";
        HashMap<String, Object> parametros = new HashMap<>();
        reportService.parametrosDefault(parametros);
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
        parametros.put("QRCODE", BarCode.generateInputStreamQRCodePng("https://nota.riobranco.ac.gov.br/#/notas-autenticar-qr-code/" + notaFiscal.getId(), 300, 300));
        parametros.put("SUBREPORT_NOTA", reportService.compilarJrxml("NotaFiscalEletronica_subreport.jrxml"));
        if (imagemPrestador != null) {
            parametros.put("LOGOPRESTADOR", Util.base64ToInputStream(imagemPrestador.getConteudo()));
        }
        ConfiguracaoNfseDTO configuracaoFromServer = configuracaoService.getConfiguracaoFromServer();

        ImpressaoNotaFiscalNfseDTO impressaoNotaFiscalNfseDTO = null;
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            impressaoNotaFiscalNfseDTO = impressaoNotaFiscalMongoRepository.findById(notaFiscal.getId()).orElse(null);
        }
        if (impressaoNotaFiscalNfseDTO == null)
            impressaoNotaFiscalNfseDTO = notaFiscal.toImpressaoNfse(configuracaoFromServer, buscarVencimentoIss(notaFiscal));

        return reportService.gerarJasper(nomeRelatorio, parametros, Lists.newArrayList(impressaoNotaFiscalNfseDTO));
    }

    public JasperPrint gerarJasperPrintCartaCorrecao(NotaFiscalNfseDTO notaFiscal, CartaCorrecaoNfseDTO cartaCorrecaoNfse) throws JRException {
        PrestadorServicoNfseDTO cadastroEconomico = cadastroEconomicoService.findOne(notaFiscal.getPrestador().getId()).getBody();
        ImagemUsuarioNfseDTO imagemPrestador = cadastroEconomicoService.getImagemPrestador(cadastroEconomico.getId());

        String nomeRelatorio = "CartaCorrecao.jrxml";
        HashMap<String, Object> parametros = new HashMap<>();
        reportService.parametrosDefault(parametros);
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
        parametros.put("QRCODE", BarCode.generateInputStreamQRCodePng("https://nota.riobranco.ac.gov.br/#/notas-autenticar-qr-code/" + notaFiscal.getId(), 300, 300));
        parametros.put("SUBREPORT_NOTA", reportService.compilarJrxml("NotaFiscalEletronica_subreport.jrxml"));
        if (imagemPrestador != null) {
            parametros.put("LOGOPRESTADOR", Util.base64ToInputStream(imagemPrestador.getConteudo()));
        }
        ConfiguracaoNfseDTO configuracaoFromServer = configuracaoService.getConfiguracaoFromServer();

        ImpressaoNotaFiscalNfseDTO impressaoNotaFiscalNfseDTO = null;
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            impressaoNotaFiscalNfseDTO = impressaoNotaFiscalMongoRepository.findById(notaFiscal.getId()).orElse(null);
        }
        if (impressaoNotaFiscalNfseDTO == null) {
            impressaoNotaFiscalNfseDTO = notaFiscal.toImpressaoNfse(configuracaoFromServer, buscarVencimentoIss(notaFiscal));
        }
        impressaoNotaFiscalNfseDTO.setDiscriminacaoCartaCorrecao(cartaCorrecaoNfse.getDescricaoAlteracao());
        impressaoNotaFiscalNfseDTO.setNumeroCartaCorrecao(cartaCorrecaoNfse.getSequencialCartaCorrecao());
        impressaoNotaFiscalNfseDTO.setEmissaoCartaCorrecao(cartaCorrecaoNfse.getDataEmissao());
        return reportService.gerarJasper(nomeRelatorio, parametros, Lists.newArrayList(impressaoNotaFiscalNfseDTO));
    }

    public void preGerarXmlNotaFiscal(BaixarXMLNfseDTO baixarXMLNfseDTO) {
        XmlNotaFiscalDTO xmlNotaFiscalDTO = new XmlNotaFiscalDTO();
        xmlNotaFiscalDTO.setInscricaoCadastral(baixarXMLNfseDTO.getInscricao());
        xmlNotaFiscalDTO.setPercentual(1);
        xmlNotaFiscalService.getGerandoXMl().add(xmlNotaFiscalDTO);
        gerarXmlNotaFiscal(baixarXMLNfseDTO, xmlNotaFiscalDTO);
    }

    @Async
    public void gerarXmlNotaFiscal(BaixarXMLNfseDTO baixarXMLNfseDTO, XmlNotaFiscalDTO xmlNotaFiscalDTO) {
        try {
            ConsultaGenericaNfseDTO consultaNotaFiscal = baixarXMLNfseDTO.getConsultaGenericaNfseDTO();

            List<Long> notasFiscais = repository.buscarIdsNotasFiscais(consultaNotaFiscal.getParametrosQuery());

            JAXBContext jaxbContext = JAXBContext.newInstance(ConsultarNfseLote.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            StringWriter sw = new StringWriter();

            ConsultarNfseLote consultarNfseLote = new ConsultarNfseLote();
            consultarNfseLote.setListaNfse(new ConsultarNfseLote.ListaNfse());
            consultarNfseLote.getListaNfse().setCompNfse(new ArrayList<CompNfse>());
            int processados = 0;
            if (notasFiscais != null && !notasFiscais.isEmpty()) {
                for (Long id : notasFiscais) {
                    NotaFiscalNfseDTO notaFiscal = findById(id);
                    consultarNfseLote.getListaNfse().getCompNfse().add(criarCompNfse(notaFiscal));
                    processados++;
                    xmlNotaFiscalDTO.setPercentual((processados * 100) / notasFiscais.size());
                    xmlNotaFiscalService.getGerandoXMl().add(xmlNotaFiscalDTO);
                }
            }
            jaxbMarshaller.marshal(consultarNfseLote, sw);
            String xmlNotaFiscal = sw.toString();
            xmlNotaFiscalDTO.setConteudo(retirarTagsDesnecessariasXml(xmlNotaFiscal));
            xmlNotaFiscalDTO.setConteudo(identarXml(xmlNotaFiscalDTO.getConteudo()));
        } catch (Exception e) {
            log.debug("Erro na geração do xml das notas fiscais do cmc {}. Erro {}", baixarXMLNfseDTO.getInscricao(), e);
            log.error("Ocorreu um erro na geração do xml das notas fiscais do cmc {}.", baixarXMLNfseDTO.getInscricao());
            xmlNotaFiscalDTO.setErro(Boolean.TRUE);
        } finally {
            xmlNotaFiscalDTO.setPercentual(100);
        }
    }

    public XmlNotaFiscalDTO consultarXmlNotas(BaixarXMLNfseDTO baixarXMLNfseDTO) throws IOException {
        Optional<XmlNotaFiscalDTO> first = xmlNotaFiscalService.getGerandoXMl()
                .stream()
                .filter(xmlNotaFiscalDTO -> xmlNotaFiscalDTO.getInscricaoCadastral().equals(baixarXMLNfseDTO.getInscricao()))
                .findFirst();

        if (!first.isPresent()) {
            XmlNotaFiscalDTO xmlNotaFiscalDTO = new XmlNotaFiscalDTO();
            xmlNotaFiscalDTO.setErro(Boolean.TRUE);
            return xmlNotaFiscalDTO;
        }

        XmlNotaFiscalDTO xmlNotaFiscal = first.get();
        if (xmlNotaFiscal.getPercentual() != null && xmlNotaFiscal.getPercentual() >= 100) {
            if (!Strings.isNullOrEmpty(xmlNotaFiscal.getConteudo())) {
                tratarXmlNotaFiscal(baixarXMLNfseDTO, xmlNotaFiscal);
                xmlNotaFiscal.setConteudo(getArquivoBase64DTO(xmlNotaFiscal).getConteudo());
            }
            xmlNotaFiscalService.getGerandoXMl().remove(xmlNotaFiscal);
        }
        return xmlNotaFiscal;
    }

    public void tratarXmlNotaFiscal(BaixarXMLNfseDTO baixarXMLNfseDTO, XmlNotaFiscalDTO xmlNotaFiscal) {
        if (BaixarXMLNfseDTO.Tipo.ENVELOPE_SOAP.equals(baixarXMLNfseDTO.getTipo())) {
            xmlNotaFiscal.setConteudo(xmlNotaFiscal.getConteudo().replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            xmlNotaFiscal.setConteudo("<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
                    "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> " +
                    "<SOAP-ENV:Body> " +
                    "<ConsultarLoteRps.ExecuteResponse xmlns=\"http://www.e-nfs.com.br\"> " +
                    "<Outputxml xmlns=\"http://www.e-nfs.com.br\"> " +
                    "<ConsultarLoteRpsResposta>" +
                    xmlNotaFiscal.getConteudo() +
                    "</ConsultarLoteRpsResposta> " +
                    "</Outputxml> " +
                    "</ConsultarLoteRps.ExecuteResponse> " +
                    "</SOAP-ENV:Body> " +
                    "</SOAP-ENV:Envelope>");

        }
    }

    public ArquivoBase64DTO gerarXmlNotaFiscal(Long idNotaFiscal) throws IOException, JAXBException, DatatypeConfigurationException {
        XmlNotaFiscalDTO xmlNotaFiscal = gerarXmlNotaFiscal(findById(idNotaFiscal));
        return getArquivoBase64DTO(xmlNotaFiscal);
    }

    private ArquivoBase64DTO getArquivoBase64DTO(XmlNotaFiscalDTO xmlNotaFiscal) throws IOException {
        File file = File.createTempFile("tempfile", ".xml");

        //write it
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(xmlNotaFiscal.getConteudo());
        bw.close();

        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        String base64 = new String(encoded, StandardCharsets.UTF_8);
        return new ArquivoBase64DTO(base64);
    }

    public ArquivoBase64DTO gerarXlsNotas(ConsultaGenericaNfseDTO consultaGenericaNfseDTO) throws Exception {
        Pageable pageable = PaginationUtil.generatePageRequest(consultaGenericaNfseDTO.getOffset(), consultaGenericaNfseDTO.getLimit());

        Page<NotaFiscalSearchDTO> notas = getRepository().consultarPaginado(pageable, consultaGenericaNfseDTO.getParametrosQuery(),
                consultaGenericaNfseDTO.getOrderBy());

        String json = new Gson().toJson(notas);
        json = "{\"infile\":" + json + "}";
        JSONObject output;

        output = new JSONObject(json);

        JSONArray docs = output.getJSONArray("infile");

        File file = File.createTempFile("tempfile", ".csv");
        String csv = CDL.toString(docs);
        FileUtils.writeStringToFile(file, csv);

        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        String base64 = new String(encoded, StandardCharsets.UTF_8);
        return new ArquivoBase64DTO(base64);
    }

    public NotaFiscalNfseDTO novoComPrestador(Long prestadorId) {
        ConfiguracaoNfseDTO configuracao = configuracaoService.getConfiguracaoFromServer();
        UserNfseDTO user = userService.getUserWithAuthorities();
        PrestadorServicoNfseDTO cad = cadastroEconomicoService.findOne(prestadorId).getBody();

        if (cad.getEnquadramentoFiscal() == null) {
            throw new NfseOperacaoNaoPermitidaException("O prestador não possuí uma situação tributária cadastrada!");
        } else if (!TipoNotaFiscalServicoNfseDTO.ELETRONICA.equals(cad.getEnquadramentoFiscal().getTipoNotaFiscal())) {
            throw new NfseOperacaoNaoPermitidaException("O prestador não está cadastrado como emissor de nota fiscal eletrônica!");
        }

        validarDebitosEmDia(cad, configuracao);
        validarBloqueioEmissaoNfse(user, cad);
        return criarNotaFiscalComPrestador(cad);

    }

    private void validarBloqueioEmissaoNfse(UserNfseDTO usuario,
                                            PrestadorServicoNfseDTO prestador) {
        PrestadorUsuarioNfseDTO prestadorUsuario = cadastroEconomicoService.findByUserAndPrestador(usuario.getId(), prestador.getId());
        if (prestadorUsuario != null && prestadorUsuario.isBloqueadoEmissaoNfse()) {
            throw new NfseOperacaoNaoPermitidaException("EMISSÃO DE NFS-e BLOQUEADA. Por motivo de segurança a emissão de nota fiscal " +
                    "foi bloqueada. Para desbloquear entre em contato com o telefone: " + prestadorUsuario.getTelefoneDesbloqueio() +
                    ", ou compareça à Divisão de ISSQN da Prefeitura de Rio Branco.");
        }
    }

    public NotaFiscalNfseDTO novoRpsManual(Long prestadorId) {
        NotaFiscalNfseDTO nota = novoComPrestador(prestadorId);
        nota.setRps(new RpsNfseDTO());
        Long ultimoRps = rpsJDBCRepository.getUltimoNumeroRps(prestadorId) + 1;
        nota.getRps().setNumero(ultimoRps.toString());
        return nota;
    }

    private NotaFiscalNfseDTO criarNotaFiscalComPrestador(PrestadorServicoNfseDTO prestador) {
        NotaFiscalNfseDTO nota = criarNotaFiscal();
        nota.setPrestador(prestador);
        nota.getDeclaracaoPrestacaoServico().setDadosPessoaisPrestador(prestador.getPessoa().getDadosPessoais());
        nota.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().setTipoIssqn(prestador.getEnquadramentoFiscal().getTipoIssqn());
        nota.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().setRegimeTributario(prestador.getEnquadramentoFiscal().getRegimeTributario());

        UserNfseDTO usuarioWeb = userService.getUserWithAuthorities();

        Set<String> emails = Sets.newHashSet();
        if (usuarioWeb != null && usuarioWeb.getEmail() != null) {
            emails.add(usuarioWeb.getEmail());
        }
        nota.setUltimaEmissao(repository.getUltimaEmissao(prestador.getId()));
        return nota;
    }

    public NotaFiscalNfseDTO criarNotaFiscal() {
        NotaFiscalNfseDTO nota = new NotaFiscalNfseDTO();
        nota.setCodigoVerificacao(new Generex("[0-9a-fA-F]{16}").random().replaceAll("(.{4})(?!$)", "$1-"));
        nota.setChaveAcesso(new Generex("[0-9a-fA-F]{32}").random());
        DeclaracaoPrestacaoServicoNfseDTO declaracao = new DeclaracaoPrestacaoServicoNfseDTO();
        nota.setTomador(new TomadorServicoDTO());
        declaracao.setSituacao(SituacaoDeclaracaoNfseDTO.EMITIDA);
        declaracao.setNaturezaOperacao(ExigibilidadeNfseDTO.TRIBUTACAO_MUNICIPAL);
        declaracao.setItens(new ArrayList());
        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = new ItemDeclaracaoServicoNfseDTO();
        declaracao.getItens().add(itemDeclaracaoServico);
        itemDeclaracaoServico.setQuantidade(new BigDecimal("1"));
        nota.setDeclaracaoPrestacaoServico(declaracao);
        nota.setSituacao(SituacaoDeclaracaoNfseDTO.ABERTA);
        nota.getDeclaracaoPrestacaoServico().setTributosFederais(new TributosFederaisNfseDTO());
        return nota;
    }

    private void validarDebitosEmDia(PrestadorServicoNfseDTO cad, ConfiguracaoNfseDTO configuracaoNfse) {
        if (!TipoIssqnNfseDTO.FIXO.equals(cad.getEnquadramentoFiscal().getTipoIssqn()) ||
                configuracaoNfse.getPermiteEmissaoIssFixoComDebitoVencido()) {
            return;
        }
        ConsultaParcela consultaParcela = new ConsultaParcela(consultaDebitosService);
        consultaParcela.addParameter(br.com.webpublico.tributario.consultadebitos.ConsultaParcela.Campo.CADASTRO_ID,
                br.com.webpublico.tributario.consultadebitos.ConsultaParcela.Operador.IGUAL, cad.getId());
        consultaParcela.addParameter(br.com.webpublico.tributario.consultadebitos.ConsultaParcela.Campo.TIPO_CALCULO,
                br.com.webpublico.tributario.consultadebitos.ConsultaParcela.Operador.IGUAL, TipoCalculoDTO.ISS);
        consultaParcela.addParameter(br.com.webpublico.tributario.consultadebitos.ConsultaParcela.Campo.PARCELA_VENCIMENTO,
                br.com.webpublico.tributario.consultadebitos.ConsultaParcela.Operador.MENOR, new Date());
        consultaParcela.addParameter(br.com.webpublico.tributario.consultadebitos.ConsultaParcela.Campo.PARCELA_SITUACAO,
                br.com.webpublico.tributario.consultadebitos.ConsultaParcela.Operador.IGUAL, SituacaoParcelaDTO.EM_ABERTO);
        consultaParcela.executaConsulta();
        if (!consultaParcela.getResultados().isEmpty()) {
            throw new NfseOperacaoNaoPermitidaException("O cadastro econômico possui débitos vencidos. Por favor regularize a situação para emissão de nota fiscal.");
        }
    }

    public Page<String> buscarDiscriminacoesPorPrestador(Pageable pageable, Long prestador) {
        return repository.buscarDiscriminacoesPorPrestador(pageable, prestador);
    }

    public TemplateNfseDTO getTemplate(TipoTemplateNfse tipo) {
        if (templates == null) {
            templates = Maps.newHashMap();
        }
        if (templates.get(tipo) == null) {
            templates.put(tipo, templateJDBCRepository.findByTipo(tipo));
        }
        return templates.get(tipo);
    }

    public void enviarNotaPorEmail(String emails, NotaFiscalNfseDTO notaFiscal) {
        try {
            if (emails != null && !emails.trim().isEmpty()) {
                TemplateNfseDTO templateEmail = getTemplate(TipoTemplateNfse.EMAIL_NOTAFISCAL);
                String[] split = emails.split(",");
                for (String mail : split) {
                    enviaEmailNota(notaFiscal, mail, templateEmail);
                }
            }
        } catch (Exception e) {
            log.error("Erro ao comunicar nota por email. {}", e.getMessage());
            log.debug("Detalhes do erro ao comunicar nota por email. ", e);
        }
        notaFiscal.setEnviouPorEmail(true);
        repository.defineEmailEnviado(notaFiscal);
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            mongoRepository.save(notaFiscal);
        }
    }

    public void enviarEmailCancelamento(NotaFiscalNfseDTO notaFiscal) {
        try {
            ConfiguracaoNfseDTO config = configuracaoService.getConfiguracaoFromServer();
            if (!config.getProducao() && !config.getEnviaEmailEmHomologacao()) {
                log.info("Não vai comunicar por email o cancelamento da Nfse {}-{}",
                        notaFiscal.getNumero(), notaFiscal.getPrestador().getInscricaoMunicipal());
                return;
            }
            if (!Strings.isNullOrEmpty(notaFiscal.getPrestador().getPessoa().getDadosPessoais().getEmail())) {
                enviarEmailCancelamento(notaFiscal, getTemplate(TipoTemplateNfse.NFSE_CANCELADA_TOMADOR),
                        notaFiscal.getPrestador().getPessoa().getDadosPessoais().getEmail());
            }
            if (notaFiscal.getTomador() != null &&
                    notaFiscal.getTomador().getDadosPessoais() != null &&
                    !Strings.isNullOrEmpty(notaFiscal.getTomador().getDadosPessoais().getEmail())) {
                enviarEmailCancelamento(notaFiscal, getTemplate(TipoTemplateNfse.NFSE_CANCELADA_TOMADOR),
                        notaFiscal.getTomador().getDadosPessoais().getEmail());
            }
        } catch (Exception e) {
            log.debug("Erro ao comunicar cancelamento de nota por email", e);
        }
    }

    public void enviarEmailCancelamento(NotaFiscalNfseDTO notaFiscal, TemplateNfseDTO template, String email) {
        if (template == null) {
            return;
        }
        String conteudoEmail = new TrocaTagEmailNotaFiscal(notaFiscal, configuracaoService.getConfiguracaoFromServer())
                .trocarTags(template.getConteudo());
        try {
            List<AnexoDTO> anexos = Lists.newArrayList();
            ByteArrayOutputStream osPdf = getBytesPdf(notaFiscal.getId());
            anexos.add(new AnexoDTO(osPdf.toByteArray(), notaFiscal.getPrestador().getInscricaoMunicipal() +
                    "NFSE" + notaFiscal.getNumero(), "application/pdf"));
            emailService.enviarEmail(email, "Cancelamento de Nota Fiscal de Serviços",
                    conteudoEmail, Boolean.TRUE, anexos);
        } catch (Exception e) {
            log.debug("Erro ao comunicar nota por email", e);
            throw new NfseOperacaoNaoPermitidaException("Ocorreu um erro ao enviar o e-mail!");
        }
    }

    private void enviaEmailNota(NotaFiscalNfseDTO notaFiscal, String mail, TemplateNfseDTO templateEmail) {
        String conteudoEmail = new TrocaTagEmailNotaFiscal(notaFiscal, configuracaoService.getConfiguracaoFromServer())
                .trocarTags(templateEmail.getConteudo());
        try {
            List<AnexoDTO> anexos = Lists.newArrayList();
            ByteArrayOutputStream osXml = getBytesXml(notaFiscal);
            anexos.add(new AnexoDTO(osXml.toByteArray(), notaFiscal.getPrestador().getInscricaoMunicipal() +
                    "NFSE" + notaFiscal.getNumero(), "text/xml"));
            ByteArrayOutputStream osPdf = getBytesPdf(notaFiscal.getId());
            anexos.add(new AnexoDTO(osPdf.toByteArray(), notaFiscal.getPrestador().getInscricaoMunicipal() +
                    "NFSE" + notaFiscal.getNumero(), "application/pdf"));

            emailService.enviarEmail(mail, "Emissão de Nota Fiscal de Serviços",
                    conteudoEmail, Boolean.TRUE, anexos);
        } catch (Exception e) {
            log.error("Erro ao comunicar nota por email. {}", e.getMessage());
            log.debug("Detalhes do erro ao comunicar nota por email. ", e);
            throw new NfseOperacaoNaoPermitidaException("Ocorreu um erro ao enviar o e-mail!");
        }
    }

    public ByteArrayOutputStream getBytesPdf(Long id) throws JRException, IOException {
        ArquivoBase64DTO one = null;
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            one = arquivoBase64MongoRepository.findById(id).orElse(null);
        }
        if (one == null) {
            ByteArrayOutputStream dadosByte = new ByteArrayOutputStream();
            List<JasperPrint> jasperPrints = gerarJasperNfse(id, null);
            List<CartaCorrecaoNfseDTO> cartas = cartaCorrecaoJDBCRepository.findByNotaFiscalId(id);
            for (CartaCorrecaoNfseDTO carta : cartas) {
                jasperPrints.add(gerarJasperPrintCartaCorrecao(findById(carta.getIdNotaFiscal()), carta));
            }
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrints);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, dadosByte);
            exporter.exportReport();

            ArquivoBase64DTO dto = new ArquivoBase64DTO();
            dto.setId(id);
            dto.setConteudo(Base64.encodeBase64String(dadosByte.toByteArray()));
            if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
                arquivoBase64MongoRepository.save(dto);
            }
            return dadosByte;
        } else {
            InputStream inputStream = Util.base64ToInputStream(one.getConteudo());
            byte[] bytes = ByteStreams.toByteArray(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
            baos.write(bytes, 0, bytes.length);
            return baos;
        }
    }

    private ByteArrayOutputStream getBytesXml(NotaFiscalNfseDTO notaFiscal) throws IOException, JAXBException, DatatypeConfigurationException {
        XmlNotaFiscalDTO one = null;
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            one = xmlBase64MongoRepository.findById(notaFiscal.getId()).orElse(null);
        }
        if (one == null) {
            File file = File.createTempFile("tempfile", ".xml");
            //write it
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(gerarXmlNotaFiscal(notaFiscal).getConteudo());
            bw.close();

            byte[] bytesXml = FileUtils.readFileToByteArray(file);

            ByteArrayOutputStream osXml = new ByteArrayOutputStream(bytesXml.length);
            osXml.write(bytesXml, 0, bytesXml.length);
            osXml.flush();
            osXml.close();

            XmlNotaFiscalDTO dto = new XmlNotaFiscalDTO();
            dto.setId(notaFiscal.getId());
            dto.setConteudo(Base64.encodeBase64String(osXml.toByteArray()));
            if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
                xmlBase64MongoRepository.save(dto);
            }
            return osXml;
        } else {
            InputStream inputStream = Util.base64ToInputStream(one.getConteudo());
            byte[] bytes = ByteStreams.toByteArray(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
            baos.write(bytes, 0, bytes.length);
            return baos;
        }
    }

    public XmlNotaFiscalDTO gerarXmlNotaFiscal(NotaFiscalNfseDTO notaFiscal) throws
            JAXBException, DatatypeConfigurationException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CompNfse.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        StringWriter sw = new StringWriter();

        CompNfse compNfse = criarCompNfse(notaFiscal);

        jaxbMarshaller.marshal(compNfse, sw);

        String xmlNotaFiscal = sw.toString();

        xmlNotaFiscal = identarXml(xmlNotaFiscal);

        XmlNotaFiscalDTO xmlNotaFiscalDTO = new XmlNotaFiscalDTO();
        xmlNotaFiscalDTO.setConteudo(retirarTagsDesnecessariasXml(xmlNotaFiscal));
        return xmlNotaFiscalDTO;
    }

    private String retirarTagsDesnecessariasXml(String xml) {
        xml = xml.replace(" xmlns:ns2=\"http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd\" xmlns:ns4=\"http://nfse.webpublico.com.br/iss/nfse_v1\" xmlns:ns3=\"http://www.w3.org/2000/09/xmldsig#\"", "");
        xml = xml.replace("ns2:", "");
        return xml;
    }

    private CompNfse criarCompNfse(NotaFiscalNfseDTO notaFiscalNfseDTO) throws DatatypeConfigurationException {
        br.com.webpublico.domain.dto.nfse10.TcCompNfse tcCompNfse = NfseXMLV1.criarTcCompNfse(notaFiscalNfseDTO, configuracaoService.getConfiguracaoFromServer());
        if (tcCompNfse.getNfse().getInfNfse().getPrestadorServico().getEndereco() != null) {
            MunicipioNfseDTO municipioPrestador = notaFiscalNfseDTO.getPrestador().getPessoa().getDadosPessoais().getMunicipio();
            if (municipioPrestador != null && !Strings.isNullOrEmpty(municipioPrestador.getNome()) && !Strings.isNullOrEmpty(municipioPrestador.getEstado())) {
                MunicipioNfseDTO cidade = cidadeService.buscarPorNomeAndEstado(municipioPrestador.getNome(), municipioPrestador.getEstado());
                if (cidade != null) {
                    tcCompNfse.getNfse().getInfNfse().getPrestadorServico().getEndereco().setCodigoMunicipio(new Integer(cidade.getCodigoIbge()));
                }
            }
        }
        if (tcCompNfse.getNfse().getInfNfse().getTomadorServico() != null && tcCompNfse.getNfse().getInfNfse().getTomadorServico().getEndereco() != null) {
            MunicipioNfseDTO municipioTomador = notaFiscalNfseDTO.getPrestador().getPessoa().getDadosPessoais().getMunicipio();
            if (municipioTomador != null && !Strings.isNullOrEmpty(municipioTomador.getNome()) && !Strings.isNullOrEmpty(municipioTomador.getEstado())) {
                MunicipioNfseDTO cidade = cidadeService.buscarPorNomeAndEstado(municipioTomador.getNome(), municipioTomador.getEstado());
                if (cidade != null) {
                    tcCompNfse.getNfse().getInfNfse().getTomadorServico().getEndereco().setCodigoMunicipio(new Integer(cidade.getCodigoIbge()));
                }
            }
        }
        CompNfse compNfse = new CompNfse();
        compNfse.setNfse(tcCompNfse.getNfse());
        if (SituacaoDeclaracaoNfseDTO.CANCELADA.equals(notaFiscalNfseDTO.getSituacao())) {
            List<CancelamentoNfseDTO> cancelamentos = cancelamentoService.findByNotaId(notaFiscalNfseDTO.getId());
            if (!cancelamentos.isEmpty()) {
                TcCancelamentoNfse tcCancelamentoNfse = new TcCancelamentoNfse();
                tcCancelamentoNfse.setConfirmacao(new TcConfirmacaoCancelamento());
                GregorianCalendar competencia = new GregorianCalendar();
                competencia.setTime(cancelamentos.get(0).getDataCancelamento());
                tcCancelamentoNfse.getConfirmacao().setDataHoraCancelamento(DatatypeFactory.newInstance().newXMLGregorianCalendar(competencia));
                tcCancelamentoNfse.getConfirmacao().setPedido(new TcPedidoCancelamento());
                tcCancelamentoNfse.getConfirmacao().getPedido().setInfPedidoCancelamento(new TcInfPedidoCancelamento());
                tcCancelamentoNfse.getConfirmacao().getPedido().getInfPedidoCancelamento().setCodigoCancelamento(cancelamentos.get(0).getMotivoCancelamento().getCodigo().toString());
                compNfse.setNfseCancelamento(tcCancelamentoNfse);
            }
        }

        return compNfse;
    }

    private String identarXml(String xmlNotaFiscal) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xmlNotaFiscal));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            xmlNotaFiscal = xmlOutput.getWriter().toString();
        } catch (Exception e) {
            log.debug("Erro ao identar XML de Nota Fiscal [[{}]]", e);
            return xmlNotaFiscal;
        }
        return xmlNotaFiscal;
    }

    public List<ResultadoParcela> getParcelasDaNota(Long idNota) {
        NotaFiscalNfseDTO nota = findById(idNota);
        List<Long> idCalculo = repository.buscarIdDoCalculoPorNotaFiscal(idNota, nota.getDeclaracaoPrestacaoServico().getIssRetido());
        if (idCalculo != null && !idCalculo.isEmpty()) {
            List<ResultadoParcela> resultados = new ConsultaParcela(consultaDebitosService)
                    .addParameter(ConsultaParcela.Campo.CALCULO_ID, ConsultaParcela.Operador.IN, idCalculo)
                    .executaConsulta().getResultados();
            return resultados;
        }
        return Lists.newArrayList();
    }

    public HttpHeaders getHeadersDefault() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return requestHeaders;
    }

    public NotaFiscalNfseDTO cancelar(CancelamentoNfseDTO cancelamento) {
        if (SituacaoDeclaracaoNfseDTO.CANCELADA.equals(cancelamento.getNotaFiscal().getSituacao())) {
            return cancelamento.getNotaFiscal();
        }
        cancelamento.setTipoDocumento(TipoDocumentoCancelamentoNfseDTO.NOTA_FISCAL);
        cancelamento = cancelamentoService.salvarCancelamento(cancelamento,
                cancelamento.getNotaFiscal().getDeclaracaoPrestacaoServico(),
                cancelamento.getNotaFiscal().getPrestador().getPessoa().getDadosPessoais(),
                cancelamento.getNotaFiscal().getSituacao());
        if (cancelamento != null) {
            removeOnMongo(cancelamento.getNotaFiscal().getId());
            cancelamento.getNotaFiscal().getDeclaracaoPrestacaoServico().setCancelamento(cancelamento);
            declaracaoPrestacaoServicoService.atribuirCancelamento(cancelamento.getNotaFiscal().getDeclaracaoPrestacaoServico());
        }
        NotaFiscalNfseDTO notaFiscalNfseDTO = findById(cancelamento.getNotaFiscal().getId());
        enviarEmailCancelamento(notaFiscalNfseDTO);
        return notaFiscalNfseDTO;
    }

    public Long getUltimoNumeroRps(Long prestadorId) {
        return rpsJDBCRepository.getUltimoNumeroRps(prestadorId);
    }

    public CancelamentoNfseDTO marcarCancelamentoVisualizado(CancelamentoNfseDTO dto) {
        return cancelamentoService.marcarCancelamentoVisualizado(dto);
    }

    public EstatisticaMensalNfseDTO buscarEstatisticaMensalNotasRecebidas(Integer mes, Integer ano, String cpf) {
        return repository.buscarEstatisticaMensalNotasRecebidas(mes, ano, cpf);
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void atualizarPlacar() {
        placar.setPrestadoresAutorizados(cadastroEconomicoService.buscarQuantidadePrestadoresAutorizados());
        placar.setNotasFiscaisEmitidas(repository.buscarQuantidadeNotasFiscaisEmitidas());
        placar.setUsuariosAtivos(userService.buscarQuantidadeUsuariosAtivos());
    }

    public PlacarNfseDTO getPlacar() {
        return placar;
    }

    @XmlRootElement
    public static class ListaNotasXml {
        private List<NotaFiscalNfseDTO> listaNotas;

        public List<NotaFiscalNfseDTO> getListaNotas() {
            return listaNotas;
        }

        public void setListaNotas(List<NotaFiscalNfseDTO> listaNotas) {
            this.listaNotas = listaNotas;
        }
    }

    public void gerarRelatorioNotasFiscaisPDF(HttpServletResponse response,
                                              EmissaoRelatorioNotaFiscalDTO emissaoRelatorioNotaFiscal) {
        try {
            List<NotaFiscalNfseDTO> notas = repository.consultar(null,
                    emissaoRelatorioNotaFiscal.getConsultaNotaFiscal().getParametrosQuery(),
                    emissaoRelatorioNotaFiscal.getConsultaNotaFiscal().getOrderBy());
            for (NotaFiscalNfseDTO nota : notas) {
                preencher(nota);
                recuperarNumeroDAM(nota);
            }
            byte[] bytes = gerarBytesRelatorioNotasFiscaisPDF(notas, emissaoRelatorioNotaFiscal.getTipoRelatorio(),
                    emissaoRelatorioNotaFiscal.getCriteriosUtilizados(),
                    emissaoRelatorioNotaFiscal.getTipoNota(), false);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=RelatorioNotasFiscais.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Erro ao gerar relatório e notas emitidas {} ", e);
        }
    }

    private void recuperarNumeroDAM(NotaFiscalNfseDTO nota) {
        nota.setNumeroDAM(repository.getNumeroDAM(nota.getDeclaracaoPrestacaoServico().getId(),
                nota.getDeclaracaoPrestacaoServico().getIssRetido()));
    }

    public byte[] gerarBytesRelatorioNotasFiscaisPDF(List<NotaFiscalNfseDTO> notas,
                                                     TipoRelatorioNfseDTO tipoRelatorio,
                                                     String criteriosUtilizados,
                                                     EmissaoRelatorioNotaFiscalDTO.TipoNota tipoNota,
                                                     boolean excel) {
        byte[] bytes = new byte[0];
        try {
            String jrxml = "";
            if (TipoRelatorioNfseDTO.RESUMIDO.equals(tipoRelatorio)) {
                jrxml = "RelatorioNotasEmitidasRecebidas.jrxml";
            } else {
                jrxml = "RelatorioNotasEmitidasRecebidasDetalhado.jrxml";
            }
            HashMap<String, Object> parametros = new HashMap<>();
            reportService.parametrosDefault(parametros);
            parametros.put("FILTROS", criteriosUtilizados);
            parametros.put("TIPONOTA", tipoNota.name());
            return reportService.gerarRelatorio(jrxml, parametros, notas, excel);
        } catch (Exception e) {
            log.error("Erro ao gerar o relatorio de notas fiscais {}", e);
        }
        return bytes;
    }

    public void gerarRelatorioNotasFiscaisExcel(HttpServletResponse response,
                                                Long prestador,
                                                EmissaoRelatorioNotaFiscalDTO emissaoRelatorioNotaFiscal) {
        try {
            PrestadorServicoNfseDTO prestadorServicoNfseDTO = cadastroEconomicoService.findById(prestador);
            byte[] bytes = gerarBytesRelatorioNotasFiscaisExcel(prestadorServicoNfseDTO, emissaoRelatorioNotaFiscal);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "inline; filename=RelatorioNotasFiscais.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de notas fiscais em excel {} ", e);
        }
    }

    public byte[] gerarBytesRelatorioNotasFiscaisExcel(PrestadorServicoNfseDTO prestador,
                                                       EmissaoRelatorioNotaFiscalDTO emissaoRelatorioNotaFiscal) throws Exception {
        String nomePlanilha = "Notas Fiscais " + (EmissaoRelatorioNotaFiscalDTO.TipoNota.EMITIDA.equals(emissaoRelatorioNotaFiscal.getTipoNota()) ?
                "Emitidas" : "Recebidas");
        String titulo = "Relatório de Notas Fiscais " + (EmissaoRelatorioNotaFiscalDTO.TipoNota.EMITIDA.equals(emissaoRelatorioNotaFiscal.getTipoNota()) ?
                "Emitidas" : "Recebidas");
        String contribuinte = prestador.getPessoa().getDadosPessoais().getCpfCnpj() + " - " +
                prestador.getPessoa().getDadosPessoais().getNomeRazaoSocial();
        List<RelatorioDocumentoDTO> documentos = Lists.newArrayList();
        List<NotaFiscalSearchDTO> notas = repositorySearch.consultar(null, emissaoRelatorioNotaFiscal.getConsultaNotaFiscal().getParametrosQuery(),
                emissaoRelatorioNotaFiscal.getConsultaNotaFiscal().getOrderBy());
        for (NotaFiscalSearchDTO notaFiscalSearch : notas) {
            String cpfCnpj = EmissaoRelatorioNotaFiscalDTO.TipoNota.EMITIDA.equals(emissaoRelatorioNotaFiscal.getTipoNota()) ?
                    notaFiscalSearch.getCpfCnpjTomador() : notaFiscalSearch.getCpfCnpjPrestador();
            String nomeRazaoSocial = EmissaoRelatorioNotaFiscalDTO.TipoNota.EMITIDA.equals(emissaoRelatorioNotaFiscal.getTipoNota()) ?
                    notaFiscalSearch.getNomeTomador() : notaFiscalSearch.getNomePrestador();
            String exigibilidade = "";
            if (!Strings.isNullOrEmpty(notaFiscalSearch.getNaturezaOperacao())) {
                exigibilidade = ExigibilidadeNfseDTO.valueOf(notaFiscalSearch.getNaturezaOperacao()).getDescricao();
            }
            documentos.add(new RelatorioDocumentoDTO(notaFiscalSearch.getNumero(), cpfCnpj, nomeRazaoSocial,
                    notaFiscalSearch.getEmissao(),
                    SituacaoDeclaracaoNfseDTO.valueOf(notaFiscalSearch.getSituacao()).getDescricao(),
                    exigibilidade,
                    notaFiscalSearch.getTotalServicos(), notaFiscalSearch.getIss()));
        }
        ConfiguracaoNfseDTO configuracao = configuracaoService.getConfiguracaoFromServer();
        GeradorExcelRelatorioDocumento geradorExcel = new GeradorExcelRelatorioDocumento(configuracao.getLogoMunicipio(),
                nomePlanilha, titulo, contribuinte, emissaoRelatorioNotaFiscal.getCriteriosUtilizados(),
                documentos);
        File file = geradorExcel.gerar();
        return FileUtils.readFileToByteArray(file);
    }

    public NotaFiscalNfseDTO findById(Long id) {
        NotaFiscalNfseDTO nota = findOnMongo(id);
        if (nota != null) return nota;
        nota = repository.findById(id);
        preencher(nota);
        saveOnMongo(nota);
        return nota;
    }

    public NotaFiscalNfseDTO findByInscricaoCadastralAndNumero(String inscricaoCadastral, Long numero, Boolean homologa) {
        NotaFiscalNfseDTO nota = repository.findByInscricaoCadastralAndNumero(inscricaoCadastral, numero, homologa);
        preencher(nota);
        return nota;
    }

    public NotaFiscalNfseDTO findByIdPrestadorAndNumero(Long idPrestador, Long numero) {
        NotaFiscalNfseDTO nota = repository.findByIdPrestadorAndNumero(idPrestador, numero);
        preencher(nota);
        return nota;
    }

    @Override
    public void preencher(NotaFiscalNfseDTO dto) {
        if (dto != null) {
            dto.setDeclaracaoPrestacaoServico(declaracaoPrestacaoServicoService.findById(dto.getDeclaracaoPrestacaoServico().getId()));
            if (dto.getPrestador() != null) {
                dto.setPrestador(cadastroEconomicoService.findOne(dto.getPrestador().getId()).getBody());
            }
            if (dto.getTomador() != null) {
                dto.setTomador(tomadorService.findById(dto.getTomador().getId()));
            }
            if (dto.getRps() != null) {
                dto.setRps(rpsJDBCRepository.findById(dto.getRps().getId()));
            }
        }
    }

    public List<NotaFiscalNfseDTO> findByLoteRps(Long idLoteRps) {
        List<NotaFiscalNfseDTO> notas = repository.findByLoteRps(idLoteRps);
        for (NotaFiscalNfseDTO nota : notas) {
            preencher(nota);
        }
        return notas;
    }

    public void removeOnMongo(Long id) {
        if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            return;
        }
        try {
            if (id != null) {
                mongoRepository.deleteById(id);
                impressaoNotaFiscalMongoRepository.deleteById(id);
                arquivoBase64MongoRepository.deleteById(id);
                xmlBase64MongoRepository.deleteById(id);
                notaFiscalSearchMongoRepository.deleteById(id);
            }
        } catch (Exception e) {
            log.error("Erro ao remover dados do mongo", e);
        }
    }

    public NotaFiscalNfseDTO calcularValores(NotaFiscalNfseDTO notaFiscal) {
        if (notaFiscal.getDeclaracaoPrestacaoServico().getItens() == null || notaFiscal.getDeclaracaoPrestacaoServico().getItens().isEmpty()) {
            return notaFiscal;
        }
        notaFiscal.setDescontosIncondicionais(BigDecimal.ZERO);
        notaFiscal.setDescontosCondicionais(BigDecimal.ZERO);
        notaFiscal.setDeducoesLegais(BigDecimal.ZERO);
        notaFiscal.setAliquotaServico(BigDecimal.ZERO);
        notaFiscal.setTotalServicos(BigDecimal.ZERO);

        for (ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico : notaFiscal.getDeclaracaoPrestacaoServico().getItens()) {
            notaFiscal.setDescontosIncondicionais(notaFiscal.getDescontosIncondicionais().add(itemDeclaracaoServico.getDescontosIncondicionados()));
            notaFiscal.setDescontosCondicionais(notaFiscal.getDescontosCondicionais().add(itemDeclaracaoServico.getDescontosCondicionados()));
            notaFiscal.setDeducoesLegais(notaFiscal.getDeducoesLegais().add(itemDeclaracaoServico.getDeducoes()));
            notaFiscal.setAliquotaServico(notaFiscal.getAliquotaServico().add(itemDeclaracaoServico.getAliquotaServico()));
            BigDecimal valorServico = itemDeclaracaoServico.getValorServico().multiply(itemDeclaracaoServico.getQuantidade());
            notaFiscal.setTotalServicos(notaFiscal.getTotalServicos().add(valorServico));
            itemDeclaracaoServico.setBaseCalculo(itemDeclaracaoServico.getValorServico().subtract(notaFiscal.getDescontosIncondicionais()).subtract(notaFiscal.getDeducoesLegais()));
        }
        notaFiscal.setTotalNota(notaFiscal.getTotalServicos().subtract(notaFiscal.getDescontos()));
        notaFiscal.setBaseCalculo(notaFiscal.getTotalServicos().subtract(notaFiscal.getDescontosIncondicionais()).subtract(notaFiscal.getDeducoesLegais()));
        notaFiscal = calcularIss(notaFiscal);
        notaFiscal = calcularValorLiquido(notaFiscal);
        arredondarValores(notaFiscal);
        return notaFiscal;
    }

    public NotaFiscalNfseDTO calcularValorLiquido(NotaFiscalNfseDTO notaFiscal) {
        BigDecimal valorRetido = BigDecimal.ZERO;
        TributosFederaisNfseDTO tributosFederais = notaFiscal.getDeclaracaoPrestacaoServico().getTributosFederais();
        if (tributosFederais != null) {
            valorRetido = valorRetido.add(tributosFederais.getTotalRetencoes());
        }
        if (notaFiscal.getDeclaracaoPrestacaoServico().getIssRetido() &&
                !ExigibilidadeNfseDTO.TRIBUTACAO_FORA_MUNICIPIO.equals(notaFiscal.getDeclaracaoPrestacaoServico().getNaturezaOperacao())) {
            valorRetido = valorRetido.add(BigDecimalUtils.arredondar(notaFiscal.getIssCalculado(), 2));
        }
        BigDecimal subtract = BigDecimalUtils.arredondar(notaFiscal.getTotalNota().subtract(valorRetido), 2);
        notaFiscal.setValorLiquido(subtract);
        return notaFiscal;
    }

    public NotaFiscalNfseDTO calcularIss(NotaFiscalNfseDTO notaFiscal) {
        notaFiscal.setIssPagar(BigDecimal.ZERO);
        notaFiscal.setIssCalculado(BigDecimal.ZERO);
        if (notaFiscal.getDeclaracaoPrestacaoServico().getItens() != null &&
                !notaFiscal.getDeclaracaoPrestacaoServico().getItens().isEmpty()) {
            ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = notaFiscal.getDeclaracaoPrestacaoServico().getItens().stream().findFirst().get();
            if (itemDeclaracaoServico.getServico() != null) {
                notaFiscal.setIssCalculado(notaFiscal.getBaseCalculo().multiply(itemDeclaracaoServico.getAliquotaServico().divide(new BigDecimal(100), 8, RoundingMode.HALF_UP)));
                itemDeclaracaoServico.setIss(notaFiscal.getIssCalculado());
                if (ExigibilidadeNfseDTO.TRIBUTACAO_MUNICIPAL.equals(notaFiscal.getDeclaracaoPrestacaoServico().getNaturezaOperacao()) &&
                        !notaFiscal.getDeclaracaoPrestacaoServico().getIssRetido() &&
                        !notaFiscal.getPrestador().getEnquadramentoFiscal().getSuperSimples()) {
                    notaFiscal.setIssPagar(notaFiscal.getIssCalculado());
                }
            }
        }
        return notaFiscal;
    }

    public NotaFiscalNfseDTO substituirNotaFiscal(SubstituicaoNotaFiscalNfseDTO substituicao) throws ValidacaoNfseWSException {
        if (substituicao.getSubstituidora() == null) {
            throw new ValidacaoNfseWSException(TipoValidacao.W006);
        }
        if (substituicao.getSubstituida() == null) {
            throw new ValidacaoNfseWSException(TipoValidacao.W007);
        }

        PrestadorServicoNfseDTO prestador = cadastroEconomicoService.findById(substituicao.getSubstituidora().getPrestador().getId());
        substituicao.getSubstituidora().setPrestador(prestador);
        save(substituicao.getSubstituidora());

        CancelamentoNfseDTO cancelarNfseDTO = new CancelamentoNfseDTO();
        cancelarNfseDTO.setNotaFiscal(substituicao.getSubstituida());
        cancelarNfseDTO.setNotaFiscalSubstituta(substituicao.getSubstituidora());
        cancelarNfseDTO.setMotivoCancelamento(substituicao.getMotivoCancelamento());

        cancelar(cancelarNfseDTO);

        return findById(substituicao.getSubstituidora().getId());
    }
}

