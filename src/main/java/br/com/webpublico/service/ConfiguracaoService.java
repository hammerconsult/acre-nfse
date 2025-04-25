package br.com.webpublico.service;

import br.com.webpublico.FileUtils;
import br.com.webpublico.config.Constants;
import br.com.webpublico.domain.dto.ConfiguracaoDividaNfseDTO;
import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.ConfiguracaoNfseParametroNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoDeclaracaoMensalServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoParametroNfseDTO;
import br.com.webpublico.repository.ConfiguracaoJDBCRepository;
import br.com.webpublico.util.Util;
import com.beust.jcommander.internal.Lists;
import com.google.common.base.Strings;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * Created by William on 18/01/2017.
 */
@Service
@Transactional
public class ConfiguracaoService extends AbstractWPService<ConfiguracaoNfseDTO> {
    private final Logger log = LoggerFactory.getLogger(ConfiguracaoService.class);

    private LocalTime ultimaSincronizacao;
    private ConfiguracaoNfseDTO configuracaoNfseDTO;
    @Autowired
    private CidadeService cidadeService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private DetentorArquivoComposicaoService detentorArquivoComposicaoService;
    @Autowired
    private ConfiguracaoJDBCRepository configuracaoJDBCRepository;
    @Inject
    private Environment env;

    @Override
    public String getTableName() {
        return "ConfiguracaoTributario";
    }

    @Override
    public String getDefaltSearchFields() {
        return "";
    }

    @Override
    public ParameterizedTypeReference<List<ConfiguracaoNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<ConfiguracaoNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<ConfiguracaoNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<ConfiguracaoNfseDTO>() {
        };
    }

    public synchronized ConfiguracaoNfseDTO getConfiguracaoFromServer() {
        if (configuracaoNfseDTO == null ||
                ultimaSincronizacao == null ||
                ultimaSincronizacao.isBefore(LocalTime.now().minusMinutes(10))) {
            configuracaoNfseDTO = configuracaoJDBCRepository.find();

            if (configuracaoNfseDTO.getMunicipio() != null) {
                configuracaoNfseDTO.setMunicipio(cidadeService.findById(configuracaoNfseDTO.getMunicipio().getId()));
            }

            if (configuracaoNfseDTO.getPadraoServicoPrestado() != null) {
                configuracaoNfseDTO.setPadraoServicoPrestado(
                        servicoService.findById(configuracaoNfseDTO.getPadraoServicoPrestado().getId()));
            }

            if (configuracaoNfseDTO.getArquivoBrasao() != null) {

                configuracaoNfseDTO.setArquivoBrasao(detentorArquivoComposicaoService.findById(
                        configuracaoNfseDTO.getArquivoBrasao().getId()));

                if (configuracaoNfseDTO.getArquivoBrasao().
                        getArquivo() != null) {
                    byte[] dados = Util.getByteArrayDosDados(configuracaoNfseDTO.getArquivoBrasao().
                            getArquivo().getArquivoNfseDTO().getPartes());
                    configuracaoNfseDTO.setLogoMunicipio("data:image/png;base64," +
                            FileUtils.getBase64Encode(dados));
                }
            }
            preencherParametros(configuracaoNfseDTO);
            ultimaSincronizacao = LocalTime.now();
        }
        return configuracaoNfseDTO;
    }


    private void preencherParametros(ConfiguracaoNfseDTO configuracaoNfseDTO) {
        List<ConfiguracaoNfseParametroNfseDTO> parametros = configuracaoJDBCRepository.findParametros(configuracaoNfseDTO.getId());
        configuracaoNfseDTO.setVerificarAidfe(getParametroBoolean(parametros, TipoParametroNfseDTO.OBRIGA_USO_AIDF));
        configuracaoNfseDTO.setEmissaoRetroativa(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_EMISSAO_RETROATIVA));
        configuracaoNfseDTO.setEmissaoRetroativaUltimaEmitida(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_EMISSAO_RETROATIVA_ULTIMA_EMITIDA));
        configuracaoNfseDTO.setVincularRpsNaEmissao(getParametroBoolean(parametros, TipoParametroNfseDTO.PERGUNTA_RPS_NA_EMISSAO_NFSE));
        configuracaoNfseDTO.setValidarDadosDeducaoNfse(getParametroBoolean(parametros, TipoParametroNfseDTO.VALIDA_DADOS_DEDUCAO_EMISSAO_NFSE));
        configuracaoNfseDTO.setValidarDmsAbertaMesesAnteriores(getParametroBoolean(parametros, TipoParametroNfseDTO.VALIDA_DMS_ABERTA_MESES_ANTERIORES));
        configuracaoNfseDTO.setPermiteSelcionarServicoNaoAutorizado(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_SELECIONAR_SERVICO_NAO_AUTORIZADO));
        configuracaoNfseDTO.setIsentaServicoIncorporacao(getParametroBoolean(parametros, TipoParametroNfseDTO.ISENTA_SERVICO_INCORPORACAO));
        configuracaoNfseDTO.setPermiteAlterarOpcoesEmailPerfilEmpresa(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_ALTERAR_OPCOES_ENVIO_EMAIL_PERFIL_EMPRESA));
        configuracaoNfseDTO.setPermiteServicoDeclaradoPrestado(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_SERVICO_DECLARADO_PRESTADO));
        configuracaoNfseDTO.setMensagemLogin(getParametroString(parametros, TipoParametroNfseDTO.MENSAGEM_LOGIN));
        configuracaoNfseDTO.setBloqueiaLogin(getParametroBoolean(parametros, TipoParametroNfseDTO.BLOQUEIA_LOGIN));
        configuracaoNfseDTO.setPermiteEmissaoIssFixoComDebitoVencido(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_EMISSAO_NFSE_ISS_FIXO_VENCIDO));
        configuracaoNfseDTO.setEnviaEmailEmHomologacao(getParametroBoolean(parametros, TipoParametroNfseDTO.ENVIA_EMAIL_HOMOLOGACAO));
        configuracaoNfseDTO.setPermiteEmissaoNfsAvulsa(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_EMISSAO_NFS_AVULSA));
        configuracaoNfseDTO.setPermiteEmissaoRetroativaUltimaEmitida(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_EMISSAO_RETROATIVA_ULTIMA_EMITIDA));
        configuracaoNfseDTO.setProducao(Objects.equals(env.getProperty("perfil.aplicacao"), "PRODUCAO"));
        configuracaoNfseDTO.setTiposRpsPermitido(getParametroString(parametros, TipoParametroNfseDTO.TIPOS_RPS_PERMITIDOS));
        configuracaoNfseDTO.setDiaLimiteCancelamento(getParametroInteger(parametros, TipoParametroNfseDTO.DIA_LIMITE_CANCELAMENTO_NOTA));
        configuracaoNfseDTO.setHabilitaPrimeiroAcesso(getParametroBoolean(parametros, TipoParametroNfseDTO.HABILITA_PRIMEIRO_ACESSO));
        configuracaoNfseDTO.setMensagemPrimeiroAcessoDesabilitado(getParametroString(parametros, TipoParametroNfseDTO.MENSAGEM_PRIMEIRO_ACESSO_DESABILITADO));
        configuracaoNfseDTO.setMensagemAutenticacao(getParametroString(parametros, TipoParametroNfseDTO.MENSAGEM_TELA_AUTENTICACAO));
        configuracaoNfseDTO.setQuantidadeCancelamentosPermitidos(getParametroInteger(parametros, TipoParametroNfseDTO.QUANTIDADE_SOLICITACAO_CANCELAMENTO));
        configuracaoNfseDTO.setPermiteCancelamentoForaPrazo(getParametroBoolean(parametros, TipoParametroNfseDTO.PERMITE_CANCELAMENTO_FORA_PRAZO));
        configuracaoNfseDTO.setTextoBloqueioCancelamentoForaPrazo(getParametroString(parametros, TipoParametroNfseDTO.TEXTO_BLOQUEIO_CANCELAMENTO_FORA_PRAZO));
        if (configuracaoNfseDTO.getProducao()) {
            configuracaoNfseDTO.setUrlNfse(getParametroString(parametros, TipoParametroNfseDTO.URL_APLICACAO_NFSE));
            configuracaoNfseDTO.setUrlAutenticao(getParametroString(parametros, TipoParametroNfseDTO.URL_AUTENTICACAO_NFSE));
        } else {
            configuracaoNfseDTO.setUrlNfse(getParametroString(parametros, TipoParametroNfseDTO.URL_APLICACAO_NFSE_HOMOLOGACAO));
            configuracaoNfseDTO.setUrlAutenticao(getParametroString(parametros, TipoParametroNfseDTO.URL_AUTENTICACAO_NFSE_HOMOLOGACAO));
        }
        configuracaoNfseDTO.setDiaLimiteCancelamentoEncerramentoMensal(getParametroInteger(parametros, TipoParametroNfseDTO.DIA_LIMITE_CANCELAMENTO_ENCERRAMENTO_MENSAL));
        configuracaoNfseDTO.setUtilizaBancoCache(getParametroBoolean(parametros, TipoParametroNfseDTO.UTILIZA_BANCO_CACHE));
        configuracaoNfseDTO.setPrazoAvisoNovaMensagem(getParametroInteger(parametros, TipoParametroNfseDTO.PRAZO_AVISO_NOVA_MENSAGEM));
        configuracaoNfseDTO.setGruposObrigatoriosCosif(getParametroListString(parametros, TipoParametroNfseDTO.GRUPOS_COSIF_OBRIGATORIOS));
        configuracaoNfseDTO.setUrlNotaPremiada(getParametroString(parametros, TipoParametroNfseDTO.URL_APLICACAO_NOTA_PREMIADA));
    }

    public boolean getParametroBoolean(List<ConfiguracaoNfseParametroNfseDTO> parametros, TipoParametroNfseDTO tipo) {
        String valor = getParametroString(parametros, tipo);
        return Strings.isNullOrEmpty(valor) ? false : Boolean.valueOf(valor);
    }

    public String getParametroString(List<ConfiguracaoNfseParametroNfseDTO> parametros, TipoParametroNfseDTO tipo) {
        for (ConfiguracaoNfseParametroNfseDTO parametro : parametros) {
            if (tipo.equals(parametro.getTipoParametroNfseDTO())) {
                return parametro.getValor();
            }
        }
        return null;
    }

    public List<String> getParametroListString(List<ConfiguracaoNfseParametroNfseDTO> parametros,
                                               TipoParametroNfseDTO tipo) {
        String parametroString = getParametroString(parametros, tipo);
        if (!Strings.isNullOrEmpty(parametroString)) {
            return Lists.newArrayList(parametroString.split(","));
        }
        return Lists.newArrayList();
    }

    public Integer getParametroInteger(List<ConfiguracaoNfseParametroNfseDTO> parametros, TipoParametroNfseDTO tipo) {
        for (ConfiguracaoNfseParametroNfseDTO parametro : parametros) {
            if (tipo.equals(parametro.getTipoParametroNfseDTO())) {
                if (!Strings.isNullOrEmpty(parametro.getValor())) {
                    return new Integer(parametro.getValor());
                }
                return null;
            }
        }
        return null;
    }

    public void imprimirPdf(HttpServletResponse response, String conteudo) {
        try {
            String url = urlsProperties.getWebpublicoPathNfse() + "/gerar-pdf";
            url = UriComponentsBuilder.fromUriString(url)
                    .queryParam("conteudo", conteudo)
                    .toUriString();

            ResponseEntity<byte[]> exchange = restTemplate.postForEntity(url, conteudo, byte[].class);
            byte[] bytes = exchange.getBody();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=ISS-Online.pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("Não foi possível imprimir o relatorio ", e);
        }
    }

    public ConfiguracaoDividaNfseDTO getConfiguracaoDivida(ConfiguracaoNfseDTO configuracao,
                                                           TipoMovimentoMensalNfseDTO tipoMovimento,
                                                           TipoDeclaracaoMensalServicoNfseDTO tipoDeclaracaoMensalServico) {
        return configuracaoJDBCRepository.getConfiguracaoDivida(configuracao, tipoMovimento, tipoDeclaracaoMensalServico);
    }
}
