package br.com.webpublico.service;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.enums.*;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.domain.dto.importacaodesif.DeclaracaoContaBancariaNfseDTO;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import br.com.webpublico.domain.enumeration.Mes;
import br.com.webpublico.repository.DeclaracaoMensalServicoJDBCRepository;
import br.com.webpublico.repository.DeclaracaoPrestacaoServicoJDBCRepository;
import br.com.webpublico.repository.mongo.EstatisticaMensalMongoRepository;
import br.com.webpublico.tributario.consultadebitos.ResultadoParcela;
import br.com.webpublico.web.rest.util.PaginationUtil;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.JRException;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeclaracaoMensalServicoService extends AbstractWPService<DeclaracaoMensalServicoNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(DeclaracaoMensalServicoService.class);

    @Autowired
    DeclaracaoMensalServicoJDBCRepository declaracaoMensalServicoJDBCRepository;
    @Autowired
    EstatisticaMensalMongoRepository estatisticaMensalMongoRepository;
    @Autowired
    CadastroEconomicoService cadastroEconomicoService;
    @Autowired
    ReportService reportService;
    @Autowired
    DeclaracaoPrestacaoServicoService declaracaoPrestacaoServicoService;
    @Autowired
    DeclaracaoPrestacaoServicoJDBCRepository declaracaoPrestacaoServicoJDBCRepository;
    @Autowired
    NotaFiscalSearchService notaFiscalSearchService;
    @Autowired
    UserService userService;
    @Autowired
    ExercicioService exercicioService;
    @Autowired
    ConfiguracaoService configuracaoService;
    @Autowired
    OpcaoPagamentoService opcaoPagamentoService;
    @Autowired
    DebitoService debitoService;
    @Autowired
    FeriadoService feriadoService;
    @Autowired
    DesifService desifService;
    @Autowired
    CacheSistemaService cacheSistemaService;
    @Autowired
    DamService damService;

    public DeclaracaoMensalServicoNfseDTO save(DeclaracaoMensalServicoNfseDTO declaracao) {
        CacheSistemaNfseDTO cacheDms = new CacheSistemaNfseDTO(TipoCacheSistemaNfseDTO.DECLARACAO_MENSAL_SERVICO,
                "Prestador: " + declaracao.getPrestador().getInscricaoMunicipal() + " Exercício: " + declaracao.getExercicio() +
                        " Mês: " + declaracao.getMes() + " Tipo Movimento: " + declaracao.getTipoMovimento());
        if (cacheSistemaService.cached(cacheDms)) {
            throw new NfseOperacaoNaoPermitidaException("Existe um encerramento em processamento para está competência," +
                    " por favor tente novamente mais tarde.");
        }
        try {
            cacheSistemaService.addCache(cacheDms);
            if (declaracao.isTodasNotasDoMes()) {
                declaracao.setNotas(notaFiscalSearchService.buscarNotasSemDeclararPorCompetencia(declaracao.getPrestador().getId(),
                        declaracao.getMes(), declaracao.getExercicio(), declaracao.getTipoMovimento(), false, 0, declaracao.getQtdNotas()));
            }
            preSaveDeclaracao(declaracao);
            validarDeclaracao(declaracao);
            return saveAndFinalize(declaracao);
        } finally {
            cacheSistemaService.removeCache(cacheDms);
        }
    }

    private void preSaveDeclaracao(DeclaracaoMensalServicoNfseDTO declaracao) {
        switch (declaracao.getTipoMovimento()) {
            case NORMAL:
            case ISS_RETIDO:
            case RETENCAO: {
                preSaveDeclaracaoNormalOrRetencao(declaracao);
                break;
            }
            case INSTITUICAO_FINANCEIRA: {
                preSaveDeclaracaoInstituicaoFinanceira(declaracao);
                break;
            }
        }
    }

    private void preSaveDeclaracaoNormalOrRetencao(DeclaracaoMensalServicoNfseDTO declaracao) {
        declaracao.setNotasDeclaradas(new ArrayList<>());
        if (declaracao.getNotas() != null) {
            for (NotaFiscalSearchDTO notaFiscalSearchDTO : declaracao.getNotas()) {
                NotaDeclaradaNfseDTO notaDeclarada = new NotaDeclaradaNfseDTO();
                notaDeclarada.setDeclaracaoPrestacaoServico(declaracaoPrestacaoServicoJDBCRepository.findById(notaFiscalSearchDTO.getId()));
                declaracao.getNotasDeclaradas().add(notaDeclarada);
            }
        }
    }

    private void preSaveDeclaracaoInstituicaoFinanceira(DeclaracaoMensalServicoNfseDTO declaracao) {
        DesifNfseDTO desif = criarDesif(declaracao);
        declaracao.setNotasDeclaradas(new ArrayList<>());
        NotaDeclaradaNfseDTO notaDeclarada = new NotaDeclaradaNfseDTO();
        notaDeclarada.setDeclaracaoPrestacaoServico(desif.getDeclaracaoPrestacaoServico());
        declaracao.getNotasDeclaradas().add(notaDeclarada);
    }

    private DesifNfseDTO criarDesif(DeclaracaoMensalServicoNfseDTO declaracao) {
        DesifNfseDTO desif = new DesifNfseDTO();
        desif.setPrestador(declaracao.getPrestador());
        DeclaracaoPrestacaoServicoNfseDTO declaracaoServico = new DeclaracaoPrestacaoServicoNfseDTO();
        declaracaoServico.setTipoDocumento(TipoDocumentoNfseDTO.DESIF);
        declaracaoServico.setNaturezaOperacao(ExigibilidadeNfseDTO.TRIBUTACAO_MUNICIPAL);
        LocalDate competencia = LocalDate.now().withYear(declaracao.getExercicio()).withMonthOfYear(declaracao.getMes());
        declaracaoServico.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.NAO_IDENTIFICADO);
        declaracaoServico.setCompetencia(competencia.monthOfYear().withMinimumValue().toDate());
        declaracaoServico.setSituacao(SituacaoDeclaracaoNfseDTO.EMITIDA);
        declaracaoServico.setDadosPessoaisPrestador(declaracao.getPrestador().getPessoa().getDadosPessoais());
        declaracaoServico.setIssRetido(false);
        declaracaoServico.setTotalServicos(declaracao.getTotalServicos());
        declaracaoServico.setDeducoesLegais(BigDecimal.ZERO);
        declaracaoServico.setBaseCalculo(BigDecimal.ZERO);
        declaracaoServico.setIssCalculado(declaracao.getTotalIss());
        declaracaoServico.setIssPagar(declaracao.getTotalIss());
        declaracaoServico.setItens(new ArrayList());
        for (DeclaracaoContaBancariaNfseDTO contaBancaria : declaracao.getContasBancarias()) {
            ItemDeclaracaoServicoNfseDTO item = new ItemDeclaracaoServicoNfseDTO();
            item.setConta(contaBancaria.getConta());
            item.setDescricao(contaBancaria.getConta().getNome());
            item.setObservacoes(contaBancaria.getConta().getDescricaoDetalhada());
            if (contaBancaria.getConta().getCodigoTributacao() != null) {
                item.setServico(contaBancaria.getConta().getCodigoTributacao().getServico());
            } else if (contaBancaria.getConta().getServico() != null) {
                item.setServico(contaBancaria.getConta().getServico());
            } else {
                throw new NfseOperacaoNaoPermitidaException("Serviço não definido para a conta " +
                        item.getConta().getConta() + " - " + item.getConta().getNome());
            }
            item.setNomeServico(item.getServico().getDescricao());
            item.setSaldoAnterior(contaBancaria.getSaldoInicial());
            item.setCredito(contaBancaria.getValorCredito());
            item.setDebito(contaBancaria.getValorDebito());
            item.setAliquotaServico(contaBancaria.getAliquotaIssqn());
            item.setBaseCalculo(contaBancaria.getBaseCalculo());
            item.setValorServico(contaBancaria.getBaseCalculo());
            item.setIss(contaBancaria.getBaseCalculo().multiply((contaBancaria.getAliquotaIssqn().divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_UP))));
            item.setDeducoes(BigDecimal.ZERO);
            item.setPrestadoNoPais(true);
            declaracaoServico.getItens().add(item);
        }
        desif.setDeclaracaoPrestacaoServico(declaracaoServico);
        desifService.inserir(desif);
        return desif;
    }

    private void validarDeclaracao(DeclaracaoMensalServicoNfseDTO declaracao) {
        validarAusenciaMovimento(declaracao);
        validarNotasDeclaradas(declaracao);
        validarCompetencia(declaracao);
    }

    private void validarCompetencia(DeclaracaoMensalServicoNfseDTO declaracao) {
        if (!TipoMovimentoMensalNfseDTO.INSTITUICAO_FINANCEIRA.equals(declaracao.getTipoMovimento())) {
            for (NotaDeclaradaNfseDTO notasDeclarada : declaracao.getNotasDeclaradas()) {
                Integer ano = DateUtils.getAno(notasDeclarada.getDeclaracaoPrestacaoServico().getCompetencia());
                Integer mes = DateUtils.getMes(notasDeclarada.getDeclaracaoPrestacaoServico().getCompetencia());
                if (ano.compareTo(declaracao.getExercicio()) != 0 ||
                        mes.compareTo(declaracao.getMes()) != 0) {
                    throw new NfseOperacaoNaoPermitidaException("O encerramento mensal contém notas fiscais de competências diferentes de " +
                            declaracao.getMes() + "/" + declaracao.getExercicio());
                }
            }
        }
        Calendar dataAtual = Calendar.getInstance();
        dataAtual.setTime(new Date());
        if (declaracao.getExercicio() >= dataAtual.get(Calendar.YEAR) &&
                declaracao.getMes() > dataAtual.get(Calendar.MONTH) + 1) {
            throw new NfseOperacaoNaoPermitidaException("Não é possível encerrar uma competência futura.");
        }
    }

    private void validarNotasDeclaradas(DeclaracaoMensalServicoNfseDTO declaracao) {
        if (TipoMovimentoMensalNfseDTO.NORMAL.equals(declaracao.getTipoMovimento()) ||
                TipoMovimentoMensalNfseDTO.RETENCAO.equals(declaracao.getTipoMovimento())) {
            for (NotaDeclaradaNfseDTO notaDeclarada : declaracao.getNotasDeclaradas()) {
                if (hasDeclaracaoMensalServico(notaDeclarada.getDeclaracaoPrestacaoServico(), declaracao.getTipoMovimento())) {
                    throw new NfseOperacaoNaoPermitidaException("O encerramento possui notas fiscais já declaradas, por favor atualize o encerramento e tente novamente.");
                }
            }
        }
    }

    private void validarAusenciaMovimento(DeclaracaoMensalServicoNfseDTO declaracao) {
        if (!declaracao.getAusenciaMovimento()) {
            return;
        }
        if (declaracaoMensalServicoJDBCRepository.hasDeclaracaoNaCompetencia(declaracao.getPrestador().getId(),
                Mes.getMesToInt(declaracao.getMes()), declaracao.getExercicio(), declaracao.getTipoMovimento())) {
            throw new NfseOperacaoNaoPermitidaException("Não é possível declarar ausência de movimentos para um período que tenha encerramento mensal.");
        }
        if (TipoMovimentoMensalNfseDTO.NORMAL.equals(declaracao.getTipoMovimento())) {
            List<NotaFiscalSearchDTO> notasNaoDeclaradas =
                    notaFiscalSearchService.buscarNotasSemDeclararPorCompetencia(declaracao.getPrestador().getId(), declaracao.getMes(), declaracao.getExercicio(),
                            declaracao.getTipoMovimento(), true, 0, 1);
            if (!notasNaoDeclaradas.isEmpty()) {
                throw new NfseOperacaoNaoPermitidaException("Não é possível declarar ausência de movimentos para um periodo que existem notas emitidas");
            }
        } else if (TipoMovimentoMensalNfseDTO.RETENCAO.equals(declaracao.getTipoMovimento())) {
            List<NotaFiscalSearchDTO> notasNaoDeclaradas = notaFiscalSearchService.buscarNotasSemDeclararPorCompetencia(declaracao.getPrestador().getId(), declaracao.getMes(), declaracao.getExercicio(),
                    declaracao.getTipoMovimento(), false, 0, 1);
            if (!notasNaoDeclaradas.isEmpty()) {
                throw new NfseOperacaoNaoPermitidaException("Não é possível declarar ausência de movimentos para um periodo que existem notas com retenção recebidas");
            }
        }
    }

    private DeclaracaoMensalServicoNfseDTO saveAndFinalize(DeclaracaoMensalServicoNfseDTO declaracao) {
        if (declaracao.getUsuarioDeclaracao() == null && userService.getUserWithAuthorities() != null) {
            declaracao.setUsuarioDeclaracao(userService.getUserWithAuthorities().getLogin());
        }
        declaracao.setSituacao(SituacaoDeclaracaoMensalServicoNfseDTO.ENCERRADO);
        declaracao.setReferencia(DateUtils.primeiroDiaMes(declaracao.getMes(), declaracao.getExercicio()));
        declaracao.setCodigo(declaracaoMensalServicoJDBCRepository.buscarUltimoNumeroDeDeclaracaoDoPrestador(declaracao.getPrestador().getId()) + 1);
        declaracao.setIdExercicio(exercicioService.getIdByAno(declaracao.getExercicio()));
        declaracao.setTipo(declaracaoMensalServicoJDBCRepository.hasDeclaracaoNaCompetencia(declaracao.getPrestador().getId(),
                Mes.getMesToInt(declaracao.getMes()), declaracao.getExercicio(), declaracao.getTipoMovimento()) ?
                TipoDeclaracaoMensalServicoNfseDTO.COMPLEMENTAR :
                TipoDeclaracaoMensalServicoNfseDTO.PRINCIPAL);
        declaracaoMensalServicoJDBCRepository.inserir(declaracao);
        DebitoNfseDTO debito = gerarDebito(declaracao);
        declaracaoMensalServicoJDBCRepository.vincularProcessoCalculo(declaracao.getId(), debito.getIdProcesso());
        return declaracao;
    }

    private DebitoNfseDTO gerarDebito(DeclaracaoMensalServicoNfseDTO declaracao) {
        ConfiguracaoNfseDTO configuracao = configuracaoService.getConfiguracaoFromServer();
        ConfiguracaoDividaNfseDTO configuracaoDivida = configuracaoService.getConfiguracaoDivida(configuracao,
                declaracao.getTipoMovimento(), declaracao.getTipo());

        if (configuracaoDivida == null) {
            throw new NfseOperacaoNaoPermitidaException("Configuração de dívida não encontrada.");
        }
        OpcaoPagamentoNfseDTO opcaoPagamento = opcaoPagamentoService.findByDivida(configuracaoDivida.getIdDivida());
        if (configuracaoDivida == null) {
            throw new NfseOperacaoNaoPermitidaException("Opção de pagamento não encontrada.");
        }

        String descricao = " Nota Fiscal Eletrônica " + declaracao.getPrestador().getInscricaoMunicipal() +
                " " + declaracao.getMes() + "/" + declaracao.getExercicio();

        Long idCadastro = declaracao.getPrestador().getId();
        Long idPessoa = declaracao.getPrestador().getPessoa().getId();

        Long subDivida = declaracaoMensalServicoJDBCRepository.contarCalculoIss(declaracao.getPrestador().getId(),
                declaracao.getPrestador().getPessoa().getId(),
                declaracao.getExercicio(), declaracao.getMes()) + 1;
        String referencia = "Exercício: " + declaracao.getExercicio() + " Mês: " + declaracao.getMes() + " Seq: " + subDivida;

        Date dataVencimento = getDataVencimentoParcela(declaracao, opcaoPagamento);

        BigDecimal aliquota = BigDecimal.ZERO;
        BigDecimal faturamento = BigDecimal.ZERO;
        BigDecimal baseCalculo = BigDecimal.ZERO;
        BigDecimal valorCalculado = BigDecimal.ZERO;
        List<ServicoDeclaracaoMensalNfseDTO> servicos = declaracaoMensalServicoJDBCRepository.buscarServicosDeclaracaoMensal(declaracao.getId());
        arredondarIssServicos(declaracao, servicos);
        List<GeradorDebitoItem> itens = new ArrayList<>();
        if (servicos != null && !servicos.isEmpty()) {
            for (ServicoDeclaracaoMensalNfseDTO servico : servicos) {
                aliquota = servico.getAliquota();
                faturamento = faturamento.add(servico.getTotalServicos());
                baseCalculo = baseCalculo.add(servico.getBaseCalculo());
                valorCalculado = valorCalculado.add(servico.getIss());
                itens.add(new GeradorDebitoItem(servico.getIdServico(), servico.getAliquota(),
                        servico.getBaseCalculo(), servico.getTotalServicos(), servico.getIss()));
            }
        }

        Boolean ausenciaMovimento = valorCalculado.compareTo(BigDecimal.ZERO) == 0;

        GeradorDebito geradorDebito = new GeradorDebito(descricao,
                declaracao.getAbertura(), declaracao.getIdExercicio(), declaracao.getMes(),
                configuracaoDivida.getIdDivida(), configuracaoDivida.getIdTributo(), idCadastro, idPessoa,
                ausenciaMovimento, subDivida, aliquota, baseCalculo, faturamento, valorCalculado, subDivida,
                dataVencimento, opcaoPagamento.getId(), referencia, itens);

        return debitoService.gravarDebito(geradorDebito.toDebito());
    }

    private void arredondarIssServicos(DeclaracaoMensalServicoNfseDTO declaracao,
                                       List<ServicoDeclaracaoMensalNfseDTO> servicos) {
        if (servicos == null || servicos.isEmpty()) {
            return;
        }
        BigDecimal totalIss = servicos.stream().map(ServicoDeclaracaoMensalNfseDTO::getIss)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalIss.compareTo(declaracao.getTotalIss()) != 0) {
            BigDecimal diferenca = totalIss.subtract(declaracao.getTotalIss());
            BigDecimal novoValor = servicos.get(0).getIss();
            novoValor = diferenca.compareTo(BigDecimal.ZERO) > 0 ? novoValor.subtract(diferenca) :
                    novoValor.add(diferenca.multiply(new BigDecimal(-1)));
            servicos.get(0).setIss(novoValor);
        }
    }

    private Date getDataVencimentoParcela(DeclaracaoMensalServicoNfseDTO declaracao, OpcaoPagamentoNfseDTO opcaoPagamento) {
        Date dataVencimento = DateUtils.getData(opcaoPagamento.getDiaVencimento(), declaracao.getMes() + 1, declaracao.getExercicio());
        return feriadoService.proximoDiaUtil(dataVencimento);
    }

    public Page<DeclaracaoMensalServicoNfseDTO> buscarDeclaracaoMensalServico(Pageable pageable,
                                                                              List<ParametroQuery> parametros,
                                                                              String orderBy) throws Exception {
        List<DeclaracaoMensalServicoNfseDTO> dtos = declaracaoMensalServicoJDBCRepository.consultarDeclaracaoMensalServico(pageable, parametros,
                orderBy);

        calcularAcrescimos(dtos);

        Integer count = declaracaoMensalServicoJDBCRepository.contarDeclaracaoMensalServico(parametros);

        return new PageImpl<>(dtos, pageable, count);
    }

    private void calcularAcrescimos(List<DeclaracaoMensalServicoNfseDTO> dtos) {
        if (dtos != null) {
            for (DeclaracaoMensalServicoNfseDTO dto : dtos) {
                List<ResultadoParcela> parcelas = debitoService.buscarParcelasPorCalculoId(dto.getIdCalculo());
                if (!parcelas.isEmpty()) {
                    for (ResultadoParcela resultado : parcelas) {
                        dto.setTotalJuros(dto.getTotalJuros().add(resultado.getValorJuros()));
                        dto.setTotalMulta(dto.getTotalMulta().add(resultado.getValorMulta()));
                        dto.setTotalCorrecao(dto.getTotalCorrecao().add(resultado.getValorCorrecao()));
                        dto.setTotalHonorarios(dto.getTotalHonorarios().add(resultado.getValorHonorarios()));
                        dto.setSituacaoDebito(resultado.getSituacaoNameEnum());
                    }
                }
            }
        }
    }


    public DeclaracaoMensalServicoNfseDTO findById(Long id) {
        DeclaracaoMensalServicoNfseDTO declaracao = declaracaoMensalServicoJDBCRepository.findById(id);
        if (declaracao.getUsuarioCancelamento() != null) {
            declaracao.setUsuarioCancelamento(userService.findByLogin(declaracao.getUsuarioCancelamento().getLogin()));
        }
        return declaracao;
    }


    public void delete(Long id) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/declaracao-mensal-servico";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("id", id)
                .toUriString();

        restTemplate.delete(url);
    }

    public ResponseEntity<List<DeclaracaoMensalServicoNfseDTO>> findByUser(Long prestadorId, Integer mes, Integer ano, String tipoMovimento, Pageable pageable) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/declaracao-mensal-servico-por-empresa";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("prestadorId", prestadorId)
                .queryParam("mes", mes)
                .queryParam("ano", ano)
                .queryParam("tipoMovimento", tipoMovimento)
                .toUriString();
        UriComponentsBuilder uriComponentsBuilder = PaginationUtil.addPageableParam(url, pageable);
        return restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, null, getResponseSearchTypeList());
    }

    public EstatisticaMensalNfseDTO buscarServicosPrestadosNaReferencia(int mes, int ano, Long prestadorId) {
        String id = mes + "" + ano + "" + prestadorId;
        EstatisticaMensalNfseDTO one = null;
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            one = estatisticaMensalMongoRepository.findById(id).orElse(null);
        }
        if (one == null) {
            one = declaracaoMensalServicoJDBCRepository.buscarServicosPrestadosNaReferencia(mes, ano, prestadorId);
            one.setId(id);
            if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
                estatisticaMensalMongoRepository.save(one);
            }
        }
        return one;
    }

    public ResponseEntity<ReferenciaPeriodoNfseDTO[]> buscarMesesNaoDeclaradosDoPrestador(Long prestadorId) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/meses-nao-declarados";
        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("prestadorId", prestadorId)
                .toUriString();
        return restTemplate.exchange(url,
                HttpMethod.GET, null, ReferenciaPeriodoNfseDTO[].class);
    }

    public void imprimirDam(HttpServletResponse response, Long idDeclaracao) {
            Long idCalculoDeclaracao = declaracaoMensalServicoJDBCRepository.buscarIdCalculoDeclaracao(idDeclaracao);
            List<ResultadoParcela> parcelas = debitoService.buscarParcelasPorCalculoId(idCalculoDeclaracao);
            damService.imprimirDam(parcelas.stream().map(ResultadoParcela::getIdParcela).collect(Collectors.toList()),
                    response);
    }

    public byte[] gerarBytesRelatorioDms(TipoMovimentoMensalNfseDTO tipoMovimento,
                                         List<RelatorioDeclaracaoMensalServicosNfseDTO> dados) {
        byte[] bytes = new byte[0];
        try {
            HashMap<String, Object> parametros = new HashMap<>();
            reportService.parametrosDefault(parametros);
            parametros.put("TITULO", "RELATÓRIO ANALÍTICO DE ENCERRAMENTO MENSAL DE SERVIÇOS "
                    + (TipoMovimentoMensalNfseDTO.RETENCAO.equals(tipoMovimento) ? "TOMADOS" : "PRESTADOS"));
            parametros.put("FILTROS", "Todos.");
            return reportService.gerarRelatorio("RelatorioDeclaracaoMensalServicos.jrxml", parametros, dados, false);
        } catch (Exception e) {
            log.error("Erro ao gerar o relatorio de notas fiscais {}", e);
        }
        return bytes;
    }

    public List<RelatorioDeclaracaoMensalServicosNfseDTO> buscarDadosDoRelatorioDeclaracaoMensalServico(Long idDms) {
        List<RelatorioDeclaracaoMensalServicosNfseDTO> dados = declaracaoMensalServicoJDBCRepository
                .buscarDadosDoRelatorioDeclaracaoMensalServico(idDms);
        if (dados != null) {
            for (RelatorioDeclaracaoMensalServicosNfseDTO dado : dados) {
                calcularAcrescimosRelatorioDms(dado);
            }
        }
        return dados;
    }

    public List<RelatorioDeclaracaoMensalServicosNfseDTO> buscarDadosDoRelatorioDeclaracaoMensalServico(Long idPrestador,
                                                                                                        Integer ano,
                                                                                                        Integer mes,
                                                                                                        String tipoMovimento) {
        List<RelatorioDeclaracaoMensalServicosNfseDTO> dados = declaracaoMensalServicoJDBCRepository
                .buscarDadosDoRelatorioDeclaracaoMensalServico(idPrestador, ano, mes, tipoMovimento);
        if (dados != null) {
            for (RelatorioDeclaracaoMensalServicosNfseDTO dado : dados) {
                calcularAcrescimosRelatorioDms(dado);
            }
        }
        return dados;
    }

    private void calcularAcrescimosRelatorioDms(RelatorioDeclaracaoMensalServicosNfseDTO dado) {
        List<ResultadoParcela> parcelas = debitoService.buscarParcelasPorCalculoId(dado.getIdCalculo());
        if (!parcelas.isEmpty()) {
            for (ResultadoParcela resultado : parcelas) {
                dado.setSituacaoParcela(resultado.getSituacaoDescricaoEnum());
                dado.setDataVencimentoIss(resultado.getVencimento());
                dado.setValorTotalJuros(dado.getValorTotalJuros().add(resultado.getValorJuros()));
                dado.setValorTotalMulta(dado.getValorTotalMulta().add(resultado.getValorMulta()));
                dado.setValorTotalCorrecao(dado.getValorTotalCorrecao().add(resultado.getValorCorrecao()));
            }
        }
    }

    private void escreverResponse(HttpServletResponse response, byte[] bytes, String fileName) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        response.setContentLength(bytes.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes, 0, bytes.length);
        outputStream.flush();
        outputStream.close();
    }

    public void imprimirRelatorioDms(HttpServletResponse response, Long idDms) {
        try {
            DeclaracaoMensalServicoNfseDTO declaracaoMensalServico = findById(idDms);
            byte[] bytes = gerarBytesRelatorioDms(declaracaoMensalServico.getTipoMovimento(),
                    buscarDadosDoRelatorioDeclaracaoMensalServico(idDms));
            escreverResponse(response, bytes, "RelatorioDMS.pdf");
        } catch (Exception e) {
            log.error("Não foi possível imprimir o dam ", e);
        }
    }

    public void imprimirRelatorioDms(HttpServletResponse response, Long prestadorId, int ano, int mes, String tipoMovimento) {
        try {
            TipoMovimentoMensalNfseDTO tipoMovimentoMensal = TipoMovimentoMensalNfseDTO.valueOf(tipoMovimento);
            byte[] bytes = gerarBytesRelatorioDms(tipoMovimentoMensal,
                    buscarDadosDoRelatorioDeclaracaoMensalServico(prestadorId, ano, mes, tipoMovimento));
            escreverResponse(response, bytes, "RelatorioDMS-" + ano + "-" + mes + ".pdf");
        } catch (Exception e) {
            log.error("Não foi possível imprimir o dam ", e);
        }
    }


    @Override
    public String getTableName() {
        return "DeclaracaoMensalServico";
    }

    @Override
    public String getDefaltSearchFields() {
        return "codigo, competencia, tipo";
    }

    @Override
    public ParameterizedTypeReference<List<DeclaracaoMensalServicoNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<DeclaracaoMensalServicoNfseDTO>>() {
        };
    }

    public ParameterizedTypeReference<List<DeclaracaoMensalServicoNfseDTO>> getResponseSearchTypeList() {
        return new ParameterizedTypeReference<List<DeclaracaoMensalServicoNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<DeclaracaoMensalServicoNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<DeclaracaoMensalServicoNfseDTO>() {
        };
    }

    private void validarPrazoCancelamentoDeclaracao(DeclaracaoMensalServicoNfseDTO declaracao) {
        ConfiguracaoNfseDTO configuracaoNfse = configuracaoService.getConfiguracaoFromServer();
        Integer diaLimiteCancelamento = configuracaoNfse.getDiaLimiteCancelamentoEncerramentoMensal();
        if (diaLimiteCancelamento != null) {
            Calendar dataLimite = Calendar.getInstance();
            dataLimite.set(Calendar.YEAR, declaracao.getExercicio());
            dataLimite.set(Calendar.MONTH, declaracao.getMes());
            dataLimite.set(Calendar.DAY_OF_MONTH, diaLimiteCancelamento);
            Date dataLimiteFinal = feriadoService.proximoDiaUtil(dataLimite.getTime());
            if (DateUtils.dataSemHorario(dataLimiteFinal).before(DateUtils.dataSemHorario(new Date()))) {
                throw new NfseOperacaoNaoPermitidaException("O prazo para cancelamento do encerramento mensal foi excedido.");
            }
        }
    }

    private void validarCancelamentoDeclaracao(DeclaracaoMensalServicoNfseDTO declaracao) {
        validarPrazoCancelamentoDeclaracao(declaracao);
        if ("PAGO".equals(declaracao.getSituacaoDebito())) {
            throw new NfseOperacaoNaoPermitidaException("A guia do encerramento mensal já está paga. Por esse motivo a mesma não pode ser cancelada.");
        }
    }

    public DeclaracaoMensalServicoNfseDTO cancelar(Long id) {
        DeclaracaoMensalServicoNfseDTO declaracao = findById(id);
        validarCancelamentoDeclaracao(declaracao);
        declaracao.setDataCancelamento(new Date());
        declaracao.setUsuarioCancelamento(userService.getUserWithAuthorities());
        declaracao.setSituacao(SituacaoDeclaracaoMensalServicoNfseDTO.CANCELADO);
        debitoService.cancelarDebito(declaracao.getIdCalculo());
        declaracaoMensalServicoJDBCRepository.cancelar(declaracao);
        return findById(declaracao.getId());
    }

    public void imprimeComposicaoGuiaNfse(HttpServletResponse response,
                                          Long prestadorId,
                                          Long damId) {
        try {
            byte[] bytes = gerarBytesComposicaoGuiaNfse(prestadorId, damId);
            escreverResponse(response, bytes, "ComposicaoGuiaNfse.pdf");
        } catch (Exception e) {
            log.error("Não foi possível gerar o relatório de composição da guia da nfse {}", e);
        }
    }

    private byte[] gerarBytesComposicaoGuiaNfse(Long prestadorId, Long damId) throws JRException, IOException {
        PrestadorServicoNfseDTO prestador = cadastroEconomicoService.findById(prestadorId);
        GuiaNfseDTO guia = declaracaoMensalServicoJDBCRepository.buscarGuiaNfse(damId);
        if (guia != null) {
            guia.setNotasFiscais(declaracaoMensalServicoJDBCRepository.buscarNotasFiscaisGuiaNfse(damId));
        }
        HashMap<String, Object> parametros = new HashMap<>();
        reportService.parametrosDefault(parametros);
        parametros.put("CPFCNPJ", prestador.getPessoa().getDadosPessoais().getCpfCnpj());
        parametros.put("NOMERAZAOSOCIAL", prestador.getPessoa().getDadosPessoais().getNomeRazaoSocial());
        parametros.put("SUBREPORT_NOTAS", reportService.compilarJrxml("RelatorioComposicaoGuiaNfse_notas.jrxml"));
        return reportService.gerarRelatorio("RelatorioComposicaoGuiaNfse.jrxml", parametros, Lists.newArrayList(guia), false);
    }

    public boolean hasDeclaracaoMensalServico(DeclaracaoPrestacaoServicoNfseDTO declaracao) {
        return declaracaoMensalServicoJDBCRepository.hasDeclaracaoMensalServico(declaracao);
    }

    public boolean hasDeclaracaoMensalServico(DeclaracaoPrestacaoServicoNfseDTO declaracao, TipoMovimentoMensalNfseDTO tipoMovimento) {
        return declaracaoMensalServicoJDBCRepository.hasDeclaracaoMensalServico(declaracao, tipoMovimento);
    }

    public DeclaracaoMensalServicoNfseDTO findByCalculoId(Long calculoId) {
        return declaracaoMensalServicoJDBCRepository.findByCalculoId(calculoId);
    }

    public ResumoDMSDTO buscarResumoPorDMS(Long idDMS) {
        return notaFiscalSearchService.buscarResumoPorDMS(idDMS);
    }
}

