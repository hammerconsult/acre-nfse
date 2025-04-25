package br.com.webpublico.domain.dto;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoIssqnNfseDTO;
import br.com.webpublico.domain.dto.nfse12.*;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import br.com.webpublico.domain.dto.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.RowMapper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotaFiscalNfseDTO extends AbstractEntity implements RowMapper<NotaFiscalNfseDTO> {


    private Long numero;
    private String codigoVerificacao;
    private DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico;
    private RpsNfseDTO rps;
    private Date emissao;
    private Date ultimaEmissao;
    private SituacaoDeclaracaoNfseDTO situacao;
    private String emails;
    private String informacoesAdicionais;
    private ModalidadeEmissao modalidade;
    private BigDecimal totalServicos;
    private BigDecimal totalNota;
    private BigDecimal deducoesLegais;
    private BigDecimal descontosIncondicionais;
    private BigDecimal descontosCondicionais;
    private BigDecimal retencoesFederais;
    private BigDecimal baseCalculo;
    private BigDecimal issCalculado;
    private BigDecimal issPagar;
    private BigDecimal valorLiquido;
    private PrestadorServicoNfseDTO prestador;
    private Boolean tomadorHabitual;
    private TomadorServicoDTO tomador;
    private String descriminacaoServico;
    private String chaveAcesso;
    private BigDecimal aliquotaServico;
    private Boolean homologacao;
    private Boolean substitutoTributario;
    private Boolean enviouPorEmail;
    private String numeroDAM;

    public NotaFiscalNfseDTO() {
        this.totalServicos = BigDecimal.ZERO;
        this.totalNota = BigDecimal.ZERO;
        this.deducoesLegais = BigDecimal.ZERO;
        this.descontosIncondicionais = BigDecimal.ZERO;
        this.descontosCondicionais = BigDecimal.ZERO;
        this.retencoesFederais = BigDecimal.ZERO;
        this.baseCalculo = BigDecimal.ZERO;
        this.issCalculado = BigDecimal.ZERO;
        this.issPagar = BigDecimal.ZERO;
        this.valorLiquido = BigDecimal.ZERO;
        this.aliquotaServico = BigDecimal.ZERO;
        this.homologacao = false;
        this.tomadorHabitual = Boolean.FALSE;
        this.substitutoTributario = Boolean.FALSE;
        this.enviouPorEmail = Boolean.FALSE;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao != null ? codigoVerificacao.toUpperCase() : " ";
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public DeclaracaoPrestacaoServicoNfseDTO getDeclaracaoPrestacaoServico() {
        return declaracaoPrestacaoServico;
    }

    public void setDeclaracaoPrestacaoServico(DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico) {
        this.declaracaoPrestacaoServico = declaracaoPrestacaoServico;
    }

    public SituacaoDeclaracaoNfseDTO getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoDeclaracaoNfseDTO situacao) {
        this.situacao = situacao;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    public Date getUltimaEmissao() {
        return ultimaEmissao;
    }

    public void setUltimaEmissao(Date ultimaEmissao) {
        this.ultimaEmissao = ultimaEmissao;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getTotalNota() {
        return totalNota;
    }

    public void setTotalNota(BigDecimal totalNota) {
        this.totalNota = totalNota;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getDeducoesLegais() {
        return deducoesLegais;
    }

    public void setDeducoesLegais(BigDecimal deducoesLegais) {
        this.deducoesLegais = deducoesLegais;
    }

    public BigDecimal getDescontosIncondicionais() {
        return descontosIncondicionais != null ? descontosIncondicionais : BigDecimal.ZERO;
    }

    public void setDescontosIncondicionais(BigDecimal descontosIncondicionais) {
        this.descontosIncondicionais = descontosIncondicionais;
    }

    public BigDecimal getDescontosCondicionais() {
        return descontosCondicionais != null ? descontosCondicionais : BigDecimal.ZERO;
    }

    public void setDescontosCondicionais(BigDecimal descontosCondicionais) {
        this.descontosCondicionais = descontosCondicionais;
    }

    public BigDecimal getIssCalculado() {
        return issCalculado;
    }

    public void setIssCalculado(BigDecimal issCalculado) {
        this.issCalculado = issCalculado;
    }

    public BigDecimal getIssPagar() {
        return issPagar;
    }

    public void setIssPagar(BigDecimal issPagar) {
        this.issPagar = issPagar;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public ModalidadeEmissao getModalidade() {
        return modalidade;
    }

    public void setModalidade(ModalidadeEmissao modalidade) {
        this.modalidade = modalidade;
    }

    public RpsNfseDTO getRps() {
        return rps;
    }

    public void setRps(RpsNfseDTO rps) {
        this.rps = rps;
    }

    public BigDecimal getRetencoesFederais() {
        return retencoesFederais;
    }

    public void setRetencoesFederais(BigDecimal retencoesFederais) {
        this.retencoesFederais = retencoesFederais;
    }

    public BigDecimal getAliquotaServico() {
        return aliquotaServico;
    }

    public void setAliquotaServico(BigDecimal aliquotaServico) {
        this.aliquotaServico = aliquotaServico;
    }

    @JsonIgnore
    public boolean tomadorJaCadastrado() {
        return this.getDeclaracaoPrestacaoServico() != null &&
                this.getTomador() != null &&
                this.getTomador() != null &&
                this.getTomador().getId() != null;
    }

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public Boolean getTomadorHabitual() {
        return tomadorHabitual;
    }

    public void setTomadorHabitual(Boolean tomadorHabitual) {
        this.tomadorHabitual = tomadorHabitual;
    }

    public TomadorServicoDTO getTomador() {
        return tomador;
    }

    public void setTomador(TomadorServicoDTO tomador) {
        this.tomador = tomador;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public boolean intermediarioJaCadastrado() {
        return this.getDeclaracaoPrestacaoServico() != null &&
                this.getDeclaracaoPrestacaoServico().getIntermediario() != null &&
                this.getDeclaracaoPrestacaoServico().getIntermediario() != null &&
                this.getDeclaracaoPrestacaoServico().getIntermediario().getId() != null;
    }

    public String getDescriminacaoServico() {
        return descriminacaoServico;
    }

    public void setDescriminacaoServico(String descriminacaoServico) {
        this.descriminacaoServico = descriminacaoServico;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public boolean getHomologacao() {
        return homologacao;
    }

    public void setHomologacao(boolean homologacao) {
        this.homologacao = homologacao;
    }

    public Boolean getSubstitutoTributario() {
        return substitutoTributario;
    }

    public void setSubstitutoTributario(Boolean substitutoTributario) {
        this.substitutoTributario = substitutoTributario;
    }

    public Boolean getEnviouPorEmail() {
        return enviouPorEmail;
    }

    public void setEnviouPorEmail(Boolean enviouPorEmail) {
        this.enviouPorEmail = enviouPorEmail;
    }

    public String getNumeroDAM() {
        return numeroDAM;
    }

    public void setNumeroDAM(String numeroDAM) {
        this.numeroDAM = numeroDAM;
    }

    @JsonIgnore
    public BigDecimal getDescontos() {
        BigDecimal descontos = BigDecimal.ZERO;
        if (descontosCondicionais != null) {
            descontos = descontos.add(descontosCondicionais);
        }
        if (descontosCondicionais != null) {
            descontos = descontos.add(descontosIncondicionais);
        }
        return descontos;
    }

    @JsonIgnore
    public String gerarInformacoesAdicionais(MunicipioNfseDTO municipio, Date vencimentoIss) {
        ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico = declaracaoPrestacaoServico.getItens().get(0);

        String informacoesAdicionais = "";
        informacoesAdicionais += " - Natureza Operação: " + declaracaoPrestacaoServico.getNaturezaOperacao().getDescricao() + ".\n";
        if (!ExigibilidadeNfseDTO.NAO_INCIDENCIA.equals(declaracaoPrestacaoServico.getNaturezaOperacao()) &&
                !ExigibilidadeNfseDTO.IMUNIDADE.equals(declaracaoPrestacaoServico.getNaturezaOperacao()) &&
                !ExigibilidadeNfseDTO.ISENCAO.equals(declaracaoPrestacaoServico.getNaturezaOperacao())) {
            if (!ExigibilidadeNfseDTO.TRIBUTACAO_FORA_MUNICIPIO.equals(declaracaoPrestacaoServico.getNaturezaOperacao()) &&
                    !ExigibilidadeNfseDTO.EXPORTACAO.equals(declaracaoPrestacaoServico.getNaturezaOperacao())) {
                informacoesAdicionais += " - ISS de responsabilidade do: " + responsabilidadeIssqn() + ".\n";
            }
            if (!itemDeclaracaoServico.getServico().getPermiteRecolhimentoFora()) {
                informacoesAdicionais += " - Serviço tributado no município: " +
                        (municipio != null ? municipio.getNome() + " - " + municipio.getEstado() : "") + ".\n";
            } else {
                if (Boolean.TRUE.equals(itemDeclaracaoServico.getPrestadoNoPais())) {
                    informacoesAdicionais += " - Serviço tributado no município: " + (itemDeclaracaoServico.getMunicipio() != null ?
                            itemDeclaracaoServico.getMunicipio().getNome() + " - " + itemDeclaracaoServico.getMunicipio().getEstado() : "") + ".\n";
                } else {
                    informacoesAdicionais += " - Serviço tributado no país: " + (itemDeclaracaoServico.getPais() != null ? itemDeclaracaoServico.getPais().getNome() : "") + ".\n";
                }
            }
            if (!ExigibilidadeNfseDTO.TRIBUTACAO_FORA_MUNICIPIO.equals(declaracaoPrestacaoServico.getNaturezaOperacao()) &&
                    !ExigibilidadeNfseDTO.EXPORTACAO.equals(declaracaoPrestacaoServico.getNaturezaOperacao()) &&
                    !ExigibilidadeNfseDTO.SIMPLES_NACIONAL.equals(declaracaoPrestacaoServico.getNaturezaOperacao()))
                informacoesAdicionais += " - Data de vencimento do ISS desta NFSE: " + DateUtils.getDataFormatada(vencimentoIss) + ".\n";
        }

        if (prestador.getEnquadramentoFiscal() != null && TipoIssqnNfseDTO.SIMPLES_NACIONAL.equals(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getTipoIssqn()) &&
                !declaracaoPrestacaoServico.getIssRetido()) {
            informacoesAdicionais += " - ISSQN a ser calculado pela Tabela da LC 123/Simples Nacional e pago na guia DAS/Simples.\n ";
        }

        if (rps != null) {
            informacoesAdicionais += " - RPS n°: " + rps.getNumero() + ".\n";
            informacoesAdicionais += " - Data de Emissão RPS: " + (rps != null ? DateUtils.getDataFormatada(rps.getDataEmissao()) : "") + ".\n";
        }

        informacoesAdicionais += " - Código Verificação: " + codigoVerificacao + ".\n";

        if (prestador.getEnquadramentoFiscal() != null && TipoIssqnNfseDTO.SIMPLES_NACIONAL.equals(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getTipoIssqn())) {
            informacoesAdicionais += " - DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL.\n";
        }

        if (declaracaoPrestacaoServico.getNaturezaOperacao().equals(ExigibilidadeNfseDTO.NAO_INCIDENCIA)) {
            informacoesAdicionais += " - Operação não gera valor de ISSQN. Contribuinte Fixo, MEI ou Estimado.\n";
        }

        if (declaracaoPrestacaoServico.getNaturezaOperacao().equals(ExigibilidadeNfseDTO.IMUNIDADE)) {
            informacoesAdicionais += " - Operação não gera valor de ISSQN. Contribuinte no regime de recolhimento imune.\n";
        }

        if (declaracaoPrestacaoServico.getNaturezaOperacao().equals(ExigibilidadeNfseDTO.ISENCAO)) {
            informacoesAdicionais += " - Operação não gera valor de ISSQN. Contribuinte no regime de recolhimento isento.\n";
        }

        if (declaracaoPrestacaoServico.getNaturezaOperacao().equals(ExigibilidadeNfseDTO.RETENCAO) && Boolean.TRUE.equals(substitutoTributario)) {
            informacoesAdicionais += " - Operação com retenção de ISS por Substituição Tributária.\n";
        }

        if (prestador.getEnquadramentoFiscal() != null && TipoIssqnNfseDTO.SUBLIMITE_ULTRAPASSADO.equals(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getTipoIssqn())) {
            informacoesAdicionais += " - ESTABELECIMENTO IMPEDIDO DE RECOLHER O ICMS/ISS PELO SIMPLES NACIONAL,\n" +
                    " NOS TERMOS DO § 1º DO ART. 20 DA LEI COMPLEMENTAR Nº 123, DE 2006.\n";
        }
        return informacoesAdicionais;
    }

    @JsonIgnore
    private String responsabilidadeIssqn() {
        if (declaracaoPrestacaoServico.getNaturezaOperacao().equals(ExigibilidadeNfseDTO.NAO_INCIDENCIA)) {
            return "Operação não gera valor de ISSQN. Contribuinte MEI, Fixo Anual, Estimado, Imune ou Isento.";
        } else if (Boolean.TRUE.equals(declaracaoPrestacaoServico.getIssRetido())) {
            return "Tomador de Serviço";
        } else {
            return "Prestador de Serviço";
        }
    }

    @Override
    public NotaFiscalNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        NotaFiscalNfseDTO dto = new NotaFiscalNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setNumero(resultSet.getLong("numero"));
        dto.setCodigoVerificacao(resultSet.getString("codigoverificacao"));
        dto.setDeclaracaoPrestacaoServico(new DeclaracaoPrestacaoServicoNfseDTO());
        dto.getDeclaracaoPrestacaoServico().setId(resultSet.getLong("declaracaoprestacaoservico_id"));
        dto.setEmissao(resultSet.getDate("emissao"));
        dto.setEmails(resultSet.getString("emails"));
        if (resultSet.getLong("rps_id") != 0) {
            dto.setRps(new RpsNfseDTO());
            dto.getRps().setId(resultSet.getLong("rps_id"));
        }
        if (resultSet.getLong("tomador_id") != 0) {
            dto.setTomador(new TomadorServicoDTO());
            dto.getTomador().setId(resultSet.getLong("tomador_id"));
        }
        dto.setPrestador(new PrestadorServicoNfseDTO());
        dto.getPrestador().setId(resultSet.getLong("prestador_id"));
        dto.setDescriminacaoServico(resultSet.getString("descriminacaoservico"));
        dto.setChaveAcesso(resultSet.getString("chaveacesso"));
        dto.setHomologacao(resultSet.getBoolean("homologacao"));
        dto.setInformacoesAdicionais(resultSet.getString("informacoesadicionais"));
        dto.setSubstitutoTributario(resultSet.getBoolean("substitutotributario"));
        if (resultSet.getString("situacao") != null &&
                !resultSet.getString("situacao").isEmpty()) {
            dto.setSituacao(SituacaoDeclaracaoNfseDTO.valueOf(resultSet.getString("situacao")));
        }
        if (resultSet.getString("modalidade") != null &&
                !resultSet.getString("modalidade").isEmpty()) {
            dto.setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao.valueOf(resultSet.getString("modalidade")));
        }
        dto.setTotalServicos(resultSet.getBigDecimal("totalservicos"));
        dto.setDeducoesLegais(resultSet.getBigDecimal("deducoeslegais"));
        dto.setDescontosCondicionais(resultSet.getBigDecimal("descontoscondicionais"));
        dto.setDescontosIncondicionais(resultSet.getBigDecimal("descontosincondicionais"));
        dto.setRetencoesFederais(resultSet.getBigDecimal("retencoesfederais"));
        dto.setBaseCalculo(resultSet.getBigDecimal("basecalculo"));
        dto.setIssCalculado(resultSet.getBigDecimal("isscalculado"));
        dto.setIssPagar(resultSet.getBigDecimal("isspagar"));
        dto.setValorLiquido(resultSet.getBigDecimal("valorliquido"));
        dto.setAliquotaServico(resultSet.getBigDecimal("aliquotaservico"));
        dto.setTotalNota(resultSet.getBigDecimal("totalnota"));
        return dto;
    }

    public enum ModalidadeEmissao {
        IDENTIFICADO, SEM_CPF, NAO_IDENTIFICADO;
    }

    public ImpressaoNotaFiscalNfseDTO toImpressaoNfse(ConfiguracaoNfseDTO configuracaoNfse, Date vencimento) {

        ImpressaoNotaFiscalNfseDTO impressao = new ImpressaoNotaFiscalNfseDTO();
        impressao.setId(id);
        impressao.setNumero(numero);
        impressao.setEmissao(emissao);
        if (DateUtils.hasHourOrMinute(emissao))
            impressao.setEmissaoFormatada(DateUtils.getDataFormatada(emissao, "dd/MM/yyyy HH:mm"));
        else
            impressao.setEmissaoFormatada(DateUtils.getDataFormatada(emissao, "dd/MM/yyyy"));
        if (declaracaoPrestacaoServico != null && declaracaoPrestacaoServico.getDadosPessoaisPrestador() != null) {
            impressao.setPrestadorNomeFantasia(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getNomeFantasia());
            impressao.setPrestadorRazaoSocial(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getNomeRazaoSocial());
            impressao.setPrestadorCnpjCpf(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getCpfCnpj());
            impressao.setPrestadorCadastroGeral(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getInscricaoMunicipal());
            impressao.setPrestadorEmail(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getEmail());
            impressao.setPrestadorTelefone(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getTelefone());
            impressao.setPrestadorLogradouro(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getLogradouro());
            impressao.setPrestadorBairro(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getBairro());
            impressao.setPrestadorNumero(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getNumero());
            impressao.setPrestadorComplemento(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getComplemento());
            if (declaracaoPrestacaoServico.getDadosPessoaisPrestador().getMunicipio() != null) {
                impressao.setPrestadorCidade(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getMunicipio().getNome());
                impressao.setPrestadorUF(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getMunicipio().getEstado());
            }
            impressao.setPrestadorCEP(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getCep());
            impressao.setPrestadorInscricaoEstadual(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getInscricaoEstadualRg());
            impressao.setPrestadorInscricaoMunicipal(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getInscricaoMunicipal());
        }

        if (informacoesAdicionais == null || informacoesAdicionais.isEmpty()) {
            impressao.setMensagemRodape(gerarInformacoesAdicionais(configuracaoNfse.getMunicipio(), vencimento));
        } else {
            impressao.setMensagemRodape(informacoesAdicionais);
        }

        if (getRps() != null && getRps().getId() != null) {
            impressao.setNumeroRPS(getRps().getNumero());
            impressao.setSerieRPS(getRps().getSerie());
            impressao.setEmissaoRPS(getRps().getDataEmissao());
        }

        if (declaracaoPrestacaoServico.getDadosPessoaisPrestador().getTipoIssqn() != null)
            impressao.setRegimeFiscalDesc(declaracaoPrestacaoServico.getDadosPessoaisPrestador().getTipoIssqn().getDescricao());

        if (declaracaoPrestacaoServico != null && tomador != null && declaracaoPrestacaoServico.getDadosPessoaisTomador() != null) {
            impressao.setTomadorRazaoSocial(declaracaoPrestacaoServico.getDadosPessoaisTomador().getNomeRazaoSocial());
            impressao.setTomadorCadastroGeral(declaracaoPrestacaoServico.getDadosPessoaisTomador().getInscricaoMunicipal());
            impressao.setTomadorInscricaoEstadual(declaracaoPrestacaoServico.getDadosPessoaisTomador().getInscricaoEstadualRg());
            impressao.setTomadorCnpjCpf(declaracaoPrestacaoServico.getDadosPessoaisTomador().getCpfCnpj());
            impressao.setTomadorLogradouro(declaracaoPrestacaoServico.getDadosPessoaisTomador().getLogradouro());
            impressao.setTomadorBairro(declaracaoPrestacaoServico.getDadosPessoaisTomador().getBairro());
            impressao.setTomadorComplemento(declaracaoPrestacaoServico.getDadosPessoaisTomador().getComplemento());
            if (declaracaoPrestacaoServico.getDadosPessoaisTomador().getMunicipio() != null) {
                impressao.setTomadorCidade(declaracaoPrestacaoServico.getDadosPessoaisTomador().getMunicipio().getNome());
                impressao.setTomadorUF(declaracaoPrestacaoServico.getDadosPessoaisTomador().getMunicipio().getEstado());
            }
            if (declaracaoPrestacaoServico.getDadosPessoaisTomador().getPais() != null) {
                impressao.setTomadorPais(declaracaoPrestacaoServico.getDadosPessoaisTomador().getPais().getNome());
            }
            impressao.setTomadorCEP(declaracaoPrestacaoServico.getDadosPessoaisTomador().getCep());
            impressao.setTomadorNumero(declaracaoPrestacaoServico.getDadosPessoaisTomador().getNumero());
            impressao.setTomadorInscricaoMunicipal(declaracaoPrestacaoServico.getDadosPessoaisTomador().getInscricaoMunicipal());
            impressao.setTomadorEmail(declaracaoPrestacaoServico.getDadosPessoaisTomador().getEmail());
        }

        if (declaracaoPrestacaoServico != null) {
            if (declaracaoPrestacaoServico.getItens() != null && !declaracaoPrestacaoServico.getItens().isEmpty()) {
                ItemDeclaracaoServicoNfseDTO item = declaracaoPrestacaoServico.getItens().get(0);
                if (item.getServico() != null) {
                    impressao.setCodigoListaServico(item.getServico().getCodigo());
                    impressao.setCodigoListaServicoDesc(item.getServico().getDescricao());
                }
                if (item.getCnae() != null) {
                    impressao.setCodigoCnae(item.getCnae().getCodigo());
                }
                if (item.getMunicipio() != null && !Strings.isNullOrEmpty(item.getMunicipio().getNome()))
                    impressao.setLocalPrestacao(item.getMunicipio().getNome() + " - " + item.getMunicipio().getEstado());
                if (item.getPais() != null && !Strings.isNullOrEmpty(item.getPais().getNome()))
                    impressao.setLocalPrestacao(item.getPais().getNome());
                impressao.setObservacao(item.getObservacoes());

                ImpressaoNotaFiscalNfseItemDTO itemDTO = new ImpressaoNotaFiscalNfseItemDTO();
                itemDTO.setTributavel(item.getDeducao() ? "Não" : "Sim");
                itemDTO.setDescricao(item.getDescricao());
                itemDTO.setQuantidade(item.getQuantidade());

                itemDTO.setValorUnitario(item.getValorServico());
                itemDTO.setValorDesconto((this.getDescontosCondicionais() != null ? this.getDescontosCondicionais() : BigDecimal.ZERO)
                        .add(this.getDescontosIncondicionais() != null ? this.getDescontosIncondicionais() : BigDecimal.ZERO));

                itemDTO.setDetalhes(new ArrayList());
                if (item.getDetalhes() != null) {
                    for (DetalheItemDeclaracaoServicoNfseDTO detalhe : item.getDetalhes()) {
                        ImpressaoNotaFiscalNfseItemDetalheDTO itemDetalheDTO = new ImpressaoNotaFiscalNfseItemDetalheDTO();
                        itemDetalheDTO.setDescricao(detalhe.getDescricao());
                        itemDetalheDTO.setQuantidade(detalhe.getQuantidade());
                        itemDetalheDTO.setValorServico(detalhe.getValorServico());
                        itemDTO.getDetalhes().add(itemDetalheDTO);
                    }
                }

                impressao.setItem(itemDTO);
            }


            if (declaracaoPrestacaoServico.getCompetencia() != null) {
                impressao.setMesCompetencia(String.valueOf(Util.getMes(declaracaoPrestacaoServico.getCompetencia())));
                impressao.setAnoCompetencia(String.valueOf(Util.getAno(declaracaoPrestacaoServico.getCompetencia())));
            }

            if (declaracaoPrestacaoServico.getConstrucaoCivil() != null) {
                impressao.setCodigoObra(declaracaoPrestacaoServico.getConstrucaoCivil().getCodigoObra().toString());
                if (declaracaoPrestacaoServico.getConstrucaoCivil().getIncorporacao() == null) {
                    impressao.setIncorporacao("NÃO");
                } else {
                    impressao.setIncorporacao(declaracaoPrestacaoServico.getConstrucaoCivil().getIncorporacao() ? "SIM" : "NÃO");
                }
                impressao.setArt(declaracaoPrestacaoServico.getConstrucaoCivil().getArt());
            }

            if (situacao != null) {
                impressao.setSituacaoNotaDesc(situacao.getDescricao());
            }

            if (declaracaoPrestacaoServico.getNaturezaOperacao() != null) {
                impressao.setNaturezaOperacaoDesc(declaracaoPrestacaoServico.getNaturezaOperacao().getDescricao());
            }

            impressao.setDiscriminacao(descriminacaoServico);
            impressao.setRetencao(declaracaoPrestacaoServico.getIssRetido());
            if (declaracaoPrestacaoServico.getTributosFederais() != null) {
                TributosFederaisNfseDTO tributosFederais = declaracaoPrestacaoServico.getTributosFederais();
                impressao.setIssqn(new ImpressaoNotaFiscalNfseImpostoDTO("ISSQN", declaracaoPrestacaoServico.getItens().get(0).getAliquotaServico(), getIssCalculado(), declaracaoPrestacaoServico.getIssRetido()));
                impressao.setCofins(new ImpressaoNotaFiscalNfseImpostoDTO("COFINS", tributosFederais.getPercentualCofins(), tributosFederais.getCofins(), tributosFederais.getRetencaoCofins()));
                impressao.setCpp(new ImpressaoNotaFiscalNfseImpostoDTO("CPP", tributosFederais.getPercentualCpp(), tributosFederais.getCpp(), tributosFederais.getRetencaoCpp()));
                impressao.setCsll(new ImpressaoNotaFiscalNfseImpostoDTO("CSLL", tributosFederais.getPercentualCsll(), tributosFederais.getCsll(), tributosFederais.getRetencaoCsll()));
                impressao.setInss(new ImpressaoNotaFiscalNfseImpostoDTO("INSS", tributosFederais.getPercentualInss(), tributosFederais.getInss(), tributosFederais.getRetencaoInss()));
                impressao.setIrrf(new ImpressaoNotaFiscalNfseImpostoDTO("IRRF", tributosFederais.getPercentualIrrf(), tributosFederais.getIrrf(), tributosFederais.getRetencaoIrrf()));
                impressao.setPis(new ImpressaoNotaFiscalNfseImpostoDTO("PIS", tributosFederais.getPercentualPis(), tributosFederais.getPis(), tributosFederais.getRetencaoPis()));
                impressao.setOutrasRetencoes(new ImpressaoNotaFiscalNfseImpostoDTO("Outras Retenções", tributosFederais.getPercentualOutrasRetencoes(), tributosFederais.getOutrasRetencoes(), tributosFederais.getRetencaoOutrasRetencoes()));
                impressao.setTotalTributos(new ImpressaoNotaFiscalNfseImpostoDTO("Total dos Tributos", null, tributosFederais.getTotalRetencoes().add(declaracaoPrestacaoServico.getIssRetido() ? getIssCalculado() : BigDecimal.ZERO), null));
            }
            impressao.setTotalServicos(getTotalServicos());
            impressao.setBaseCalculo(getBaseCalculo());
            impressao.setDescontoCondicionado(getDescontosCondicionais());
            impressao.setDescontoIncondicionado(getDescontosIncondicionais());
            impressao.setDeducoes(getDeducoesLegais());
            impressao.setValorLiquido(getValorLiquido());
            impressao.setTotalNota(getTotalNota());

            impressao.setCodigoVerificacao(getCodigoVerificacao());
        }
        impressao.setHomologacao(this.homologacao || !configuracaoNfse.getProducao());
        if (declaracaoPrestacaoServico.getNaturezaOperacao().equals(ExigibilidadeNfseDTO.NAO_INCIDENCIA)) {
            impressao.setResponsavelIss("Operação não gera valor de ISSQN. Contribuinte MEI, Fixo Anual, Estimado, Imune ou Isento.");
        } else if (Boolean.TRUE.equals(declaracaoPrestacaoServico.getIssRetido())) {
            impressao.setResponsavelIss("Tomador de Serviço");
        } else {
            impressao.setResponsavelIss("Prestador de Serviço");
        }
        return impressao;
    }

    public NotaFiscalSearchDTO toSearch() {
        NotaFiscalSearchDTO dto = new NotaFiscalSearchDTO();
        dto.setId(getId());
        dto.setIdPrestador(getPrestador().getId());
        if (getTomador() != null && getTomador().getId() != null) {
            dto.setIdTomador(getTomador().getId());
            dto.setNomeTomador(getTomador().getDadosPessoais().getNomeRazaoSocial());
            dto.setCpfCnpjTomador(getTomador().getDadosPessoais().getCpfCnpj());
        }
        if (getRps() != null) {
            dto.setIdRps(getRps().getId());
            dto.setCodigoRps(getRps().getNumero());
            dto.setSerieRps(getRps().getSerie());
            dto.setTipoRps(getRps().getTipoRps() != null ? getRps().getTipoRps().getDescricao() : "");
        }
        dto.setNumero(getNumero());
        dto.setEmissao(getEmissao());
        dto.setNomePrestador(getPrestador().getPessoa().getDadosPessoais().getNomeRazaoSocial());
        dto.setCpfCnpjPrestador(getPrestador().getPessoa().getDadosPessoais().getCpfCnpj());
        dto.setSituacao(getSituacao().name());
        dto.setModalidade(getModalidade().name());
        dto.setIssRetido(getDeclaracaoPrestacaoServico().getIssRetido());
        dto.setTotalServicos(getTotalServicos());
        dto.setDeducoes(getDeducoesLegais());
        dto.setBaseCalculo(getBaseCalculo());
        dto.setIss(getIssPagar());
        dto.setIssCalculado(getIssCalculado());
        dto.setDiscriminacaoServico(getDescriminacaoServico());
        return dto;
    }

}
