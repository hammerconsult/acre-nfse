package br.com.webpublico.domain.dto.consultadebitos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ParcelaDTO implements Serializable {

    private Long idParcela;
    private Long idCalculo;
    private Long idCadastro;
    private Long idPessoa;
    private Long idValorDivida;
    private Long idConfiguracaoAcrescimo;
    private Long idDivida;
    private Long idOpcaoPagamento;
    private String cadastro;
    private String tipoCadastro;
    private String tipoCalculo;
    private String divida;
    private Boolean dividaIsDividaAtiva;
    private String referencia;
    private Integer exercicio;
    private String parcela;
    private Integer sd;
    private Date vencimento;
    private Date pagamento;
    private Date emissao;
    private BigDecimal valorOriginal;
    private BigDecimal valorImposto;
    private BigDecimal valorTaxa;
    private BigDecimal valorDesconto;
    private BigDecimal valorJuros;
    private BigDecimal valorMulta;
    private BigDecimal valorHonorarios;
    private BigDecimal valorCorrecao;
    private BigDecimal valorPago;
    private BigDecimal total;
    private String situacao;
    private Boolean dividaAtiva;
    private Boolean dividaAtivaAjuizada;
    private String permissaoEmissaoDAM;
    private Boolean bloqueiaImpressao;
    private Boolean cotaUnica;
    private Long quantidadeDamImpresso;
    private String enderecoCadastro;
    private Integer qtdeOpcoesPagamento;
    private String observacaoCalculo;
    private Integer ordemApresentacao;
    private String numeroDam;

    public ParcelaDTO() {
        valorOriginal = BigDecimal.ZERO;
        valorCorrecao = BigDecimal.ZERO;
        valorPago = BigDecimal.ZERO;
        valorImposto = BigDecimal.ZERO;
        valorTaxa = BigDecimal.ZERO;
        valorHonorarios = BigDecimal.ZERO;
        valorDesconto = BigDecimal.ZERO;
        valorJuros = BigDecimal.ZERO;
        valorMulta = BigDecimal.ZERO;
        total = BigDecimal.ZERO;
        dividaAtiva = Boolean.FALSE;
        dividaAtivaAjuizada = Boolean.FALSE;
        numeroDam = "";
    }

    public Long getIdParcela() {
        return idParcela;
    }

    public void setIdParcela(Long idParcela) {
        this.idParcela = idParcela;
    }

    public Long getIdCalculo() {
        return idCalculo;
    }

    public void setIdCalculo(Long idCalculo) {
        this.idCalculo = idCalculo;
    }

    public Long getIdCadastro() {
        return idCadastro;
    }

    public void setIdCadastro(Long idCadastro) {
        this.idCadastro = idCadastro;
    }

    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Long getIdValorDivida() {
        return idValorDivida;
    }

    public void setIdValorDivida(Long idValorDivida) {
        this.idValorDivida = idValorDivida;
    }

    public Long getIdConfiguracaoAcrescimo() {
        return idConfiguracaoAcrescimo;
    }

    public void setIdConfiguracaoAcrescimo(Long idConfiguracaoAcrescimo) {
        this.idConfiguracaoAcrescimo = idConfiguracaoAcrescimo;
    }

    public Long getIdDivida() {
        return idDivida;
    }

    public void setIdDivida(Long idDivida) {
        this.idDivida = idDivida;
    }

    public Long getIdOpcaoPagamento() {
        return idOpcaoPagamento;
    }

    public void setIdOpcaoPagamento(Long idOpcaoPagamento) {
        this.idOpcaoPagamento = idOpcaoPagamento;
    }

    public String getCadastro() {
        return cadastro;
    }

    public void setCadastro(String cadastro) {
        this.cadastro = cadastro;
    }

    public String getTipoCadastro() {
        return tipoCadastro;
    }

    public void setTipoCadastro(String tipoCadastro) {
        this.tipoCadastro = tipoCadastro;
    }

    public String getTipoCalculo() {
        return tipoCalculo;
    }

    public void setTipoCalculo(String tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }

    public String getDivida() {
        return divida;
    }

    public void setDivida(String divida) {
        this.divida = divida;
    }

    public Boolean getDividaIsDividaAtiva() {
        return dividaIsDividaAtiva;
    }

    public void setDividaIsDividaAtiva(Boolean dividaIsDividaAtiva) {
        this.dividaIsDividaAtiva = dividaIsDividaAtiva;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getExercicio() {
        return exercicio;
    }

    public void setExercicio(Integer exercicio) {
        this.exercicio = exercicio;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public Integer getSd() {
        return sd;
    }

    public void setSd(Integer sd) {
        this.sd = sd;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public Date getPagamento() {
        return pagamento;
    }

    public void setPagamento(Date pagamento) {
        this.pagamento = pagamento;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public BigDecimal getValorImposto() {
        return valorImposto;
    }

    public void setValorImposto(BigDecimal valorImposto) {
        this.valorImposto = valorImposto;
    }

    public BigDecimal getValorTaxa() {
        return valorTaxa;
    }

    public void setValorTaxa(BigDecimal valorTaxa) {
        this.valorTaxa = valorTaxa;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorJuros() {
        return valorJuros;
    }

    public void setValorJuros(BigDecimal valorJuros) {
        this.valorJuros = valorJuros;
    }

    public BigDecimal getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(BigDecimal valorMulta) {
        this.valorMulta = valorMulta;
    }

    public BigDecimal getValorHonorarios() {
        return valorHonorarios;
    }

    public void setValorHonorarios(BigDecimal valorHonorarios) {
        this.valorHonorarios = valorHonorarios;
    }

    public BigDecimal getValorCorrecao() {
        return valorCorrecao;
    }

    public void setValorCorrecao(BigDecimal valorCorrecao) {
        this.valorCorrecao = valorCorrecao;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Boolean getDividaAtiva() {
        return dividaAtiva;
    }

    public void setDividaAtiva(Boolean dividaAtiva) {
        this.dividaAtiva = dividaAtiva;
    }

    public Boolean getDividaAtivaAjuizada() {
        return dividaAtivaAjuizada;
    }

    public void setDividaAtivaAjuizada(Boolean dividaAtivaAjuizada) {
        this.dividaAtivaAjuizada = dividaAtivaAjuizada;
    }

    public String getPermissaoEmissaoDAM() {
        return permissaoEmissaoDAM;
    }

    public void setPermissaoEmissaoDAM(String permissaoEmissaoDAM) {
        this.permissaoEmissaoDAM = permissaoEmissaoDAM;
    }

    public Boolean getBloqueiaImpressao() {
        return bloqueiaImpressao;
    }

    public void setBloqueiaImpressao(Boolean bloqueiaImpressao) {
        this.bloqueiaImpressao = bloqueiaImpressao;
    }

    public Boolean getCotaUnica() {
        return cotaUnica;
    }

    public void setCotaUnica(Boolean cotaUnica) {
        this.cotaUnica = cotaUnica;
    }

    public Long getQuantidadeDamImpresso() {
        return quantidadeDamImpresso;
    }

    public void setQuantidadeDamImpresso(Long quantidadeDamImpresso) {
        this.quantidadeDamImpresso = quantidadeDamImpresso;
    }

    public String getEnderecoCadastro() {
        return enderecoCadastro;
    }

    public void setEnderecoCadastro(String enderecoCadastro) {
        this.enderecoCadastro = enderecoCadastro;
    }

    public Integer getQtdeOpcoesPagamento() {
        return qtdeOpcoesPagamento;
    }

    public void setQtdeOpcoesPagamento(Integer qtdeOpcoesPagamento) {
        this.qtdeOpcoesPagamento = qtdeOpcoesPagamento;
    }

    public String getObservacaoCalculo() {
        return observacaoCalculo;
    }

    public void setObservacaoCalculo(String observacaoCalculo) {
        this.observacaoCalculo = observacaoCalculo;
    }

    public Integer getOrdemApresentacao() {
        return ordemApresentacao;
    }

    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
    }

    public String getNumeroDam() {
        return numeroDam;
    }

    public void setNumeroDam(String numeroDam) {
        this.numeroDam = numeroDam;
    }
}
