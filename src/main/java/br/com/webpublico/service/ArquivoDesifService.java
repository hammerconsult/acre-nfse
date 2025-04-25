package br.com.webpublico.service;

import br.com.webpublico.BigDecimalUtils;
import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.desif.CabecalhoArquivoCosifDTO;
import br.com.webpublico.domain.dto.desif.DetalheArquivoCosifDTO;
import br.com.webpublico.domain.dto.desif.UploadCosifDTO;
import br.com.webpublico.domain.dto.enums.*;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.domain.dto.importacaodesif.*;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ArquivoDesifJDBCRepository;
import br.com.webpublico.util.Util;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mifmif.common.regex.Generex;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;

@Transactional
@Service
public class ArquivoDesifService extends AbstractService<ArquivoDesifNfseDTO> {

    private static final Logger log = LoggerFactory.getLogger(ArquivoDesifService.class);
    private static final String FORMAT_ANO_MES = "yyyyMM";
    private static final String FORMAT_ANO_MES_DIA = "yyyyMMdd";
    private DecimalFormat decimalFormat;

    @Autowired
    ArquivoDesifJDBCRepository repository;
    @Autowired
    ArquivoDesifRegistro0100Service arquivoDesifRegistro0100Service;
    @Autowired
    ArquivoDesifRegistro0200Service arquivoDesifRegistro0200Service;
    @Autowired
    ArquivoDesifRegistro0300Service arquivoDesifRegistro0300Service;
    @Autowired
    ArquivoDesifRegistro0400Service arquivoDesifRegistro0400Service;
    @Autowired
    ArquivoDesifRegistro0410Service arquivoDesifRegistro0410Service;
    @Autowired
    ArquivoDesifRegistro0430Service arquivoDesifRegistro0430Service;
    @Autowired
    ArquivoDesifRegistro0440Service arquivoDesifRegistro0440Service;
    @Autowired
    ArquivoDesifRegistro1000Service arquivoDesifRegistro1000Service;
    @Autowired
    ConfiguracaoService configuracaoService;
    @Autowired
    TipoInstituicaoFinanceiraService tipoInstituicaoFinanceiraService;
    @Autowired
    CidadeService cidadeService;
    @Autowired
    CadastroEconomicoService cadastroEconomicoService;
    @Autowired
    CosifService cosifService;
    @Autowired
    CodigoTributacaoService codigoTributacaoService;
    @Autowired
    TarifaBancariaService tarifaBancariaService;
    @Autowired
    ProdutoServicoBancarioService produtoServicoBancarioService;
    @Autowired
    PlanoGeralContasComentadoService planoGeralContasComentadoService;
    @Autowired
    EventoContabilDesifService eventoContabilDesifService;
    @Autowired
    TipoDependenciaDesifService tipoDependenciaDesifService;
    @Autowired
    DeclaracaoMensalServicoService declaracaoMensalServicoService;
    @Autowired
    UserService userService;
    @Autowired
    CodigoTributacaoMunicipalService codigoTributacaoMunicipalService;
    @Autowired
    ReportService reportService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif == null)
            return;
        if (arquivoDesif.getPrestador() != null) {
            arquivoDesif.setPrestador(cadastroEconomicoService.findById(arquivoDesif.getPrestador().getId()));
        }
        if (arquivoDesif.getMunicipio() != null) {
            arquivoDesif.setMunicipio(cidadeService.findById(arquivoDesif.getMunicipio().getId()));
        }
        if (arquivoDesif.getTipoInstituicaoFinanceira() != null) {
            arquivoDesif.setTipoInstituicaoFinanceira(tipoInstituicaoFinanceiraService.findById(arquivoDesif.getTipoInstituicaoFinanceira().getId()));
        }
    }

    public ImportacaoArquivoDesifNfseDTO validarImportacaoArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao) {
        ConfiguracaoNfseDTO configuracao = configuracaoService.getConfiguracaoFromServer();
        ArquivoDesifNfseDTO arquivoDesif = lerAndValidarLeiauteArquivoDesif(importacao);
        arquivoDesif.setEnviadoPor(userService.getUserWithAuthorities());
        arquivoDesif.setEnviadoEm(new Date());
        arquivoDesif.setSituacao(SituacaoArquivoDesifNfseDTO.AGUARDANDO);
        if (importacao.getValidacoes().isEmpty()) {
            validarConsistenciaArquivoDesif(configuracao, importacao, arquivoDesif);
            if (importacao.getValidacoes().isEmpty()) {
                save(arquivoDesif);
                importacao.setIdArquivo(arquivoDesif.getId());
            }
        }
        return importacao;
    }

    private void validarConsistenciaArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                 ImportacaoArquivoDesifNfseDTO importacao,
                                                 ArquivoDesifNfseDTO arquivoDesif) {
        validarConsistenciaHeaderArquivoDesif(configuracao, importacao, arquivoDesif);
        if (importacao.getValidacoes().isEmpty()) {
            validarConsistenciaDetailArquivoDesif(configuracao, importacao, arquivoDesif);
        }
    }

    private void validarConsistenciaDetailArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                       ImportacaoArquivoDesifNfseDTO importacao,
                                                       ArquivoDesifNfseDTO arquivoDesif) {
        switch (arquivoDesif.getModulo()) {
            case MODULO_1: {
                validarConsistenciaModulo1ArquivoDesif(configuracao, importacao, arquivoDesif);
                break;
            }
            case MODULO_2: {
                validarConsistenciaModulo2ArquivoDesif(configuracao, importacao, arquivoDesif);
                break;
            }
            case MODULO_3: {
                validarConsistenciaModulo3ArquivoDesif(configuracao, importacao, arquivoDesif);
                break;
            }
            case MODULO_4: {
                validarConsistenciaModulo4ArquivoDesif(configuracao, importacao, arquivoDesif);
                break;
            }
        }
    }

    private void validarConsistenciaModulo3ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                        ImportacaoArquivoDesifNfseDTO importacao,
                                                        ArquivoDesifNfseDTO arquivoDesif) {
        validarConsistenciaRegistros0100ArquivoDesif(configuracao, importacao, arquivoDesif);
        validarConsistenciaRegistros0200ArquivoDesif(configuracao, importacao, arquivoDesif);
        validarConsistenciaRegistros0300ArquivoDesif(configuracao, importacao, arquivoDesif);
    }

    private void validarConsistenciaRegistros0100ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                              ImportacaoArquivoDesifNfseDTO importacao,
                                                              ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif.getRegistros0100() == null) {
            return;
        }
        validarGruposObrigatoriosCosif(configuracao, importacao, arquivoDesif.getRegistros0100());
        for (ArquivoDesifRegistro0100NfseDTO registro0100 : arquivoDesif.getRegistros0100()) {
            validarConsistenciaRegistro0100ArquivoDesif(importacao, arquivoDesif, registro0100);
        }
    }

    private void validarGruposObrigatoriosCosif(ConfiguracaoNfseDTO configuracao,
                                                ImportacaoArquivoDesifNfseDTO importacao,
                                                List<ArquivoDesifRegistro0100NfseDTO> registros0100) {
        List<String> gruposObrigatoriosCosif = configuracao.getGruposObrigatoriosCosif();
        if (gruposObrigatoriosCosif == null || gruposObrigatoriosCosif.isEmpty()) {
            return;
        }
        List<CosifNfseDTO> contasCosifObrigatorias = Lists.newArrayList();
        gruposObrigatoriosCosif.forEach(grupo -> {
            List<CosifNfseDTO> byGrupo = cosifService.findByGrupo(grupo);
            if (byGrupo != null && !byGrupo.isEmpty()) {
                contasCosifObrigatorias.addAll(byGrupo);
            }
        });
        if (!contasCosifObrigatorias.isEmpty()) {
            List<String> contasCosifNaoInformadas = Lists.newArrayList();
            contasCosifObrigatorias.forEach(contaCosif -> {
                if (registros0100.stream()
                        .noneMatch(registro0100 -> registro0100.getCosif().getConta().equals(contaCosif.getConta()))) {
                    contasCosifNaoInformadas.add(contaCosif.getConta());
                }
            });
            if (!contasCosifNaoInformadas.isEmpty()) {
                contasCosifNaoInformadas.sort(Comparator.naturalOrder());
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.W003, StringUtils.join(contasCosifNaoInformadas, ", "));
            }
        }
    }

    private CodigoTributacaoNfseDTO validarEG011AndEG047(ImportacaoArquivoDesifNfseDTO importacao,
                                                         ArquivoDesifNfseDTO arquivoDesif,
                                                         Long linha,
                                                         CodigoTributacaoNfseDTO codigoTributacao) {
        CodigoTributacaoNfseDTO byCodigo = codigoTributacaoService.findByCodigo(codigoTributacao.getCodigo());
        if (byCodigo == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG011, "Linha: " + linha +
                    " Campo: Cod_Trib_DES-IF Conteúdo: " + codigoTributacao.getCodigo());
        } else {
            CodigoTributacaoMunicipalNfseDTO codigoTributacaoMunicipal = codigoTributacaoMunicipalService.findByCodigoTributacaoAndVigencia(byCodigo,
                    arquivoDesif.getInicioCompetencia(), arquivoDesif.getFimCompetencia());
            if (codigoTributacaoMunicipal == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG047, "Linha: " + linha +
                        " Campo: Cod_Trib_DES-IF Conteúdo: " + codigoTributacao.getCodigo());
            } else {
                return byCodigo;
            }
        }
        return null;
    }

    private void validarEM046ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                          String campo,
                                          Long linha,
                                          CodigoTributacaoNfseDTO codigoTributacao,
                                          BigDecimal aliquota) {
        CodigoTributacaoMunicipalNfseDTO codigoTributacaoMunicipal =
                codigoTributacaoMunicipalService.findByCodigoTributacaoAndAliquota(codigoTributacao, aliquota);
        if (codigoTributacaoMunicipal == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EM046, "Linha: " + linha +
                    " Campo: " + campo + " Conteúdo: " + aliquota);
        }
    }

    private void validarConsistenciaRegistro0100ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                                             ArquivoDesifNfseDTO arquivoDesif,
                                                             ArquivoDesifRegistro0100NfseDTO registro0100) {
        CosifNfseDTO cosif = cosifService.findByConta(registro0100.getCosif().getConta());
        if (cosif == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG036, "Linha: " + registro0100.getLinha() +
                    " Campo: Conta_COSIF Conteúdo: " + registro0100.getCosif().getConta());
        } else {
            registro0100.setCosif(cosif);
        }
        registro0100.setCodigoTributacao(validarEG011AndEG047(importacao, arquivoDesif,
                registro0100.getLinha(), registro0100.getCodigoTributacao()));
    }

    private void validarConsistenciaRegistros0200ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                              ImportacaoArquivoDesifNfseDTO importacao,
                                                              ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif.getRegistros0200() != null) {
            for (ArquivoDesifRegistro0200NfseDTO registro0200 : arquivoDesif.getRegistros0200()) {
                validarConsistenciaRegistro0200ArquivoDesif(importacao, registro0200);
            }
        }
    }

    private void validarConsistenciaRegistro0200ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                                             ArquivoDesifRegistro0200NfseDTO registro0200) {
        TarifaBancariaNfseDTO tarifaBancaria = tarifaBancariaService.findByCodigo(registro0200.getTarifaBancaria().getCodigo().toString());
        if (tarifaBancaria == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EI037,
                    "Linha: " + registro0200.getLinha() +
                            " Campo: Idto_Tari Conteúdo: " + registro0200.getTarifaBancaria().getCodigo());
        } else {
            registro0200.setTarifaBancaria(tarifaBancaria);
        }
    }

    private void validarConsistenciaRegistros0300ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                              ImportacaoArquivoDesifNfseDTO importacao,
                                                              ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif.getRegistros0300() != null) {
            for (ArquivoDesifRegistro0300NfseDTO registro0300 : arquivoDesif.getRegistros0300()) {
                validarConsistenciaRegistro0300ArquivoDesif(importacao, registro0300);
            }
        }
    }

    private void validarConsistenciaRegistro0300ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                                             ArquivoDesifRegistro0300NfseDTO registro0300) {
        ProdutoServicoBancarioNfseDTO produtoServicoBancario = produtoServicoBancarioService
                .findByCodigo(registro0300.getProdutoServicoBancario().getCodigo().toString());
        if (produtoServicoBancario == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EI017,
                    "Linha: " + registro0300.getLinha() +
                            " Campo: Idto_Serv Conteúdo: " + registro0300.getProdutoServicoBancario().getCodigo());
        } else {
            registro0300.setProdutoServicoBancario(produtoServicoBancario);
        }
    }

    private void validarConsistenciaHeaderArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                       ImportacaoArquivoDesifNfseDTO importacao,
                                                       ArquivoDesifNfseDTO arquivoDesif) {
        PrestadorServicoNfseDTO prestador = cadastroEconomicoService.findById(arquivoDesif.getPrestador().getId());
        if (arquivoDesif.getTipoInstituicaoFinanceira() != null) {
            TipoInstituicaoFinanceiraNfseDTO tipoInstituicaoFinanceira =
                    tipoInstituicaoFinanceiraService.findByCodigo(arquivoDesif.getTipoInstituicaoFinanceira().getCodigo());
            if (tipoInstituicaoFinanceira == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED003, "Linha: 1 Conteúdo: " + arquivoDesif.getTipoInstituicaoFinanceira().getCodigo());
            } else {
                arquivoDesif.setTipoInstituicaoFinanceira(tipoInstituicaoFinanceira);
            }
        }
        if (arquivoDesif.getMunicipio() != null) {
            MunicipioNfseDTO municipio = cidadeService.findByCodigoIBGE(arquivoDesif.getMunicipio().getCodigoIbge());
            if (municipio == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG001, "Linha: 1 Conteúdo: " + arquivoDesif.getMunicipio().getCodigoIbge());
            } else if (!municipio.getId().equals(configuracao.getMunicipio().getId())) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED059, "Linha: 1 Conteúdo: " + arquivoDesif.getMunicipio().getCodigoIbge());
            } else {
                arquivoDesif.setMunicipio(municipio);
            }
        }
        validarED074ArquivoDesif(importacao, arquivoDesif, prestador);
        validarED052ArquivoDesif(importacao, arquivoDesif);
        validarED054ArquivoDesif(importacao, arquivoDesif);
        validarED023ArquivoDesif(importacao, arquivoDesif);
        validarED075ArquivoDesif(importacao, arquivoDesif, prestador);
    }

    private ArquivoDesifNfseDTO lerAndValidarLeiauteArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao) {
        ArquivoDesifNfseDTO arquivoDesif = new ArquivoDesifNfseDTO();
        arquivoDesif.setPrestador(importacao.getPrestador());
        String arquivo = Util.getBase64ToString(importacao.getFile());
        try {
            InputStream inputStream = Util.getBase64ToInputStream(arquivo);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String conteudo;
            Long ordem = 0l;
            while ((conteudo = reader.readLine()) != null) {
                if (conteudo.isEmpty()) {
                    continue;
                }
                ordem++;
                if (ordem == 1) {
                    lerAndValidarHeaderArquivoDesif(importacao, arquivoDesif, ordem, conteudo);
                    if (!importacao.getValidacoes().isEmpty()) {
                        break;
                    }
                } else {
                    lerAndValidarDetailArquivoDesif(importacao, arquivoDesif, ordem, conteudo);
                }
            }
            reader.close();
            streamReader.close();
            inputStream.close();
            if (ordem == 0) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG018, null);
            }
            return arquivoDesif;
        } catch (Exception e) {
            log.error("Erro ao ler arquivo desif: ", e);
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG019, null);
        }
        return arquivoDesif;
    }

    private void lerAndValidarHeaderArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao, ArquivoDesifNfseDTO arquivoDesif,
                                                 Long ordem, String conteudo) {
        String[] campos = conteudo.split("\\|", -1);
        if (!validarEG014ArquivoDesif(importacao, ordem, campos.length, 15)) {
            return;
        }

        String linhaString = campos[0];
        Long linha = 0L;
        if (validarEG009ArquivoDesif(importacao, ordem, linhaString, "Num_Linha", 8, false) &&
                validarEG008ArquivoDesif(importacao, ordem, linhaString, "Num_Linha", Long.class) &&
                validarEG013ArquivoDesif(importacao, ordem, new Long(linhaString))) {
            linha = new Long(linhaString);
        }

        if (!"0000".equals(campos[1])) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED035, "");
            return;
        }

        String modulo = campos[8];
        if (validarEG046ArquivoDesif(importacao, linha, modulo, "Modu_Decl") &&
                validarEG009ArquivoDesif(importacao, linha, modulo, "Modu_Decl", 1, true) &&
                validarEG008ArquivoDesif(importacao, linha, modulo, "Modu_Decl", Integer.class)) {
            ModuloDesifNfseDTO moduloByCodigo = ModuloDesifNfseDTO.findByCodigo(new Integer(modulo));
            arquivoDesif.setModulo(moduloByCodigo);
            if (moduloByCodigo == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED015, "Linha: " + linha +
                        " Campo: Modu_Decl Conteúdo: " + modulo);
            }
        } else {
            return;
        }

        String cnpj = campos[2];
        if (validarEG046ArquivoDesif(importacao, linha, cnpj, "CNPJ") &&
                validarEG009ArquivoDesif(importacao, linha, cnpj, "CNPJ", 8, true) &&
                validarW001ArquivoDesif(importacao, linha, cnpj, "CNPJ")) {
            arquivoDesif.setPrestador(importacao.getPrestador());
        }

        String codigoTipoInstituicao = campos[4];
        if (validarEG046ArquivoDesif(importacao, linha, codigoTipoInstituicao, "Tipo_Inti") &&
                validarEG009ArquivoDesif(importacao, linha, codigoTipoInstituicao, "Tipo_Inti", 1, true)) {
            arquivoDesif.setTipoInstituicaoFinanceira(new TipoInstituicaoFinanceiraNfseDTO());
            arquivoDesif.getTipoInstituicaoFinanceira().setCodigo(codigoTipoInstituicao);
        }

        String codigoMunicipio = campos[5];
        validarEG046ArquivoDesif(importacao, linha, codigoMunicipio, "Cod_Munc");
        validarEG009ArquivoDesif(importacao, linha, codigoMunicipio, "Cod_Munc", 7, true);
        if (validarEG008ArquivoDesif(importacao, linha, codigoMunicipio, "Cod_Munc", Long.class)) {
            arquivoDesif.setMunicipio(new MunicipioNfseDTO());
            arquivoDesif.getMunicipio().setCodigoIbge(codigoMunicipio);
        }

        String inicioCompetencia = campos[6];
        if (validarEG046ArquivoDesif(importacao, linha, inicioCompetencia, "Ano_Mes_Inic_Cmpe") &&
                validarEG009ArquivoDesif(importacao, linha, inicioCompetencia, "Ano_Mes_Inic_Cmpe", 6, true) &&
                validarEG007ArquivoDesif(importacao, linha, inicioCompetencia, FORMAT_ANO_MES, "Ano_Mes_Inic_Cmpe") &&
                validarED005ArquivoDesif(importacao, linha, DateUtils.getData(inicioCompetencia, FORMAT_ANO_MES), "Ano_Mes_Inic_Cmpe")) {
            arquivoDesif.setInicioCompetencia(DateUtils.getData(inicioCompetencia, FORMAT_ANO_MES));
            arquivoDesif.setInicioCompetencia(DateUtils.primeiroDiaMes(arquivoDesif.getInicioCompetencia()).getTime());
        }

        String fimCompetencia = campos[7];
        if (validarEG046ArquivoDesif(importacao, linha, fimCompetencia, "Ano_Mes_Fim_Cmpe") &&
                validarEG009ArquivoDesif(importacao, linha, fimCompetencia, "Ano_Mes_Fim_Cmpe", 6, true) &&
                validarEG007ArquivoDesif(importacao, linha, fimCompetencia, FORMAT_ANO_MES, "Ano_Mes_Fim_Cmpe") &&
                validarED005ArquivoDesif(importacao, linha, DateUtils.getData(fimCompetencia, FORMAT_ANO_MES), "Ano_Mes_Fim_Cmpe") &&
                validarED004ArquivoDesif(importacao, linha, DateUtils.getData(fimCompetencia, FORMAT_ANO_MES), "Ano_Mes_Fim_Cmpe")) {
            arquivoDesif.setFimCompetencia(DateUtils.getData(fimCompetencia, FORMAT_ANO_MES));
            arquivoDesif.setFimCompetencia(DateUtils.getUltimoDiaMes(arquivoDesif.getFimCompetencia()).getTime());
        }

        String tipoDeclaracao = campos[9];
        if (validarEG046ArquivoDesif(importacao, linha, tipoDeclaracao, "Tipo_Decl") &&
                validarEG009ArquivoDesif(importacao, linha, tipoDeclaracao, "Tipo_Decl", 1, true) &&
                validarEG008ArquivoDesif(importacao, linha, tipoDeclaracao, "Tipo_Decl", Integer.class)) {
            TipoDesifNfseDTO tipoDesifByCodigo = TipoDesifNfseDTO.findByCodigo(new Integer(tipoDeclaracao));
            arquivoDesif.setTipo(tipoDesifByCodigo);
            if (tipoDesifByCodigo == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED006, "Linha: " + linha +
                        " Campo: Tipo_Decl Conteúdo: " + tipoDeclaracao);
            }
        }

        String protocolo = campos[10];
        if (validarEG009ArquivoDesif(importacao, linha, protocolo, "Prtc_Decl_Ante", 30, false)) {
            arquivoDesif.setProtocolo(protocolo);
        }

        String tipoConsolidacao = campos[11];
        if (validarEG046ArquivoDesif(importacao, linha, tipoConsolidacao, "Tipo_Cnso") &&
                validarEG009ArquivoDesif(importacao, linha, tipoConsolidacao, "Tipo_Cnso", 1, true) &&
                validarEG008ArquivoDesif(importacao, linha, tipoConsolidacao, "Tipo_Cnso", Integer.class)) {
            TipoConsolidacaoDesifNfseDTO tipoConsolidacaoByCodigo = TipoConsolidacaoDesifNfseDTO.findByCodigo(new Integer(tipoConsolidacao));
            arquivoDesif.setTipoConsolidacao(tipoConsolidacaoByCodigo);
            if (tipoConsolidacaoByCodigo == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED031, "Linha: " + linha + "Campo: Tipo_Cnso" +
                        " Conteúdo: " + tipoConsolidacao);
            }
        }

        String cpfCnpjResponsavel = campos[12];
        if (validarEG009ArquivoDesif(importacao, linha, cpfCnpjResponsavel, "CNPJ_Resp_Rclh", 6, true)) {
            arquivoDesif.setCnpjResponsavel(cpfCnpjResponsavel);
        }

        String versao = campos[13];
        arquivoDesif.setVersao(versao);

        String tipoArredondamento = campos[14];
        if (validarEG046ArquivoDesif(importacao, linha, tipoArredondamento, "Tipo_Arred") &&
                validarEG009ArquivoDesif(importacao, linha, tipoArredondamento, "Tipo_Arred", 1, true) &&
                validarEG008ArquivoDesif(importacao, linha, tipoArredondamento, "Tipo_Arred", Integer.class)) {
            TipoArredondamentoDesifNfseDTO tipoArredondamentoByCodigo = TipoArredondamentoDesifNfseDTO.findByCodigo(new Integer(tipoArredondamento));
            arquivoDesif.setTipoArredondamento(tipoArredondamentoByCodigo);
            if (tipoArredondamentoByCodigo == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED045, "Linha: " + linha +
                        "Campo: Tipo_Arred Conteúdo: " + tipoArredondamento);
            }
        }
    }

    private boolean validarED052ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif) {
        if (DateUtils.getAno(arquivoDesif.getInicioCompetencia())
                .compareTo(DateUtils.getAno(arquivoDesif.getFimCompetencia())) != 0) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED052, "Conteúdo: " +
                    DateUtils.getDataFormatada(arquivoDesif.getInicioCompetencia(), FORMAT_ANO_MES) +
                    " " + DateUtils.getDataFormatada(arquivoDesif.getFimCompetencia(), FORMAT_ANO_MES));
            return false;
        }
        return true;
    }

    private boolean validarED054ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif) {
        if (DateUtils.dataSemHorario(arquivoDesif.getFimCompetencia()).before(
                DateUtils.dataSemHorario(arquivoDesif.getInicioCompetencia()))) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED054, "Conteúdo: " +
                    DateUtils.getDataFormatada(arquivoDesif.getInicioCompetencia(), FORMAT_ANO_MES) +
                    " " + DateUtils.getDataFormatada(arquivoDesif.getFimCompetencia(), FORMAT_ANO_MES));
            return false;
        }
        return true;
    }

    private boolean validarED023ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif) {
        if (ModuloDesifNfseDTO.MODULO_2.equals(arquivoDesif.getModulo())) {
            int anoInicio = DateUtils.getAno(arquivoDesif.getInicioCompetencia());
            int mesInicio = DateUtils.getMes(arquivoDesif.getInicioCompetencia());
            int anoFim = DateUtils.getAno(arquivoDesif.getFimCompetencia());
            int mesFim = DateUtils.getMes(arquivoDesif.getFimCompetencia());
            if (anoInicio != anoFim || mesInicio != mesFim) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED023, "Conteúdo: " +
                        DateUtils.getDataFormatada(arquivoDesif.getInicioCompetencia(), FORMAT_ANO_MES) +
                        " " + DateUtils.getDataFormatada(arquivoDesif.getFimCompetencia(), FORMAT_ANO_MES));
                return false;
            }
        }
        return true;
    }

    private boolean validarED074ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif,
                                             PrestadorServicoNfseDTO prestador) {
        int mesInicio = DateUtils.getMes(arquivoDesif.getInicioCompetencia());
        if (ModuloDesifNfseDTO.MODULO_1.equals(arquivoDesif.getModulo())) {
            if (mesInicio != 1 &&
                    mesInicio != 7 &&
                    isAberturaNuloOrDiferenteInicioCompetencia(prestador.getAbertura(), arquivoDesif.getInicioCompetencia())) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED074, "Conteúdo: " +
                        DateUtils.getDataFormatada(arquivoDesif.getInicioCompetencia(), FORMAT_ANO_MES));
                return false;
            }
        } else if (ModuloDesifNfseDTO.MODULO_3.equals(arquivoDesif.getModulo())) {
            if (mesInicio != 1 &&
                    isAberturaNuloOrDiferenteInicioCompetencia(prestador.getAbertura(), arquivoDesif.getInicioCompetencia())) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED074, "Conteúdo: " +
                        DateUtils.getDataFormatada(arquivoDesif.getInicioCompetencia(), FORMAT_ANO_MES));
                return false;
            }
        }
        return true;
    }

    private boolean isAberturaNuloOrDiferenteInicioCompetencia(Date abertura, Date inicioCompetencia) {
        Integer anoInicio = DateUtils.getAno(inicioCompetencia);
        int mesInicio = DateUtils.getMes(inicioCompetencia);
        return abertura == null ||
                DateUtils.getAno(abertura).compareTo(anoInicio) != 0 ||
                DateUtils.getMes(abertura) != mesInicio;
    }

    private boolean validarED075ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif,
                                             PrestadorServicoNfseDTO prestador) {
        int mesFim = DateUtils.getMes(arquivoDesif.getFimCompetencia());
        if (ModuloDesifNfseDTO.MODULO_1.equals(arquivoDesif.getModulo())) {
            if (mesFim != 6 &&
                    mesFim != 12 &&
                    isEncerramentoNuloOrDiferenteFimCompetencia(prestador.getEncerramento(), arquivoDesif.getFimCompetencia())) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED075, "Conteúdo: " +
                        DateUtils.getDataFormatada(arquivoDesif.getFimCompetencia(), FORMAT_ANO_MES));
                return false;
            }
        } else if (ModuloDesifNfseDTO.MODULO_3.equals(arquivoDesif.getModulo())) {
            if (mesFim != 12 &&
                    isEncerramentoNuloOrDiferenteFimCompetencia(prestador.getEncerramento(), arquivoDesif.getFimCompetencia())) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED075, "Conteúdo: " +
                        DateUtils.getDataFormatada(arquivoDesif.getFimCompetencia(), FORMAT_ANO_MES));
                return false;
            }
        }
        return true;
    }

    private boolean isEncerramentoNuloOrDiferenteFimCompetencia(Date encerramento, Date fimCompetencia) {
        Integer anoFim = DateUtils.getAno(fimCompetencia);
        Integer mesFim = DateUtils.getMes(fimCompetencia);
        return encerramento == null ||
                DateUtils.getAno(encerramento).compareTo(anoFim) != 0 ||
                DateUtils.getMes(encerramento) != mesFim;
    }

    private boolean validarED005ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao, Long linha, Date valor, String nomeCampo) {
        if (valor == null) {
            Calendar dataAtual = Calendar.getInstance();
            dataAtual.setTime(new Date());
            dataAtual.set(Calendar.DAY_OF_MONTH, 1);

            Calendar data = Calendar.getInstance();
            data.setTime(valor);

            if (dataAtual.compareTo(data) >= 0) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED005, "Linha: " + linha +
                        "Campo: " + nomeCampo + "Conteúdo: " + DateUtils.getDataFormatada(valor));
                return false;
            }
        }
        return true;
    }

    private boolean validarEG004ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             Long linha,
                                             String conteudo,
                                             String nomeCampo) {
        if (conteudo != null && !conteudo.isEmpty()) {
            if (!Util.valida_CpfCnpj(conteudo)) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG004, "Linha: " + linha + " Campo: " +
                        nomeCampo + " Conteúdo: " + conteudo);
                return false;
            }
        }
        return true;
    }

    private boolean validarEG008ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             Long linha,
                                             String conteudo,
                                             String nomeCampo,
                                             Class classe) {
        try {
            if (conteudo != null && !conteudo.isEmpty()) {
                if (classe == Integer.class) {
                    new Integer(conteudo);
                } else if (classe == Long.class) {
                    new Long(conteudo);
                } else if (classe == BigDecimal.class) {
                    convertStringToBigDecimal(conteudo);
                }
            }
            return true;
        } catch (Exception e) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG008, "Linha: " + linha + " Campo: " +
                    nomeCampo + " Conteúdo: " + conteudo);
            return false;
        }
    }

    private boolean validarEG048ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             Long linha,
                                             BigDecimal valor,
                                             String nomeCampo) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG008, "Linha: " + linha + " Campo: " +
                    nomeCampo + " Conteúdo: " + BigDecimalUtils.formatarDuasCasasDecimais(valor));
            return false;
        }
        return true;
    }

    private boolean validarEG013ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             Long ordem,
                                             Long linha) {
        if (ordem.compareTo(linha) != 0) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG003, "Ordem: " + ordem);
            return false;
        }
        return true;
    }

    private boolean validarEG005ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             Long linha,
                                             String conteudo,
                                             String nomeCampo) {
        if (conteudo != null && !conteudo.trim().isEmpty()) {
            Date dataConvertida = DateUtils.getData(conteudo, FORMAT_ANO_MES_DIA);
            if (dataConvertida == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG005, "Linha: " + linha + " Campo: " +
                        nomeCampo + " Conteúdo: " + conteudo);
                return false;
            }
        }
        return true;
    }

    private boolean validarEG007ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             Long linha,
                                             String conteudo,
                                             String formato,
                                             String nomeCampo) {
        Date dataConvertida = DateUtils.getData(conteudo, formato);
        if (dataConvertida == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG007, "Linha: " + linha + " Campo: " +
                    nomeCampo + " Conteúdo: " + conteudo);
            return false;
        }
        return true;
    }

    private boolean validarEG009ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             Long linha,
                                             String conteudo,
                                             String nomeCampo,
                                             Integer tamanho,
                                             boolean exato) {
        if (conteudo != null && !conteudo.trim().isEmpty()) {
            if (exato && conteudo.length() != tamanho) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG009, "Linha: " + linha + " Campo: " + nomeCampo +
                        " Tamanho: " + tamanho);
                return false;
            } else if (!exato && conteudo.length() > tamanho) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG009, "Linha: " + linha + " Campo: " + nomeCampo +
                        " Tamanho: " + tamanho);
                return false;
            }
        }
        return true;
    }

    private boolean validarED004ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao, Long linha, Date valor, String nomeCampo) {
        if (valor == null) {
            Calendar dataAtual = Calendar.getInstance();
            dataAtual.setTime(new Date());
            dataAtual.set(Calendar.DAY_OF_MONTH, 1);
            Integer anoAtual = dataAtual.get(Calendar.YEAR);

            Calendar data = Calendar.getInstance();
            data.setTime(valor);
            Integer ano = dataAtual.get(Calendar.YEAR);

            if (anoAtual - ano > 10) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED004, "Linha: " + linha +
                        "Campo: " + nomeCampo + "Conteúdo: " + DateUtils.getDataFormatada(valor));
                return false;
            }
        }
        return true;
    }

    private boolean validarW001ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao, Long linha, String conteudo, String campo) {
        if (!importacao.getPrestador().getPessoa().getDadosPessoais().getCpfCnpj().contains(conteudo)) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.W001, "Linha: " + linha + "Campo: " + campo +
                    " Conteúdo: " + conteudo);
            return false;
        }
        return true;
    }

    private boolean validarEG046ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao, Long linha, String valor, String nomeCampo) {
        if (Strings.isNullOrEmpty(valor)) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG046, "Linha: " + linha + " Campo: " + nomeCampo);
            return false;
        }
        return true;
    }

    private boolean validarEG014ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                             Long linha,
                                             Integer quantidadeInformada,
                                             Integer quantidadeCorreta) {
        if (quantidadeInformada.compareTo(quantidadeCorreta) != 0) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG014,
                    " Linha: " + linha);
            return false;
        }
        return true;
    }

    private void lerAndValidarDetailArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao, ArquivoDesifNfseDTO arquivoDesif,
                                                 Long ordem, String conteudo) {
        if (arquivoDesif.getModulo() != null) {
            String[] campos = conteudo.split("\\|", -1);

            String linhaString = campos[0];
            Long linha = 0L;
            if (validarEG009ArquivoDesif(importacao, ordem, linhaString, "Num_Linha", 8, false) &&
                    validarEG008ArquivoDesif(importacao, ordem, linhaString, "Num_Linha", Long.class) &&
                    validarEG013ArquivoDesif(importacao, ordem, new Long(linhaString))) {
                linha = new Long(linhaString);


                switch (arquivoDesif.getModulo()) {
                    case MODULO_1: {
                        lerAndValidarLeiauteModulo1(importacao, arquivoDesif, ordem, linha, campos);
                        break;
                    }
                    case MODULO_2: {
                        lerAndValidarLeiauteModulo2(importacao, arquivoDesif, ordem, linha, campos);
                        break;
                    }
                    case MODULO_3: {
                        lerAndValidarLeiauteModulo3(importacao, arquivoDesif, ordem, linha, campos);
                        break;
                    }
                    case MODULO_4: {
                        lerAndValidarLeiauteModulo4(importacao, arquivoDesif, ordem, linha, campos);
                        break;
                    }
                }
            }
        }
    }

    private void lerAndValidarLeiauteModulo3(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif,
                                             Long ordem,
                                             Long linha,
                                             String[] campos) {
        String registro = campos[1];
        switch (registro) {
            case "0100": {
                lerAndValidarLeiauteRegistro0100(importacao, arquivoDesif, ordem, linha, campos);
                break;
            }
            case "0200": {
                lerAndValidarLeiauteRegistro0200(importacao, arquivoDesif, linha, campos);
                break;
            }
            case "0300": {
                lerAndValidarLeiauteRegistro0300(importacao, arquivoDesif, linha, campos);
                break;
            }
            default: {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EC021, "Linha: " + linha +
                        " Campo: Reg Conteúdo: " + registro);
            }
        }
    }

    private void lerAndValidarLeiauteModulo4(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif,
                                             Long ordem,
                                             Long linha,
                                             String[] campos) {
        String registro = campos[1];
        switch (registro) {
            case "1000": {
                lerAndValidarLeiauteRegistro1000(importacao, arquivoDesif, ordem, linha, campos);
                break;
            }
            default: {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EC021, "Linha: " + linha +
                        " Campo: Reg Conteúdo: " + registro);
            }
        }
    }

    private void lerAndValidarLeiauteRegistro0100(ImportacaoArquivoDesifNfseDTO importacao, ArquivoDesifNfseDTO arquivoDesif,
                                                  Long ordem, Long linha, String[] campos) {
        ArquivoDesifRegistro0100NfseDTO registro0100 = new ArquivoDesifRegistro0100NfseDTO();
        registro0100.setLinha(linha);

        if (!validarEG014ArquivoDesif(importacao, linha, campos.length, 9)) {
            return;
        }

        String conta = campos[2];
        if (validarEG046ArquivoDesif(importacao, linha, conta, "Conta") &&
                validarEG009ArquivoDesif(importacao, linha, conta, "Conta", 30, false)) {
            registro0100.setConta(conta);
        }

        String desdobramento = campos[3];
        if (validarEG046ArquivoDesif(importacao, linha, desdobramento, "Des_Mista") &&
                validarEG009ArquivoDesif(importacao, linha, desdobramento, "Des_Mista", 2, true) &&
                validarEG008ArquivoDesif(importacao, linha, desdobramento, "Des_Mista", Integer.class)) {
            registro0100.setDesdobramento(desdobramento);
        }

        String nome = campos[4];
        if (validarEG046ArquivoDesif(importacao, linha, nome, "Nome") &&
                validarEG009ArquivoDesif(importacao, linha, nome, "Nome", 100, false)) {
            registro0100.setNome(nome);
        }

        String descricaoDetalhada = campos[5];
        if (validarEG009ArquivoDesif(importacao, linha, descricaoDetalhada, "Desc_Conta", 600, false)) {
            registro0100.setDescricao(descricaoDetalhada);
        }

        String contaSuperior = campos[6];
        if (validarEG009ArquivoDesif(importacao, linha, contaSuperior, "Conta_Supe", 30, false)) {
            registro0100.setContaSuperior(contaSuperior);
        }

        String contaCosif = campos[7];
        if (validarEG046ArquivoDesif(importacao, linha, contaCosif, "Conta_COSIF") &&
                validarEG009ArquivoDesif(importacao, linha, contaCosif, "Conta_COSIF", 10, true) &&
                validarEG008ArquivoDesif(importacao, linha, contaCosif, "Conta_COSIF", Long.class)) {
            registro0100.setCosif(new CosifNfseDTO());
            registro0100.getCosif().setConta(contaCosif);
        }

        String codigoTributacao = campos[8];
        if (validarEG009ArquivoDesif(importacao, linha, codigoTributacao, "Cod_Trib_DES-IF", 9, true) &&
                validarEG008ArquivoDesif(importacao, linha, codigoTributacao, "Cod_Trib_DES-IF", Integer.class)) {
            registro0100.setCodigoTributacao(new CodigoTributacaoNfseDTO());
            registro0100.getCodigoTributacao().setCodigo(codigoTributacao);
        }

        arquivoDesif.getRegistros0100().add(registro0100);
    }

    private void lerAndValidarLeiauteRegistro1000(ImportacaoArquivoDesifNfseDTO importacao,
                                                  ArquivoDesifNfseDTO arquivoDesif,
                                                  Long ordem, Long linha, String[] campos) {
        ArquivoDesifRegistro1000NfseDTO registro1000 = new ArquivoDesifRegistro1000NfseDTO();
        registro1000.setLinha(linha);

        if (!validarEG014ArquivoDesif(importacao, linha, campos.length, 12)) {
            return;
        }

        String cnpj = campos[2];
        if (validarEG046ArquivoDesif(importacao, linha, cnpj, "CNPJ_Ctbl") &&
                validarEG009ArquivoDesif(importacao, linha, cnpj, "CNPJ_Ctbl", 14, true) &&
                validarEG008ArquivoDesif(importacao, linha, cnpj, "CNPJ_Ctbl", Long.class) &&
                validarEG004ArquivoDesif(importacao, linha, cnpj, "CNPJ_Ctbl")) {
            registro1000.setCnpj(cnpj);
        }

        String municipio = campos[3];
        if (validarEG046ArquivoDesif(importacao, linha, municipio, "Cod_Munc_Ctbl") &&
                validarEG009ArquivoDesif(importacao, linha, municipio, "Cod_Munc_Ctbl", 7, true) &&
                validarEG008ArquivoDesif(importacao, linha, municipio, "Cod_Munc_Ctbl", Long.class)) {
            registro1000.setMunicipio(new MunicipioNfseDTO());
            registro1000.getMunicipio().setCodigoIbge(municipio);
        }

        String numeroLancamento = campos[4];
        if (validarEG046ArquivoDesif(importacao, linha, numeroLancamento, "Idto_Lanc") &&
                validarEG009ArquivoDesif(importacao, linha, numeroLancamento, "Idto_Lanc", 50, false)) {
            registro1000.setNumeroLancamento(numeroLancamento);
        }

        String dataLancamento = campos[5];
        if (validarEG046ArquivoDesif(importacao, linha, dataLancamento, "Dat_Lanc") &&
                validarEG009ArquivoDesif(importacao, linha, dataLancamento, "Dat_Lanc", 8, true) &&
                validarEG005ArquivoDesif(importacao, linha, dataLancamento, "Dat_Lanc")) {
            registro1000.setDataLancamento(DateUtils.getData(dataLancamento, FORMAT_ANO_MES_DIA));
        }

        String valorPartidaLancamento = campos[6];
        if (validarEG046ArquivoDesif(importacao, linha, valorPartidaLancamento, "Valr_Prda_Lanc") &&
                validarEG009ArquivoDesif(importacao, linha, valorPartidaLancamento, "Valr_Prda_Lanc", 16, false) &&
                validarEG008ArquivoDesif(importacao, linha, valorPartidaLancamento, "Valr_Prda_Lanc", BigDecimal.class) &&
                validarEG048ArquivoDesif(importacao, linha, convertStringToBigDecimal(valorPartidaLancamento), "Valr_Prda_Lanc")) {
            registro1000.setValorPartidaLancamento(convertStringToBigDecimal(valorPartidaLancamento));
        }

        String conta = campos[7];
        if (validarEG046ArquivoDesif(importacao, linha, conta, "Sub_Titu") &&
                validarEG009ArquivoDesif(importacao, linha, conta, "Sub_Titu", 30, false)) {
            registro1000.setConta(conta);
        }

        String tipoPartida = campos[8];
        if (validarEG046ArquivoDesif(importacao, linha, tipoPartida, "Tipo_Prda") &&
                validarEG009ArquivoDesif(importacao, linha, tipoPartida, "Tipo_Prda", 1, true) &&
                validarEG008ArquivoDesif(importacao, linha, tipoPartida, "Tipo_Prda", Integer.class)) {
            registro1000.setTipoPartida(TipoPartidaNfseDTO.findByCodigo(new Integer(tipoPartida)));
        }

        String codigoEvento = campos[9];
        if (validarEG009ArquivoDesif(importacao, linha, codigoEvento, "Cod_Evto", 3, true) &&
                validarEG008ArquivoDesif(importacao, linha, codigoEvento, "Cod_Evto", Integer.class)) {
            registro1000.setEventoContabil(new EventoContabilDesifNfseDTO());
            registro1000.getEventoContabil().setCodigo(new Integer(codigoEvento));
        }

        String municipioVinculo = campos[10];
        if (validarEG046ArquivoDesif(importacao, linha, municipioVinculo, "Munc_Vinc") &&
                validarEG009ArquivoDesif(importacao, linha, municipioVinculo, "Munc_Vinc", 7, true) &&
                validarEG008ArquivoDesif(importacao, linha, municipioVinculo, "Munc_Vinc", Long.class)) {
            registro1000.setMunicipioVinculo(new MunicipioNfseDTO());
            registro1000.getMunicipioVinculo().setCodigoIbge(municipioVinculo);
        }

        String historico = campos[11];
        if (validarEG046ArquivoDesif(importacao, linha, historico, "Hist_Prda") &&
                validarEG009ArquivoDesif(importacao, linha, historico, "Hist_Prda", 255, false)) {
            registro1000.setHistorico(historico);
        }

        arquivoDesif.getRegistros1000().add(registro1000);
    }

    private void lerAndValidarLeiauteRegistro0200(ImportacaoArquivoDesifNfseDTO importacao,
                                                  ArquivoDesifNfseDTO arquivoDesif,
                                                  Long linha, String[] campos) {
        ArquivoDesifRegistro0200NfseDTO registro0200 = new ArquivoDesifRegistro0200NfseDTO();
        registro0200.setLinha(linha);

        if (!validarEG014ArquivoDesif(importacao, linha, campos.length, 8)) {
            return;
        }

        String codigoTarifa = campos[2];
        if (validarEG046ArquivoDesif(importacao, linha, codigoTarifa, "Idto_Tari") &&
                validarEG009ArquivoDesif(importacao, linha, codigoTarifa, "Idto_Tari", 4, false) &&
                validarEG008ArquivoDesif(importacao, linha, codigoTarifa, "Idto_Tari", Integer.class)) {
            registro0200.setTarifaBancaria(new TarifaBancariaNfseDTO());
            registro0200.getTarifaBancaria().setCodigo(new Integer(codigoTarifa));
        }

        String dataVigencia = campos[3];
        if (validarEG046ArquivoDesif(importacao, linha, dataVigencia, "Dat_Vige") &&
                validarEG009ArquivoDesif(importacao, linha, dataVigencia, "Dat_Vige", 8, true) &&
                validarEG005ArquivoDesif(importacao, linha, dataVigencia, "Dat_Vige")) {
            registro0200.setInicioVigencia(DateUtils.getData(dataVigencia, FORMAT_ANO_MES_DIA));
        }

        String valorUnitario = campos[4];
        if (validarEG046ArquivoDesif(importacao, linha, valorUnitario, "Val_Tari_Unit") &&
                validarEG009ArquivoDesif(importacao, linha, valorUnitario, "Val_Tari_Unit", 8, false) &&
                validarEG008ArquivoDesif(importacao, linha, valorUnitario, "Val_Tari_Unit", BigDecimal.class)) {
            registro0200.setValorUnitario(convertStringToBigDecimal(valorUnitario));
        }

        String valorPercentual = campos[5];
        if (validarEG046ArquivoDesif(importacao, linha, valorPercentual, "Val_Tari_Perc") &&
                validarEG009ArquivoDesif(importacao, linha, valorPercentual, "Val_Tari_Perc", 5, false) &&
                validarEG008ArquivoDesif(importacao, linha, valorPercentual, "Val_Tari_Perc", BigDecimal.class)) {
            registro0200.setValorPercentual(convertStringToBigDecimal(valorPercentual));
        }

        String conta = campos[6];
        if (validarEG046ArquivoDesif(importacao, linha, conta, "Sub_Titu") &&
                validarEG009ArquivoDesif(importacao, linha, conta, "Sub_Titu", 30, false)) {
            registro0200.setConta(conta);
        }

        String desdobramento = campos[7];
        if (validarEG046ArquivoDesif(importacao, linha, desdobramento, "Des_Mista") &&
                validarEG009ArquivoDesif(importacao, linha, desdobramento, "Des_Mista", 2, true) &&
                validarEG008ArquivoDesif(importacao, linha, desdobramento, "Des_Mista", Integer.class)) {
            registro0200.setDesdobramento(desdobramento);
        }

        arquivoDesif.getRegistros0200().add(registro0200);
    }

    private void lerAndValidarLeiauteRegistro0300(ImportacaoArquivoDesifNfseDTO importacao,
                                                  ArquivoDesifNfseDTO arquivoDesif,
                                                  Long linha, String[] campos) {
        ArquivoDesifRegistro0300NfseDTO registro0300 = new ArquivoDesifRegistro0300NfseDTO();
        registro0300.setLinha(linha);

        if (!validarEG014ArquivoDesif(importacao, linha, campos.length, 6)) {
            return;
        }

        String codigoProdutoServico = campos[2];
        if (validarEG046ArquivoDesif(importacao, linha, codigoProdutoServico, "Idto_Serv") &&
                validarEG009ArquivoDesif(importacao, linha, codigoProdutoServico, "Idto_Serv", 4, false) &&
                validarEG008ArquivoDesif(importacao, linha, codigoProdutoServico, "Idto_Serv", Integer.class)) {
            registro0300.setProdutoServicoBancario(new ProdutoServicoBancarioNfseDTO());
            registro0300.getProdutoServicoBancario().setCodigo(new Integer(codigoProdutoServico));
        }

        String descricaoComplementar = campos[3];
        if (validarEG009ArquivoDesif(importacao, linha, descricaoComplementar, "Desc_Compl_Serv", 255, false)) {
            registro0300.setDescricaoComplementar(descricaoComplementar);
        }

        String conta = campos[4];
        if (validarEG046ArquivoDesif(importacao, linha, conta, "Sub_Titu") &&
                validarEG009ArquivoDesif(importacao, linha, conta, "Sub_Titu", 30, false)) {
            registro0300.setConta(conta);
        }

        String desdobramento = campos[5];
        if (validarEG046ArquivoDesif(importacao, linha, desdobramento, "Des_Mista") &&
                validarEG009ArquivoDesif(importacao, linha, desdobramento, "Des_Mista", 2, true) &&
                validarEG008ArquivoDesif(importacao, linha, desdobramento, "Des_Mista", Integer.class)) {
            registro0300.setDesdobramento(desdobramento);
        }

        arquivoDesif.getRegistros0300().add(registro0300);
    }

    public BigDecimal convertDuasCasasDecimais(String value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        String pattern = "##0,00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        try {
            return (BigDecimal) decimalFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArquivoDesifNfseDTO findById(Long id) {
        ArquivoDesifNfseDTO arquivoDesif = repository.findById(id);
        preencher(arquivoDesif);
        return arquivoDesif;
    }

    @Override
    @Transactional
    public ArquivoDesifNfseDTO save(ArquivoDesifNfseDTO dto) {
        if (dto.getId() == null) {
            repository.insert(dto);
            insertRegistros0100(dto);
            insertRegistros0200(dto);
            insertRegistros0300(dto);
            insertRegistros0400(dto);
            insertRegistros0410(dto);
            insertRegistros0430(dto);
            insertRegistros0440(dto);
            insertRegistros1000(dto);
        } else {
            repository.update(dto);
        }
        return dto;
    }

    private void insertRegistros0100(ArquivoDesifNfseDTO dto) {
        if (dto.getRegistros0100() != null) {
            for (ArquivoDesifRegistro0100NfseDTO registro0100 : dto.getRegistros0100()) {
                registro0100.setIdArquivo(dto.getId());
            }
        }
        arquivoDesifRegistro0100Service.save(dto.getRegistros0100());
    }

    private void insertRegistros0200(ArquivoDesifNfseDTO dto) {
        if (dto.getRegistros0200() != null) {
            for (ArquivoDesifRegistro0200NfseDTO registro0200 : dto.getRegistros0200()) {
                registro0200.setIdArquivo(dto.getId());
            }
        }
        arquivoDesifRegistro0200Service.save(dto.getRegistros0200());
    }

    private void insertRegistros0300(ArquivoDesifNfseDTO dto) {
        if (dto.getRegistros0300() != null) {
            for (ArquivoDesifRegistro0300NfseDTO registro0300 : dto.getRegistros0300()) {
                registro0300.setIdArquivo(dto.getId());
            }
        }
        arquivoDesifRegistro0300Service.save(dto.getRegistros0300());
    }

    private void insertRegistros0400(ArquivoDesifNfseDTO dto) {
        if (dto.getRegistros0400() != null) {
            for (ArquivoDesifRegistro0400NfseDTO registro0400 : dto.getRegistros0400()) {
                registro0400.setIdArquivo(dto.getId());
            }
        }
        arquivoDesifRegistro0400Service.save(dto.getRegistros0400());
    }

    private void insertRegistros0410(ArquivoDesifNfseDTO dto) {
        if (dto.getRegistros0410() != null) {
            for (ArquivoDesifRegistro0410NfseDTO registro0410 : dto.getRegistros0410()) {
                registro0410.setIdArquivo(dto.getId());
            }
        }
        arquivoDesifRegistro0410Service.save(dto.getRegistros0410());
    }

    private void insertRegistros0430(ArquivoDesifNfseDTO dto) {
        if (dto.getRegistros0430() != null) {
            for (ArquivoDesifRegistro0430NfseDTO registro0430 : dto.getRegistros0430()) {
                registro0430.setIdArquivo(dto.getId());
            }
        }
        arquivoDesifRegistro0430Service.save(dto.getRegistros0430());
    }

    private void insertRegistros0440(ArquivoDesifNfseDTO dto) {
        if (dto.getRegistros0440() != null) {
            for (ArquivoDesifRegistro0440NfseDTO registro0440 : dto.getRegistros0440()) {
                registro0440.setIdArquivo(dto.getId());
            }
        }
        arquivoDesifRegistro0440Service.save(dto.getRegistros0440());
    }

    private void insertRegistros1000(ArquivoDesifNfseDTO dto) {
        if (dto.getRegistros1000() != null) {
            for (ArquivoDesifRegistro1000NfseDTO registro1000 : dto.getRegistros1000()) {
                registro1000.setIdArquivo(dto.getId());
            }
        }
        arquivoDesifRegistro1000Service.save(dto.getRegistros1000());
    }

    @Transactional
    @Override
    public void remove(Long id) {
        ArquivoDesifNfseDTO dto = findById(id);
        arquivoDesifRegistro0100Service.removerTodosPorIdArquivo(dto.getId());
        arquivoDesifRegistro0200Service.removerTodosPorIdArquivo(dto.getId());
        arquivoDesifRegistro0300Service.removerTodosPorIdArquivo(dto.getId());
        arquivoDesifRegistro0400Service.removerTodosPorIdArquivo(dto.getId());
        arquivoDesifRegistro0410Service.removerTodosPorIdArquivo(dto.getId());
        arquivoDesifRegistro0430Service.removerTodosPorIdArquivo(dto.getId());
        arquivoDesifRegistro0440Service.removerTodosPorIdArquivo(dto.getId());
        arquivoDesifRegistro1000Service.removerTodosPorIdArquivo(dto.getId());
        repository.remove(dto);
    }

    @Async
    @Transactional(propagation = Propagation.NEVER)
    public void processarArquivoDesif(ArquivoDesifNfseDTO arquivoDesif) {
        try {
            alterarArquivoParaEmProcessamento(arquivoDesif);
            processarArquivoDesifPorModulo(arquivoDesif);
            alterarArquivoParaProcessado(arquivoDesif);
        } catch (Exception e) {
            log.error("Erro no processamento de arquivo desif", e);
            alterarArquivoParaInconsistente(arquivoDesif);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processarArquivoDesifPorModulo(ArquivoDesifNfseDTO arquivoDesif) throws Exception {
        switch (arquivoDesif.getModulo()) {
            case MODULO_3: {
                processarModulo3ArquivoDesif(arquivoDesif);
                break;
            }
            case MODULO_2: {
                processarModulo2ArquivoDesif(arquivoDesif);
                break;
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void alterarArquivoParaEmProcessamento(ArquivoDesifNfseDTO arquivoDesif) {
        arquivoDesif.setSituacao(SituacaoArquivoDesifNfseDTO.EM_PROCESSAMENTO);
        save(arquivoDesif);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void alterarArquivoParaProcessado(ArquivoDesifNfseDTO arquivoDesif) {
        arquivoDesif.setProtocolo(gerarProtocoloArquivoDesif());
        arquivoDesif.setSituacao(SituacaoArquivoDesifNfseDTO.PROCESSADO);
        save(arquivoDesif);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void alterarArquivoParaInconsistente(ArquivoDesifNfseDTO arquivoDesif) {
        arquivoDesif.setProtocolo(gerarProtocoloArquivoDesif());
        arquivoDesif.setSituacao(SituacaoArquivoDesifNfseDTO.INCONSISTENTE);
        save(arquivoDesif);
    }

    private String gerarProtocoloArquivoDesif() {
        return new Generex("[0-9a-fA-F]{16}").random().replaceAll("(.{4})(?!$)", "$1-");
    }

    private void processarModulo3ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif) throws Exception {
        processarRegistros0100ArquivoDesif(arquivoDesif);
        processarRegistros0200ArquivoDesif(arquivoDesif);
        processarRegistros0300ArquivoDesif(arquivoDesif);
    }

    private void processarRegistros0100ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif) throws Exception {
        List<ArquivoDesifRegistro0100NfseDTO> registros = arquivoDesifRegistro0100Service.findByIdArquivo(arquivoDesif.getId());
        for (ArquivoDesifRegistro0100NfseDTO registro0100 : registros) {
            processarRegistro0100ArquivoDesif(arquivoDesif, registro0100);
        }
    }

    private void processarRegistro0100ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif,
                                                   ArquivoDesifRegistro0100NfseDTO registro0100) {
        PlanoGeralContasComentadoNfseDTO pgcc = planoGeralContasComentadoService
                .findByContaAndDesdobramentoAndReferencia(
                        arquivoDesif.getPrestador().getId(),
                        registro0100.getConta(),
                        registro0100.getDesdobramento(), arquivoDesif.getInicioCompetencia());
        if (pgcc == null) {
            pgcc = new PlanoGeralContasComentadoNfseDTO();
        }
        pgcc.setPrestador(arquivoDesif.getPrestador());
        pgcc.setInicioVigencia(arquivoDesif.getInicioCompetencia());
        pgcc.setFimVigencia(arquivoDesif.getFimCompetencia());
        pgcc.setConta(registro0100.getConta());
        pgcc.setDesdobramento(registro0100.getDesdobramento());
        pgcc.setNome(registro0100.getNome());
        pgcc.setDescricaoDetalhada(registro0100.getDescricao());
        pgcc.setCosif(registro0100.getCosif());
        pgcc.setCodigoTributacao(registro0100.getCodigoTributacao());
        if (!Strings.isNullOrEmpty(registro0100.getContaSuperior())) {
            PlanoGeralContasComentadoNfseDTO pgccSuperior =
                    planoGeralContasComentadoService
                            .findByContaAndDesdobramentoAndReferencia(
                                    arquivoDesif.getPrestador().getId(),
                                    registro0100.getContaSuperior(),
                                    "00", arquivoDesif.getInicioCompetencia());
            if (pgccSuperior == null) {
                pgccSuperior = new PlanoGeralContasComentadoNfseDTO();
                pgccSuperior.setPrestador(arquivoDesif.getPrestador());
                pgccSuperior.setInicioVigencia(arquivoDesif.getInicioCompetencia());
                pgccSuperior.setFimVigencia(arquivoDesif.getFimCompetencia());
                pgccSuperior.setConta(registro0100.getContaSuperior());
                pgccSuperior.setDesdobramento("00");
                planoGeralContasComentadoService.save(pgccSuperior);
            }
            pgcc.setIdContaSuperior(pgccSuperior.getId());
        }
        planoGeralContasComentadoService.save(pgcc);
    }

    private void processarRegistros0200ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif) throws Exception {
        List<ArquivoDesifRegistro0200NfseDTO> registros = arquivoDesifRegistro0200Service.findByIdArquivo(arquivoDesif.getId());
        for (ArquivoDesifRegistro0200NfseDTO registro0200 : registros) {
            processarRegistro0200ArquivoDesif(arquivoDesif, registro0200);
        }
    }

    private void processarRegistro0200ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif,
                                                   ArquivoDesifRegistro0200NfseDTO registro0200) {
        PlanoGeralContasComentadoTarifaBancariaNfseDTO pgccTarifaBancaria = new PlanoGeralContasComentadoTarifaBancariaNfseDTO();
        pgccTarifaBancaria.setTarifaBancaria(registro0200.getTarifaBancaria());
        pgccTarifaBancaria.setInicioVigencia(registro0200.getInicioVigencia());
        pgccTarifaBancaria.setValorUnitario(registro0200.getValorUnitario());
        pgccTarifaBancaria.setValorPercentual(registro0200.getValorPercentual());
        planoGeralContasComentadoService.savePgccTarifaBancaria(pgccTarifaBancaria);

        PlanoGeralContasComentadoNfseDTO pgcc = planoGeralContasComentadoService
                .findByContaAndDesdobramentoAndReferencia(
                        arquivoDesif.getPrestador().getId(),
                        registro0200.getConta(),
                        registro0200.getDesdobramento(), arquivoDesif.getInicioCompetencia());
        planoGeralContasComentadoService.changePgccTarifaBancaria(pgcc, pgccTarifaBancaria);
    }

    private void processarRegistros0300ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif) throws Exception {
        List<ArquivoDesifRegistro0300NfseDTO> registros = arquivoDesifRegistro0300Service.findByIdArquivo(arquivoDesif.getId());
        for (ArquivoDesifRegistro0300NfseDTO registro0300 : registros) {
            processarRegistro0300ArquivoDesif(arquivoDesif, registro0300);
        }
    }

    private void processarRegistro0300ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif,
                                                   ArquivoDesifRegistro0300NfseDTO registro0300) {
        PlanoGeralContasComentadoProdutoServicoNfseDTO pgccProdutoServico = new PlanoGeralContasComentadoProdutoServicoNfseDTO();
        pgccProdutoServico.setProdutoServico(registro0300.getProdutoServicoBancario());
        pgccProdutoServico.setDescricaoComplementar(registro0300.getDescricaoComplementar());
        planoGeralContasComentadoService.savePgccProdutoServico(pgccProdutoServico);

        PlanoGeralContasComentadoNfseDTO pgcc = planoGeralContasComentadoService
                .findByContaAndDesdobramentoAndReferencia(
                        arquivoDesif.getPrestador().getId(),
                        registro0300.getConta(),
                        registro0300.getDesdobramento(), arquivoDesif.getInicioCompetencia());
        planoGeralContasComentadoService.changePgccProdutoServico(pgcc, pgccProdutoServico);
    }

    private void validarConsistenciaModulo4ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                        ImportacaoArquivoDesifNfseDTO importacao,
                                                        ArquivoDesifNfseDTO arquivoDesif) {
        validarConsistenciaRegistros1000ArquivoDesif(configuracao, importacao, arquivoDesif);
    }

    private void validarConsistenciaRegistros1000ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                              ImportacaoArquivoDesifNfseDTO importacao,
                                                              ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif.getRegistros1000() != null) {
            for (ArquivoDesifRegistro1000NfseDTO registro1000 : arquivoDesif.getRegistros1000()) {
                validarConsistenciaRegistro1000ArquivoDesif(importacao, registro1000);
            }
        }
    }

    private void validarConsistenciaRegistro1000ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                                             ArquivoDesifRegistro1000NfseDTO registro1000) {
        MunicipioNfseDTO municipio = cidadeService.findByCodigoIBGE(registro1000.getMunicipio().getCodigoIbge());
        if (municipio == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG001, "Linha: " + registro1000.getLinha() +
                    "Campo: Cod_Munc_Ctbl Conteúdo: " + registro1000.getMunicipio().getCodigoIbge());
        } else {
            registro1000.setMunicipio(municipio);
        }
        if (registro1000.getEventoContabil() != null) {
            EventoContabilDesifNfseDTO eventoContabil = eventoContabilDesifService.findByCodigo(registro1000.getEventoContabil().getCodigo());
            if (eventoContabil == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EL001, "Linha: " + registro1000.getLinha() +
                        " Campo: Cod_Evto Conteúdo: " + registro1000.getEventoContabil().getCodigo());
            } else {
                registro1000.setEventoContabil(eventoContabil);
            }
        }
        MunicipioNfseDTO municipioVinculo = cidadeService.findByCodigoIBGE(registro1000.getMunicipioVinculo().getCodigoIbge());
        if (municipioVinculo == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG001, "Linha: " + registro1000.getLinha() +
                    " Campo: Munc_Vinc Conteúdo: " + registro1000.getMunicipioVinculo().getCodigoIbge());
        } else {
            registro1000.setMunicipioVinculo(municipioVinculo);
        }
    }

    private void lerAndValidarLeiauteRegistro0400(ImportacaoArquivoDesifNfseDTO importacao,
                                                  ArquivoDesifNfseDTO arquivoDesif,
                                                  Long ordem, Long linha, String[] campos) {
        ArquivoDesifRegistro0400NfseDTO registro0400 = new ArquivoDesifRegistro0400NfseDTO();
        registro0400.setLinha(linha);

        if (!validarEG014ArquivoDesif(importacao, linha, campos.length, 12)) {
            return;
        }

        String codigoDependencia = campos[2];
        if (validarEG046ArquivoDesif(importacao, linha, codigoDependencia, "Cod_Depe") &&
                validarEG009ArquivoDesif(importacao, linha, codigoDependencia, "Cod_Depe", 15, false)) {
            registro0400.setCodigoDependencia(codigoDependencia);
        }

        String identificacao = campos[3];
        if (validarEG046ArquivoDesif(importacao, linha, identificacao, "Indr_Insc_Munl") &&
                validarEG009ArquivoDesif(importacao, linha, identificacao, "Indr_Insc_Munl", 1, true) &&
                validarEG008ArquivoDesif(importacao, linha, identificacao, "Indr_Insc_Munl", Integer.class)) {
            registro0400.setIdentificacaoDependencia(IdentificacaoDependenciaNfseDTO.findByCodigo(new Integer(identificacao)));
        }

        String cnpjProprio = campos[4];
        if (validarEG009ArquivoDesif(importacao, linha, cnpjProprio, "CNPJ_Proprio", 6, true) &&
                validarEG008ArquivoDesif(importacao, linha, cnpjProprio, "CNPJ_Proprio", Integer.class) &&
                validarEG004ArquivoDesif(importacao, linha, arquivoDesif.getPrimeiros8DigitosCpfCnpj() + cnpjProprio, "CNPJ_Proprio")) {
            registro0400.setCnpjProprio(arquivoDesif.getPrimeiros8DigitosCpfCnpj() + cnpjProprio);
        }

        String tipoDependencia = campos[5];
        if (validarEG046ArquivoDesif(importacao, linha, tipoDependencia, "Tipo_Depe") &&
                validarEG009ArquivoDesif(importacao, linha, tipoDependencia, "Tipo_Depe", 2, false) &&
                validarEG008ArquivoDesif(importacao, linha, tipoDependencia, "Tipo_Depe", Integer.class)) {
            registro0400.setTipoDependencia(new TipoDependenciaDesifNfseDTO());
            registro0400.getTipoDependencia().setCodigo(new Integer(tipoDependencia));
        }

        String endereco = campos[6];
        if (validarEG009ArquivoDesif(importacao, linha, endereco, "Endr_Depe", 100, false)) {
            registro0400.setEnderecoDependencia(endereco);
        }

        String cnpjResponsavel = campos[7];
        if (validarEG009ArquivoDesif(importacao, linha, cnpjResponsavel, "CNPJ_Unif", 6, true) &&
                validarEG008ArquivoDesif(importacao, linha, cnpjResponsavel, "CNPJ_Unif", Integer.class) &&
                validarEG004ArquivoDesif(importacao, linha, arquivoDesif.getPrimeiros8DigitosCpfCnpj() + cnpjResponsavel, "CNPJ_Unif")) {
            registro0400.setCnpjResponsavel(arquivoDesif.getPrimeiros8DigitosCpfCnpj() + cnpjResponsavel);
        }

        String codigoMunicipio = campos[8];
        if (validarEG046ArquivoDesif(importacao, linha, codigoMunicipio, "Cod_Munc") &&
                validarEG009ArquivoDesif(importacao, linha, codigoMunicipio, "Cod_Munc", 7, true) &&
                validarEG008ArquivoDesif(importacao, linha, codigoMunicipio, "Cod_Munc", Integer.class)) {
            registro0400.setMunicipioResponsavel(new MunicipioNfseDTO());
            registro0400.getMunicipioResponsavel().setCodigoIbge(codigoMunicipio);
        }

        String contabilidadePropria = campos[9];
        if (validarEG046ArquivoDesif(importacao, linha, contabilidadePropria, "Cod_Munc") &&
                validarEG009ArquivoDesif(importacao, linha, contabilidadePropria, "Cod_Munc", 1, true)) {
            registro0400.setContabilidadePropria(contabilidadePropria.equals("1"));
        }

        String dataInicioParalizacao = campos[10];
        if (validarEG009ArquivoDesif(importacao, linha, dataInicioParalizacao, "Dat_Inic_Para", 8, true) &&
                validarEG005ArquivoDesif(importacao, linha, dataInicioParalizacao, "Dat_Inic_Para")) {
            registro0400.setDataInicioParalizacao(DateUtils.getData(dataInicioParalizacao, FORMAT_ANO_MES));
        }

        String dataFimParalizacao = campos[11];
        if (validarEG009ArquivoDesif(importacao, linha, dataFimParalizacao, "Dat_Fim_Para", 8, true) &&
                validarEG005ArquivoDesif(importacao, linha, dataFimParalizacao, "Dat_Fim_Para")) {
            registro0400.setDataFimParalizacao(DateUtils.getData(dataFimParalizacao, FORMAT_ANO_MES));
        }

        arquivoDesif.getRegistros0400().add(registro0400);
    }

    private void lerAndValidarLeiauteModulo1(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif,
                                             Long ordem,
                                             Long linha,
                                             String[] campos) {
        String registro = campos[1];
        switch (registro) {
            case "0400": {
                lerAndValidarLeiauteRegistro0400(importacao, arquivoDesif, ordem, linha, campos);
                break;
            }
            case "0410": {
                lerAndValidarLeiauteRegistro0410(importacao, arquivoDesif, ordem, linha, campos);
                break;
            }
            case "1000": {
                lerAndValidarLeiauteRegistro1000(importacao, arquivoDesif, ordem, linha, campos);
                break;
            }
            default: {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EC021, "Linha: " + linha +
                        " Campo: Reg Conteúdo: " + registro);
            }
        }
    }

    private void validarConsistenciaRegistro0400ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                                             ArquivoDesifRegistro0400NfseDTO registro0400) {
        if (registro0400.getTipoDependencia() != null) {
            TipoDependenciaDesifNfseDTO tipoDependencia = tipoDependenciaDesifService.findByCodigo(registro0400.getTipoDependencia().getCodigo());
            if (tipoDependencia == null) {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.ED008, "Linha: " + registro0400.getLinha() +
                        " Campo: Tipo_Depe Conteúdo: " + registro0400.getTipoDependencia().getCodigo());
            } else {
                registro0400.setTipoDependencia(tipoDependencia);
            }
        }
        MunicipioNfseDTO municipio = cidadeService.findByCodigoIBGE(registro0400.getMunicipioResponsavel().getCodigoIbge());
        if (municipio == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EG001, "Linha: " + registro0400.getLinha() +
                    " Campo: Cod_Munc Conteúdo: " + registro0400.getMunicipioResponsavel().getCodigoIbge());
        } else {
            registro0400.setMunicipioResponsavel(municipio);
        }
    }

    private void validarConsistenciaRegistros0400ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                              ImportacaoArquivoDesifNfseDTO importacao,
                                                              ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif.getRegistros0400() != null) {
            for (ArquivoDesifRegistro0400NfseDTO registro0400 : arquivoDesif.getRegistros0400()) {
                validarConsistenciaRegistro0400ArquivoDesif(importacao, registro0400);
            }
        }
    }

    private void validarConsistenciaModulo1ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                        ImportacaoArquivoDesifNfseDTO importacao,
                                                        ArquivoDesifNfseDTO arquivoDesif) {
        validarConsistenciaRegistros0400ArquivoDesif(configuracao, importacao, arquivoDesif);
        validarConsistenciaRegistros0410ArquivoDesif(configuracao, importacao, arquivoDesif);
        validarConsistenciaRegistros1000ArquivoDesif(configuracao, importacao, arquivoDesif);
    }

    private void lerAndValidarLeiauteRegistro0410(ImportacaoArquivoDesifNfseDTO importacao,
                                                  ArquivoDesifNfseDTO arquivoDesif,
                                                  Long ordem, Long linha, String[] campos) {
        ArquivoDesifRegistro0410NfseDTO registro0410 = new ArquivoDesifRegistro0410NfseDTO();
        registro0410.setLinha(linha);

        if (!validarEG014ArquivoDesif(importacao, linha, campos.length, 12)) {
            return;
        }

        String codigoDependencia = campos[2];
        if (validarEG046ArquivoDesif(importacao, linha, codigoDependencia, "Cod_Depe") &&
                validarEG009ArquivoDesif(importacao, linha, codigoDependencia, "Cod_Depe", 15, false)) {
            registro0410.setCodigoDependencia(codigoDependencia);
        }

        String competencia = campos[3];
        if (validarEG046ArquivoDesif(importacao, linha, competencia, "Ano_Mes_Cmpe") &&
                validarEG009ArquivoDesif(importacao, linha, competencia, "Ano_Mes_Cmpe", 6, true) &&
                validarEG007ArquivoDesif(importacao, linha, competencia, FORMAT_ANO_MES, "Ano_Mes_Cmpe")) {
            registro0410.setCompetencia(DateUtils.getData(competencia, FORMAT_ANO_MES));
        }

        String conta = campos[4];
        String desdobramento = campos[5];
        if (validarEG046ArquivoDesif(importacao, linha, conta, "Conta") &&
                validarEG009ArquivoDesif(importacao, linha, conta, "Conta", 30, false) &&
                validarEG046ArquivoDesif(importacao, linha, desdobramento, "Des_Mista") &&
                validarEG009ArquivoDesif(importacao, linha, desdobramento, "Des_Mista", 2, true) &&
                validarEG008ArquivoDesif(importacao, linha, desdobramento, "Des_Mista", Integer.class)) {
            registro0410.setConta(new PlanoGeralContasComentadoNfseDTO());
            registro0410.getConta().setConta(conta);
            registro0410.getConta().setDesdobramento(desdobramento);
        }

        String saldoInicial = campos[6];
        if (validarEG046ArquivoDesif(importacao, linha, saldoInicial, "Sald_Inic") &&
                validarEG009ArquivoDesif(importacao, linha, saldoInicial, "Sald_Inic", 16, false) &&
                validarEG008ArquivoDesif(importacao, linha, saldoInicial, "Sald_Inic", BigDecimal.class)) {
            registro0410.setSaldoInicial(convertStringToBigDecimal(saldoInicial));
        }

        String valorDebito = campos[7];
        if (validarEG046ArquivoDesif(importacao, linha, valorDebito, "Valr_Debt") &&
                validarEG009ArquivoDesif(importacao, linha, valorDebito, "Valr_Debt", 16, false) &&
                validarEG008ArquivoDesif(importacao, linha, valorDebito, "Valr_Debt", BigDecimal.class) &&
                validarEG048ArquivoDesif(importacao, linha, convertStringToBigDecimal(valorDebito), "Valr_Debt")) {
            registro0410.setValorDebito(convertStringToBigDecimal(valorDebito));
        }

        String valorCredito = campos[8];
        if (validarEG046ArquivoDesif(importacao, linha, valorCredito, "Valr_Cred") &&
                validarEG009ArquivoDesif(importacao, linha, valorCredito, "Valr_Cred", 16, false) &&
                validarEG008ArquivoDesif(importacao, linha, valorCredito, "Valr_Cred", BigDecimal.class) &&
                validarEG048ArquivoDesif(importacao, linha, convertStringToBigDecimal(valorCredito), "Valr_Cred")) {
            registro0410.setValorCredito(convertStringToBigDecimal(valorCredito));
        }

        String saldoFinal = campos[9];
        if (validarEG046ArquivoDesif(importacao, linha, saldoFinal, "Sald_Inic") &&
                validarEG009ArquivoDesif(importacao, linha, saldoFinal, "Sald_Inic", 16, false) &&
                validarEG008ArquivoDesif(importacao, linha, saldoFinal, "Sald_Inic", BigDecimal.class)) {
            registro0410.setSaldoFinal(convertStringToBigDecimal(saldoFinal));
        }

        arquivoDesif.getRegistros0410().add(registro0410);
    }

    private void validarConsistenciaRegistro0410ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                                             ArquivoDesifRegistro0410NfseDTO registro0410) {
        PlanoGeralContasComentadoNfseDTO pgcc = planoGeralContasComentadoService
                .findByContaAndDesdobramentoAndReferencia(
                        importacao.getPrestador().getId(),
                        registro0410.getConta().getConta(),
                        registro0410.getConta().getDesdobramento(), registro0410.getCompetencia());
        if (pgcc == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.W002, "Linha: " + registro0410.getLinha() +
                    " Campo: Conta, Des_Mista Conteúdo: " + registro0410.getConta().getConta() + ", " + registro0410.getConta().getDesdobramento());
        } else {
            registro0410.setConta(pgcc);
        }
    }

    private void validarConsistenciaRegistros0410ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                              ImportacaoArquivoDesifNfseDTO importacao,
                                                              ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif.getRegistros0410() != null) {
            for (ArquivoDesifRegistro0410NfseDTO registro0410 : arquivoDesif.getRegistros0410()) {
                validarConsistenciaRegistro0410ArquivoDesif(importacao, registro0410);
            }
        }
    }

    private void lerAndValidarLeiauteRegistro0430(ImportacaoArquivoDesifNfseDTO importacao,
                                                  ArquivoDesifNfseDTO arquivoDesif,
                                                  Long ordem, Long linha, String[] campos) {
        ArquivoDesifRegistro0430NfseDTO registro0430 = new ArquivoDesifRegistro0430NfseDTO();
        registro0430.setLinha(linha);

        if (!validarEG014ArquivoDesif(importacao, linha, campos.length, 18)) {
            return;
        }

        String codigoDependencia = campos[2];
        if (validarCampoString(importacao, linha, codigoDependencia, "Cod_Depe",
                15, false, true)) {
            registro0430.setCodigoDependencia(codigoDependencia);
        }

        String conta = campos[3];
        String desdobramento = campos[4];
        if (validarCampoString(importacao, linha, conta, "Sub_Titu", 30, false, true) &&
                validarCampoInteger(importacao, linha, desdobramento, "", 2, true, true)) {
            registro0430.setConta(new PlanoGeralContasComentadoNfseDTO());
            registro0430.getConta().setConta(conta);
            registro0430.getConta().setDesdobramento(desdobramento);
        }

        String codigoTributacao = campos[5];
        if (validarCampoString(importacao, linha, codigoTributacao, "Cod_Trib_DES-IF",
                9, true, true)) {
            registro0430.setCodigoTributacao(new CodigoTributacaoNfseDTO());
            registro0430.getCodigoTributacao().setCodigo(codigoTributacao);
        }

        String valorCredito = campos[6];
        if (validarCampoBigDecimal(importacao, linha, valorCredito, "Valr_Cred_Mens",
                16, false, true)) {
            registro0430.setValorCredito(convertStringToBigDecimal(valorCredito));
        }

        String valorDebito = campos[7];
        if (validarCampoBigDecimal(importacao, linha, valorDebito, "Valr_Debt_Mens",
                16, false, true)) {
            registro0430.setValorDebito(convertStringToBigDecimal(valorDebito));
        }

        String valorReceitaTributavel = campos[8];
        if (validarCampoBigDecimal(importacao, linha, valorReceitaTributavel, "Rece_Decl",
                16, false, true)) {
            registro0430.setValorReceitaTributavel(convertStringToBigDecimal(valorReceitaTributavel));
        }

        String valorDeducaoLegal = campos[9];
        if (validarCampoBigDecimal(importacao, linha, valorDeducaoLegal, "Dedu_Rece_Decl",
                16, false, false)) {
            registro0430.setValorDeducaoLegal(convertStringToBigDecimal(valorDeducaoLegal));
        }

        String discriminacaoDeducao = campos[10];
        if (validarCampoString(importacao, linha, discriminacaoDeducao, "Dedu_Rece_Decl",
                255, false, registro0430.getValorDeducaoLegal() != null)) {
            registro0430.setDiscriminacaoDeducao(discriminacaoDeducao);
        }

        String baseCalculo = campos[11];
        if (validarCampoBigDecimal(importacao, linha, baseCalculo, "Base_Calc",
                16, false, true)) {
            registro0430.setBaseCalculo(convertStringToBigDecimal(baseCalculo));
        }

        String aliquota = campos[12];
        if (validarCampoBigDecimal(importacao, linha, aliquota, "Aliq_ISSQN",
                16, false, true)) {
            registro0430.setAliquota(convertStringToBigDecimal(aliquota));
        }

        String valorIncentivoFiscal = campos[13];
        if (validarCampoBigDecimal(importacao, linha, valorIncentivoFiscal, "Inct_Fisc",
                16, false, false)) {
            registro0430.setValorIncentivoFiscal(convertStringToBigDecimal(valorIncentivoFiscal));
        }

        String discriminacaoIncentivo = campos[14];
        if (validarCampoString(importacao, linha, discriminacaoIncentivo, "Desc_Inct_Fisc",
                255, false, registro0430.getValorIncentivoFiscal() != null)) {
            registro0430.setDiscriminacaoIncentivo(discriminacaoDeducao);
        }

        String valorIssqnRetido = campos[15];
        if (!Strings.isNullOrEmpty(valorIssqnRetido)) {
            if (validarCampoBigDecimal(importacao, linha, valorIssqnRetido, "Valr_ISSQN_Retd",
                    16, false, false)) {
                registro0430.setValorIssqnRetido(convertStringToBigDecimal(valorIssqnRetido));
            }
        }

        String exigibilidadeSuspensao = campos[16];
        if (!Strings.isNullOrEmpty(exigibilidadeSuspensao)) {
            if (validarCampoInteger(importacao, linha, exigibilidadeSuspensao, "Motv_Nao_Exig",
                    1, false, false)) {
                registro0430.setExigibilidadeSuspensao(getExigibilidadeSuspensao(new Integer(exigibilidadeSuspensao)));
            }
        }

        String processoSuspensao = campos[17];
        if (validarCampoString(importacao, linha, processoSuspensao, "Proc_Motv_Nao_Exig",
                20, false, registro0430.getExigibilidadeSuspensao() != null)) {
            registro0430.setProcessoSuspensao(processoSuspensao);
        }

        arquivoDesif.getRegistros0430().add(registro0430);
    }

    private BigDecimal convertStringToBigDecimal(String valor) {
        if (Strings.isNullOrEmpty(valor)) {
            return null;
        }
        valor = valor.replace(",", ".");
        return new BigDecimal(valor);
    }

    private ExigibilidadeNfseDTO getExigibilidadeSuspensao(Integer codigo) {
        if (codigo != null) {
            switch (codigo) {
                case 1:
                    return ExigibilidadeNfseDTO.EXIGIBILIDADE_SUSPENSA_DECISAO_JUDICIAL;
                case 2:
                    return ExigibilidadeNfseDTO.EXIGIBILIDADE_SUSPENSA_PROCESSO_ADMINISTRATIVO;
            }
        }
        return null;
    }

    private Boolean validarCampoInteger(ImportacaoArquivoDesifNfseDTO importacao,
                                        Long linha,
                                        String valor,
                                        String campo,
                                        Integer tamanho,
                                        Boolean exato,
                                        Boolean obrigatorio) {
        if ((!obrigatorio || validarEG046ArquivoDesif(importacao, linha, valor, campo)) &&
                validarEG009ArquivoDesif(importacao, linha, valor, campo, tamanho, exato) &&
                validarEG008ArquivoDesif(importacao, linha, valor, campo, Integer.class)) {
            return true;
        }
        return false;
    }

    private Boolean validarCampoString(ImportacaoArquivoDesifNfseDTO importacao,
                                       Long linha,
                                       String valor,
                                       String campo,
                                       Integer tamanho,
                                       Boolean exato,
                                       Boolean obrigatorio) {
        if ((!obrigatorio || validarEG046ArquivoDesif(importacao, linha, valor, campo)) &&
                validarEG009ArquivoDesif(importacao, linha, valor, campo, tamanho, exato)) {
            return true;
        }
        return false;
    }

    private Boolean validarCampoBigDecimal(ImportacaoArquivoDesifNfseDTO importacao,
                                           Long linha,
                                           String valor,
                                           String campo,
                                           Integer tamanho,
                                           Boolean exato,
                                           Boolean obrigatorio) {
        if ((!obrigatorio || validarEG046ArquivoDesif(importacao, linha, valor, campo)) &&
                validarEG009ArquivoDesif(importacao, linha, valor, campo, tamanho, exato) &&
                validarEG008ArquivoDesif(importacao, linha, valor, campo, BigDecimal.class) &&
                validarEG048ArquivoDesif(importacao, linha, convertStringToBigDecimal(valor), campo)) {
            return true;
        }
        return false;
    }

    private void lerAndValidarLeiauteRegistro0440(ImportacaoArquivoDesifNfseDTO importacao,
                                                  ArquivoDesifNfseDTO arquivoDesif,
                                                  Long ordem, Long linha, String[] campos) {
        ArquivoDesifRegistro0440NfseDTO registro0440 = new ArquivoDesifRegistro0440NfseDTO();
        registro0440.setLinha(linha);

        if (!validarEG014ArquivoDesif(importacao, linha, campos.length, 21)) {
            return;
        }

        String cnpjDependencia = campos[2];
        if (validarCampoInteger(importacao, linha, cnpjDependencia, "CNPJ",
                6, true, true)) {
            registro0440.setCnpjDependencia(cnpjDependencia);
        }

        String codigoTributacao = campos[3];
        if (!Strings.isNullOrEmpty(codigoTributacao)) {
            if (validarCampoInteger(importacao, linha, codigoTributacao, "Cod_Trib_DES-IF",
                    9, true, false)) {
                registro0440.setCodigoTributacao(new CodigoTributacaoNfseDTO());
                registro0440.getCodigoTributacao().setCodigo(codigoTributacao);
            }
        }

        String valorReceitaTributaval = campos[4];
        if (validarCampoBigDecimal(importacao, linha, valorReceitaTributaval, "Rece_Decl_Cnso",
                16, false, true)) {
            registro0440.setValorReceitaTributavel(convertStringToBigDecimal(valorReceitaTributaval));
        }

        String valorDeducaoConta = campos[5];
        if (!Strings.isNullOrEmpty(valorDeducaoConta)) {
            if (validarCampoBigDecimal(importacao, linha, valorDeducaoConta, "Dedu_Rece_Decl_Sub_Titu",
                    16, false, false)) {
                registro0440.setValorDeducaoConta(convertStringToBigDecimal(valorDeducaoConta));
            }
        }

        String valorDeducaoConsolidado = campos[6];
        if (!Strings.isNullOrEmpty(valorDeducaoConsolidado)) {
            if (validarCampoBigDecimal(importacao, linha, valorDeducaoConsolidado, "Dedu_Rece_Decl_Cnso",
                    16, false, false)) {
                registro0440.setValorDeducaoConsolidado(convertStringToBigDecimal(valorDeducaoConsolidado));
            }
        }

        String discriminacaoDeducao = campos[7];
        if (validarCampoString(importacao, linha, discriminacaoDeducao, "Desc_Dedu",
                255, false, false)) {
            registro0440.setDiscriminacaoDeducao(discriminacaoDeducao);
        }

        String baseCalculo = campos[8];
        if (validarCampoBigDecimal(importacao, linha, baseCalculo, "Base_Calc",
                16, false, true)) {
            registro0440.setBaseCalculo(convertStringToBigDecimal(baseCalculo));
        }

        String aliquota = campos[9];
        if (validarCampoBigDecimal(importacao, linha, aliquota, "Aliq_ISSQN",
                16, false, true)) {
            registro0440.setAliquota(convertStringToBigDecimal(aliquota));
        }

        String valorIssqn = campos[10];
        if (validarCampoBigDecimal(importacao, linha, valorIssqn, "Valr_ISSQN_Devd",
                16, false, true)) {
            registro0440.setValorIssqn(convertStringToBigDecimal(valorIssqn));
        }

        String valorIssqnRetido = campos[11];
        if (!Strings.isNullOrEmpty(valorIssqnRetido)) {
            if (validarCampoBigDecimal(importacao, linha, valorIssqnRetido, "Valr_ISSQN_Retd",
                    16, false, false)) {
                registro0440.setValorIssqnRetido(convertStringToBigDecimal(valorIssqnRetido));
            }
        }

        String valorIncentivoConta = campos[12];
        if (!Strings.isNullOrEmpty(valorIncentivoConta)) {
            if (validarCampoBigDecimal(importacao, linha, valorIncentivoConta, "Inct_Fisc_Sub_Titu",
                    16, false, false)) {
                registro0440.setValorIncentivoConta(convertStringToBigDecimal(valorIncentivoConta));
            }
        }

        String valorIncentivoConsolidado = campos[13];
        if (!Strings.isNullOrEmpty(valorIncentivoConsolidado)) {
            if (validarCampoBigDecimal(importacao, linha, valorIncentivoConsolidado, "Inct_Fisc",
                    16, false, false)) {
                registro0440.setValorIncentivoConsolidado(convertStringToBigDecimal(valorIncentivoConsolidado));
            }
        }

        String discriminacaoIncentivo = campos[14];
        if (validarCampoString(importacao, linha, discriminacaoIncentivo, "Desc_Inct_Fisc",
                255, false, false)) {
            registro0440.setDiscriminacaoIncentivo(discriminacaoIncentivo);
        }

        String valorCompensacao = campos[15];
        if (!Strings.isNullOrEmpty(valorCompensacao)) {
            if (validarCampoBigDecimal(importacao, linha, valorCompensacao, "Valr_A_Cmpn",
                    16, false, false)) {
                registro0440.setValorCompensacao(convertStringToBigDecimal(valorCompensacao));
            }
        }

        String discriminacaoCompensacao = campos[16];
        if (validarCampoString(importacao, linha, discriminacaoCompensacao, "Orig_Cred_A_Cmpn",
                255, false, false)) {
            registro0440.setDiscriminacaoCompensacao(discriminacaoCompensacao);
        }

        String valorIssqnRecolhido = campos[17];
        if (!Strings.isNullOrEmpty(valorIssqnRecolhido)) {
            if (validarCampoBigDecimal(importacao, linha, valorIssqnRecolhido, "Valr_ISSQN_Rclh",
                    16, false, false)) {
                registro0440.setValorIssqnRecolhido(convertStringToBigDecimal(valorIssqnRecolhido));
            }
        }

        String exigibilidadeSuspensao = campos[18];
        if (!Strings.isNullOrEmpty(exigibilidadeSuspensao)) {
            if (validarCampoInteger(importacao, linha, exigibilidadeSuspensao, "Motv_Nao_Exig",
                    1, false, false)) {
                registro0440.setExigibilidadeSuspensao(getExigibilidadeSuspensao(new Integer(exigibilidadeSuspensao)));
            }
        }

        String processoSuspensao = campos[19];
        if (validarCampoString(importacao, linha, processoSuspensao, "Proc_Motv_Nao_Exig",
                20, false, registro0440.getExigibilidadeSuspensao() != null)) {
            registro0440.setProcessoSuspensao(processoSuspensao);
        }

        String valorIssqnRecolher = campos[20];
        if (!Strings.isNullOrEmpty(valorIssqnRecolher)) {
            if (validarCampoBigDecimal(importacao, linha, valorIssqnRecolher, "ISSQN_A_Relh",
                    16, false, false)) {
                registro0440.setValorIssqnRecolher(convertStringToBigDecimal(valorIssqnRecolher));
            }
        }

        arquivoDesif.getRegistros0440().add(registro0440);
    }


    private void lerAndValidarLeiauteModulo2(ImportacaoArquivoDesifNfseDTO importacao,
                                             ArquivoDesifNfseDTO arquivoDesif,
                                             Long ordem,
                                             Long linha,
                                             String[] campos) {
        String registro = campos[1];
        switch (registro) {
            case "0400": {
                lerAndValidarLeiauteRegistro0400(importacao, arquivoDesif, ordem, linha, campos);
                break;
            }
            case "0430": {
                lerAndValidarLeiauteRegistro0430(importacao, arquivoDesif, ordem, linha, campos);
                break;
            }
            case "0440": {
                lerAndValidarLeiauteRegistro0440(importacao, arquivoDesif, ordem, linha, campos);
                break;
            }
            default: {
                importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.EC021, "Linha: " + linha +
                        " Campo: Reg Conteúdo: " + registro);
            }
        }
    }

    private void validarConsistenciaRegistro0430ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                                             ArquivoDesifNfseDTO arquivoDesif,
                                                             ArquivoDesifRegistro0430NfseDTO registro0430) {
        PlanoGeralContasComentadoNfseDTO conta =
                planoGeralContasComentadoService.findByContaAndDesdobramentoAndReferencia(
                        arquivoDesif.getPrestador().getId(),
                        registro0430.getConta().getConta(),
                        registro0430.getConta().getDesdobramento(), arquivoDesif.getInicioCompetencia());
        if (conta == null) {
            importacao.adicionarValidacao(TipoValidacaoDesifNfseDTO.W002, "Linha: " + registro0430.getLinha() +
                    " Campo: Sub_Titu, Des_Mista Conteúdo: " + registro0430.getConta().getConta() + ", " +
                    registro0430.getConta().getDesdobramento());
        } else {
            registro0430.setConta(conta);
        }
        registro0430.setCodigoTributacao(validarEG011AndEG047(importacao, arquivoDesif,
                registro0430.getLinha(), registro0430.getCodigoTributacao()));
        if (registro0430.getCodigoTributacao() != null) {
            validarEM046ArquivoDesif(importacao, "Aliq_ISSQN", registro0430.getLinha(),
                    registro0430.getCodigoTributacao(), registro0430.getAliquota());
        }
    }

    private void validarConsistenciaRegistros0430ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                              ImportacaoArquivoDesifNfseDTO importacao,
                                                              ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif.getRegistros0430() != null) {
            for (ArquivoDesifRegistro0430NfseDTO registro0430 : arquivoDesif.getRegistros0430()) {
                validarConsistenciaRegistro0430ArquivoDesif(importacao, arquivoDesif, registro0430);
            }
        }
    }

    private void validarConsistenciaRegistro0440ArquivoDesif(ImportacaoArquivoDesifNfseDTO importacao,
                                                             ArquivoDesifNfseDTO arquivoDesif,
                                                             ArquivoDesifRegistro0440NfseDTO registro0440) {
        if (registro0440.getCodigoTributacao() != null) {
            registro0440.setCodigoTributacao(validarEG011AndEG047(importacao, arquivoDesif,
                    registro0440.getLinha(), registro0440.getCodigoTributacao()));
            if (registro0440.getCodigoTributacao() != null) {
                if (registro0440.getCodigoTributacao() != null) {
                    validarEM046ArquivoDesif(importacao, "Aliq_ISSQN", registro0440.getLinha(),
                            registro0440.getCodigoTributacao(), registro0440.getAliquota());
                }
            }
        }
    }

    private void validarConsistenciaRegistros0440ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                              ImportacaoArquivoDesifNfseDTO importacao,
                                                              ArquivoDesifNfseDTO arquivoDesif) {
        if (arquivoDesif.getRegistros0440() != null) {
            for (ArquivoDesifRegistro0440NfseDTO registro0440 : arquivoDesif.getRegistros0440()) {
                validarConsistenciaRegistro0440ArquivoDesif(importacao, arquivoDesif, registro0440);
            }
        }
    }

    private void validarConsistenciaModulo2ArquivoDesif(ConfiguracaoNfseDTO configuracao,
                                                        ImportacaoArquivoDesifNfseDTO importacao,
                                                        ArquivoDesifNfseDTO arquivoDesif) {
        validarConsistenciaRegistros0400ArquivoDesif(configuracao, importacao, arquivoDesif);
        validarConsistenciaRegistros0430ArquivoDesif(configuracao, importacao, arquivoDesif);
        validarConsistenciaRegistros0440ArquivoDesif(configuracao, importacao, arquivoDesif);
    }

    private void processarModulo2ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif) throws Exception {
        processarRegistros0430ArquivoDesif(arquivoDesif);
    }

    private void processarRegistros0430ArquivoDesif(ArquivoDesifNfseDTO arquivoDesif) throws Exception {
        List<ArquivoDesifRegistro0430NfseDTO> registros = arquivoDesifRegistro0430Service.findByIdArquivo(arquivoDesif.getId());
        if (!registros.isEmpty()) {
            DeclaracaoMensalServicoNfseDTO dms = new DeclaracaoMensalServicoNfseDTO();
            dms.setIdArquivoDesif(arquivoDesif.getId());
            dms.setExercicio(DateUtils.getAno(arquivoDesif.getInicioCompetencia()));
            dms.setMes(DateUtils.getMes(arquivoDesif.getInicioCompetencia()));
            dms.setTipoMovimento(TipoMovimentoMensalNfseDTO.INSTITUICAO_FINANCEIRA);
            dms.setPrestador(arquivoDesif.getPrestador());
            dms.setAbertura(new Date());
            dms.setEncerramento(new Date());
            dms.setLancadoPor(LancadoPorNfseDTO.CLIENTE);
            dms.setUsuarioDeclaracao(arquivoDesif.getEnviadoPor().getLogin());
            dms.setQtdNotas(0);
            dms.setTotalServicos(BigDecimal.ZERO);
            dms.setTotalIss(BigDecimal.ZERO);

            for (ArquivoDesifRegistro0430NfseDTO registro0430 : registros) {
                processarRegistro0430ArquivoDesif(dms, registro0430);
            }

            dms = declaracaoMensalServicoService.save(dms);
        }
    }

    private void processarRegistro0430ArquivoDesif(DeclaracaoMensalServicoNfseDTO dms,
                                                   ArquivoDesifRegistro0430NfseDTO registro0430) {
        DeclaracaoContaBancariaNfseDTO contaBancaria = new DeclaracaoContaBancariaNfseDTO();
        contaBancaria.setConta(registro0430.getConta());
        contaBancaria.setSaldoInicial(BigDecimal.ZERO);
        contaBancaria.setValorCredito(BigDecimal.ZERO);
        contaBancaria.setValorDebito(BigDecimal.ZERO);
        contaBancaria.setSaldoFinal(BigDecimal.ZERO);
        contaBancaria.setBaseCalculo(registro0430.getBaseCalculo());
        contaBancaria.setAliquotaIssqn(registro0430.getAliquota());
        dms.getContasBancarias().add(contaBancaria);
        dms.setQtdNotas(dms.getQtdNotas() + 1);
        dms.setTotalServicos(dms.getTotalServicos().add(contaBancaria.getBaseCalculo()));
        dms.setTotalIss(dms.getTotalIss().add(contaBancaria.getValorIss()));
    }

    public DeclaracaoMensalServicoNfseDTO importarArquivoDesifLegado(UploadCosifDTO dto) throws Exception {
        CabecalhoArquivoCosifDTO cabecalho = lerArquivoDesifLegado(dto);
        DeclaracaoMensalServicoNfseDTO dms = criarDmsArquivoDesifLegado(cabecalho);
        for (DetalheArquivoCosifDTO linha : cabecalho.getLinhas()) {
            criarAdicionarContaCosifDeclaracaoMensalServico(dms, linha);
        }
        return declaracaoMensalServicoService.save(dms);
    }

    private void criarAdicionarContaCosifDeclaracaoMensalServico(DeclaracaoMensalServicoNfseDTO declaracao,
                                                                 DetalheArquivoCosifDTO item) throws Exception {
        PrestadorServicoNfseDTO prestador = declaracao.getPrestador();
        String numeroConta = Util.removeZerosEsquerda(item.getNumeroConta());
        numeroConta = numeroConta.trim();
        PlanoGeralContasComentadoNfseDTO conta = planoGeralContasComentadoService.findByContaAndDesdobramentoAndReferencia(prestador.getId(),
                numeroConta, null, new Date());
        if (conta == null) {
            throw new NfseOperacaoNaoPermitidaException("Nenhuma Conta foi encontrada para o Prestador " + prestador.getInscricaoMunicipal() + " e código da conta " + numeroConta + ".");
        }
        DeclaracaoContaBancariaNfseDTO declaracaoContaBancaria = new DeclaracaoContaBancariaNfseDTO();
        declaracaoContaBancaria.setConta(conta);
        declaracaoContaBancaria.setBaseCalculo(item.getValorServico());
        declaracaoContaBancaria.setAliquotaIssqn(item.getValorAliquota());
        if (declaracao.getContasBancarias() == null) {
            declaracao.setContasBancarias(Lists.newArrayList());
        }
        declaracao.getContasBancarias().add(declaracaoContaBancaria);
    }

    private DeclaracaoMensalServicoNfseDTO criarDmsArquivoDesifLegado(CabecalhoArquivoCosifDTO cabecalho) {
        DeclaracaoMensalServicoNfseDTO dms = new DeclaracaoMensalServicoNfseDTO();
        dms.setCompetencia(cabecalho.getDataMovimento());
        dms.setAbertura(cabecalho.getDataMovimento());
        dms.setEncerramento(cabecalho.getDataMovimento());
        dms.setExercicio(cabecalho.getAno());
        dms.setMes(Integer.valueOf(cabecalho.getMes()));
        PrestadorServicoNfseDTO cadastroEconomico = cadastroEconomicoService.findByInscricaoMunicipal(cabecalho.getCnpj());
        if (cadastroEconomico == null) {
            throw new NfseOperacaoNaoPermitidaException("Nenhum Prestador foi encontrado com a inscrição municipal " + cabecalho.getCnpj() + ".");
        }
        dms.setPrestador(cadastroEconomico);
        dms.setQtdNotas(cabecalho.getTotalRegistro());
        dms.setSituacao(SituacaoDeclaracaoMensalServicoNfseDTO.ABERTO);
        dms.setTipo(TipoDeclaracaoMensalServicoNfseDTO.PRINCIPAL);
        dms.setTipoMovimento(TipoMovimentoMensalNfseDTO.INSTITUICAO_FINANCEIRA);
        dms.setTotalServicos(cabecalho.getValorTotalServico());
        dms.setTotalCorrecao(BigDecimal.ZERO);
        dms.setTotalIss(BigDecimal.ZERO);
        dms.setTotalJuros(BigDecimal.ZERO);
        dms.setTotalMulta(BigDecimal.ZERO);
        dms.setTotalIss(cabecalho.getValorTotalISSQN());
        return dms;
    }

    private CabecalhoArquivoCosifDTO lerArquivoDesifLegado(UploadCosifDTO dto) {
        try {
            String arquivo = Util.getBase64ToString(dto.getFile());
            InputStream base64ToInputStream = Util.getBase64ToInputStream(arquivo);
            return percorrerLinhasArquivoDesifLegado(base64ToInputStream);
        } catch (Exception e) {
            return null;
        }
    }

    private CabecalhoArquivoCosifDTO percorrerLinhasArquivoDesifLegado(InputStream inputStream) throws IOException {
        CabecalhoArquivoCosifDTO cabecalho = null;
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        int numeroDeLinha = 0;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            if (numeroDeLinha == 0) {
                cabecalho = processarCabecalhoArquivoDesifLegado(line);
            } else {
                DetalheArquivoCosifDTO item = processarDetalheArquivoDesifLegado(line);
                cabecalho.getLinhas().add(item);
            }
            numeroDeLinha++;
        }
        reader.close();
        streamReader.close();
        inputStream.close();
        return cabecalho;
    }

    private CabecalhoArquivoCosifDTO processarCabecalhoArquivoDesifLegado(String linha) {
        CabecalhoArquivoCosifDTO item = new CabecalhoArquivoCosifDTO();
        try {
            item.setIdentificacao(linha.substring(0, 11));
            item.setCnpj(linha.substring(11, 25));
            item.setRazaoSocial(linha.substring(25, 105));
            item.setMes(linha.substring(105, 107));
            item.setAno(Integer.valueOf(linha.substring(107, 111)));
            item.setDataMovimento(DateUtils.getData(linha.substring(111, 119), "yyyyMMdd"));
            item.setTotalRegistro(Integer.valueOf(linha.substring(119, 124)));
            String valorServico = linha.substring(124, 139);
            item.setValorTotalServico(parse(valorServico));
            String valorISSQN = linha.substring(139, 154);
            item.setValorTotalISSQN(parse(valorISSQN));
        } catch (Exception e) {
            log.error("Erro ao processar a linha", e);
        }
        return item;
    }

    private DetalheArquivoCosifDTO processarDetalheArquivoDesifLegado(String linha) {
        DetalheArquivoCosifDTO item = new DetalheArquivoCosifDTO();
        try {
            item.setSequenciaNumerica(linha.substring(0, 4));
            item.setNumeroConta(linha.substring(4, 19));
            item.setRecolheImposto(tratarRecolheImposto(linha.substring(19, 20)));
            String valorServico = linha.substring(20, 35);
            item.setValorServico(parse(valorServico));
            String valorAliquota = linha.substring(35, 41);
            item.setValorAliquota(parse(valorAliquota));
            String valorISSQN = linha.substring(41, 56);
            item.setValorISSQN(parse(valorISSQN));
        } catch (Exception e) {
            log.error("Erro ao processar a linha", e);
        }
        return item;
    }

    private DecimalFormat getDecimalFormat() {
        if (decimalFormat == null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator('.');
            String pattern = "#,##0.0#";
            decimalFormat = new DecimalFormat(pattern, symbols);
            decimalFormat.setParseBigDecimal(true);
        }
        return decimalFormat;
    }

    private BigDecimal parse(String valor) throws ParseException {
        return (BigDecimal) getDecimalFormat().parse(valor);
    }


    private Boolean tratarRecolheImposto(String recolheImposto) {
        if (recolheImposto.equals("S")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }


    public void imprimirRecibo(HttpServletResponse response, Long id) throws JRException, IOException {
        ArquivoDesifNfseDTO dto = findById(id);
        HashMap parametros = new HashMap();
        reportService.parametrosDefault(parametros);
        byte[] bytes = reportService.gerarRelatorioPdf("ReciboDesif.jrxml", parametros,
                Lists.newArrayList(dto));
        reportService.imprimirRelatorio(response, bytes, "ReciboDesif.pdf");
    }
}
