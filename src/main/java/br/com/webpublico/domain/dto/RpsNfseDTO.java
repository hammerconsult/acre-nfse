package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.*;
import br.com.webpublico.domain.dto.exception.TipoValidacao;
import br.com.webpublico.domain.dto.util.ValorPositivo;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class RpsNfseDTO implements NfseDTO {


    private Long id;
    private TipoRpsNfseDTO tipoRps;
    private String numero;
    private String numeroLote;
    private Long idNotaFiscal;
    private Long idLote;
    private String numeroNotaFiscal;
    private SituacaoDeclaracaoNfseDTO situacao;
    private String serie;
    private Date dataEmissao;
    private PrestadorServicoNfseDTO prestador;
    private ExigibilidadeNfseDTO naturezaOperacao;
    private Boolean optanteSimplesNacional;
    private Date competencia;
    private DadosPessoaisNfseDTO dadosPessoaisPrestador;
    private DadosPessoaisNfseDTO dadosPessoaisTomador;
    private List<ItemDeclaracaoServicoNfseDTO> itens;
    private TributosFederaisNfseDTO tributosFederais;
    private ResponsavelRetencaoNfseDTO responsavelRetencao;
    private Boolean issRetido;
    @ValorPositivo(tipoValidacao = TipoValidacao.E018, permiteZero = false)
    private BigDecimal totalServicos;
    @ValorPositivo(tipoValidacao = TipoValidacao.E018, permiteZero = false)
    private BigDecimal totalNota;
    @ValorPositivo(tipoValidacao = TipoValidacao.E020)
    private BigDecimal deducoesLegais;
    @ValorPositivo(tipoValidacao = TipoValidacao.E022)
    private BigDecimal descontosIncondicionais;
    @ValorPositivo(tipoValidacao = TipoValidacao.E022)
    private BigDecimal descontosCondicionais;
    private BigDecimal retencoesFederais;
    private BigDecimal baseCalculo;
    private BigDecimal issCalculado;
    private BigDecimal issPagar;
    private BigDecimal valorLiquido;
    private String descriminacaoServico;
    private IntermediarioServicoNfseDTO intermediario;
    private NotaFiscalNfseDTO.ModalidadeEmissao modalidadeEmissao;
    private RegimeTributarioNfseDTO regimeTributario;
    private Boolean incentivoFiscal;
    private ConstrucaoCivilNfseDTO construcaoCivil;

    public RpsNfseDTO() {
        this.optanteSimplesNacional = Boolean.FALSE;
        this.itens = Lists.newArrayList();
        this.issRetido = Boolean.FALSE;
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
        this.incentivoFiscal = Boolean.FALSE;
    }

    public RpsNfseDTO(Long id, String numero, String serie, Date dataEmissao, PrestadorServicoNfseDTO prestador) {
        this();
        this.id = id;
        this.numero = numero;
        this.serie = serie;
        this.dataEmissao = dataEmissao;
        this.prestador = prestador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoRpsNfseDTO getTipoRps() {
        return tipoRps;
    }

    public void setTipoRps(TipoRpsNfseDTO tipoRps) {
        this.tipoRps = tipoRps;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDescriminacaoServico() {
        return descriminacaoServico;
    }

    public void setDescriminacaoServico(String descriminacaoServico) {
        this.descriminacaoServico = descriminacaoServico;
    }

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public ExigibilidadeNfseDTO getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(ExigibilidadeNfseDTO naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    public Boolean getOptanteSimplesNacional() {
        return optanteSimplesNacional != null ? optanteSimplesNacional : Boolean.FALSE;
    }

    public void setOptanteSimplesNacional(Boolean optanteSimplesNacional) {
        if (optanteSimplesNacional == null) {
            optanteSimplesNacional = Boolean.FALSE;
        }
        this.optanteSimplesNacional = optanteSimplesNacional;
    }

    public Date getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Date competencia) {
        this.competencia = competencia;
    }

    public DadosPessoaisNfseDTO getDadosPessoaisPrestador() {
        return dadosPessoaisPrestador;
    }

    public void setDadosPessoaisPrestador(DadosPessoaisNfseDTO dadosPessoaisPrestador) {
        this.dadosPessoaisPrestador = dadosPessoaisPrestador;
    }

    public DadosPessoaisNfseDTO getDadosPessoaisTomador() {
        return dadosPessoaisTomador;
    }

    public void setDadosPessoaisTomador(DadosPessoaisNfseDTO dadosPessoaisTomador) {
        this.dadosPessoaisTomador = dadosPessoaisTomador;
    }

    public List<ItemDeclaracaoServicoNfseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemDeclaracaoServicoNfseDTO> itens) {
        if (itens == null) {
            itens = Lists.newArrayList();
        }
        this.itens = itens;
    }

    public TributosFederaisNfseDTO getTributosFederais() {
        return tributosFederais;
    }

    public void setTributosFederais(TributosFederaisNfseDTO tributosFederais) {
        this.tributosFederais = tributosFederais;
    }

    public Boolean getIssRetido() {
        return issRetido != null ? issRetido : false;
    }

    public void setIssRetido(Boolean issRetido) {
        if (issRetido == null) {
            issRetido = Boolean.FALSE;
        }
        this.issRetido = issRetido;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos != null ? totalServicos : BigDecimal.ZERO;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        if (totalServicos == null) {
            totalServicos = BigDecimal.ZERO;
        }
        this.totalServicos = totalServicos;
    }

    public BigDecimal getTotalNota() {
        return totalNota != null ? totalNota : BigDecimal.ZERO;
    }

    public void setTotalNota(BigDecimal totalNota) {
        if (totalNota == null) {
            totalNota = BigDecimal.ZERO;
        }
        this.totalNota = totalNota;
    }

    public BigDecimal getDeducoesLegais() {
        return deducoesLegais != null ? deducoesLegais : BigDecimal.ZERO;
    }

    public void setDeducoesLegais(BigDecimal deducoesLegais) {
        if (deducoesLegais == null) {
            deducoesLegais = BigDecimal.ZERO;
        }
        this.deducoesLegais = deducoesLegais;
    }

    public BigDecimal getDescontosIncondicionais() {
        return descontosIncondicionais != null ? descontosIncondicionais : BigDecimal.ZERO;
    }

    public void setDescontosIncondicionais(BigDecimal descontosIncondicionais) {
        if (descontosIncondicionais == null) {
            descontosIncondicionais = BigDecimal.ZERO;
        }
        this.descontosIncondicionais = descontosIncondicionais;
    }

    public BigDecimal getDescontosCondicionais() {
        return descontosCondicionais;
    }

    public void setDescontosCondicionais(BigDecimal descontosCondicionais) {
        if (descontosCondicionais == null) {
            descontosCondicionais = BigDecimal.ZERO;
        }
        this.descontosCondicionais = descontosCondicionais;
    }

    public RegimeTributarioNfseDTO getRegimeTributario() {
        return regimeTributario;
    }

    public void setRegimeTributario(RegimeTributarioNfseDTO regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public BigDecimal getDescontos() {
        BigDecimal descontos = BigDecimal.ZERO;
        if (descontosIncondicionais != null) {
            descontos = descontos.add(descontosIncondicionais);
        }
        if (descontosCondicionais != null) {
            descontos = descontos.add(descontosCondicionais);
        }
        return descontos;
    }

    public BigDecimal getRetencoesFederais() {
        return retencoesFederais != null ? retencoesFederais : BigDecimal.ZERO;
    }

    public void setRetencoesFederais(BigDecimal retencoesFederais) {
        if (retencoesFederais == null) {
            retencoesFederais = BigDecimal.ZERO;
        }
        this.retencoesFederais = retencoesFederais;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        if (baseCalculo == null) {
            baseCalculo = BigDecimal.ZERO;
        }
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getIssCalculado() {
        return issCalculado != null ? issCalculado : BigDecimal.ZERO;
    }

    public void setIssCalculado(BigDecimal issCalculado) {
        if (issCalculado == null) {
            issCalculado = BigDecimal.ZERO;
        }
        this.issCalculado = issCalculado;
    }

    public BigDecimal getIssPagar() {
        return issPagar != null ? issPagar : BigDecimal.ZERO;
    }

    public void setIssPagar(BigDecimal issPagar) {
        if (issPagar == null) {
            issPagar = BigDecimal.ZERO;
        }
        this.issPagar = issPagar;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido != null ? valorLiquido : BigDecimal.ZERO;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        if (valorLiquido == null) {
            valorLiquido = BigDecimal.ZERO;
        }
        this.valorLiquido = valorLiquido;
    }

    public String getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(String numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    public SituacaoDeclaracaoNfseDTO getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoDeclaracaoNfseDTO situacao) {
        this.situacao = situacao;
    }

    public Long getIdNotaFiscal() {
        return idNotaFiscal;
    }

    public void setIdNotaFiscal(Long idNotaFiscal) {
        this.idNotaFiscal = idNotaFiscal;
    }

    public Long getIdLote() {
        return idLote;
    }

    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public ResponsavelRetencaoNfseDTO getResponsavelRetencao() {
        return responsavelRetencao;
    }

    public void setResponsavelRetencao(ResponsavelRetencaoNfseDTO responsavelRetencao) {
        this.responsavelRetencao = responsavelRetencao;
    }

    public IntermediarioServicoNfseDTO getIntermediario() {
        return intermediario;
    }

    public void setIntermediario(IntermediarioServicoNfseDTO intermediario) {
        this.intermediario = intermediario;
    }

    public NotaFiscalNfseDTO.ModalidadeEmissao getModalidadeEmissao() {
        return modalidadeEmissao;
    }

    public void setModalidadeEmissao(NotaFiscalNfseDTO.ModalidadeEmissao modalidadeEmissao) {
        this.modalidadeEmissao = modalidadeEmissao;
    }

    public Boolean getIncentivoFiscal() {
        return incentivoFiscal != null ? incentivoFiscal : Boolean.FALSE;
    }

    public void setIncentivoFiscal(Boolean incentivoFiscal) {
        if (incentivoFiscal == null) {
            incentivoFiscal = Boolean.FALSE;
        }
        this.incentivoFiscal = incentivoFiscal;
    }

    public ConstrucaoCivilNfseDTO getConstrucaoCivil() {
        return construcaoCivil;
    }

    public void setConstrucaoCivil(ConstrucaoCivilNfseDTO construcaoCivil) {
        this.construcaoCivil = construcaoCivil;
    }
}
