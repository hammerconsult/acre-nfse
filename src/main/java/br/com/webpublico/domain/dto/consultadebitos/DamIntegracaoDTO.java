package br.com.webpublico.domain.dto.consultadebitos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DamIntegracaoDTO {

    private Long idParcela;
    private Long idDam;
    private String codigoCadastro;
    private Integer anoDivida;
    private Integer codigoDivida;
    private Integer codigoSubDivida;
    private Integer competencia;
    private Date dataLancamento;
    private Integer codigoReceita;
    private Date dataVencimento;
    private String situacao;
    private BigDecimal valorOriginal;
    private Integer anoDam;
    private Date emissaoDam;
    private String codigoBarras;
    private String codigoBarrasDigitos;
    private String campo1;
    private String campo2;
    private String campo3;
    private String campo4;
    private String campo5;
    private String nossoNumero;
    private BigDecimal desconto;
    private BigDecimal juros;
    private BigDecimal multa;
    private BigDecimal correcao;
    private Date dataRemissao;

    public Long getIdParcela() {
        return idParcela;
    }

    public void setIdParcela(Long idParcela) {
        this.idParcela = idParcela;
    }

    public Long getIdDam() {
        return idDam;
    }

    public void setIdDam(Long idDam) {
        this.idDam = idDam;
    }

    public String getCodigoBarrasDigitos() {
        return codigoBarrasDigitos;
    }

    public void setCodigoBarrasDigitos(String codigoBarrasDigitos) {
        this.codigoBarrasDigitos = codigoBarrasDigitos;
    }

    public String getCodigoCadastro() {
        return codigoCadastro;
    }

    public void setCodigoCadastro(String codigoCadastro) {
        this.codigoCadastro = codigoCadastro;
    }

    public Integer getAnoDivida() {
        return anoDivida;
    }

    public void setAnoDivida(Integer anoDivida) {
        this.anoDivida = anoDivida;
    }

    public Integer getCodigoDivida() {
        return codigoDivida;
    }

    public void setCodigoDivida(Integer codigoDivida) {
        this.codigoDivida = codigoDivida;
    }

    public Integer getCodigoSubDivida() {
        return codigoSubDivida;
    }

    public void setCodigoSubDivida(Integer codigoSubDivida) {
        this.codigoSubDivida = codigoSubDivida;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Integer getCodigoReceita() {
        return codigoReceita;
    }

    public void setCodigoReceita(Integer codigoReceita) {
        this.codigoReceita = codigoReceita;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public Integer getAnoDam() {
        return anoDam;
    }

    public void setAnoDam(Integer anoDam) {
        this.anoDam = anoDam;
    }

    public Date getEmissaoDam() {
        return emissaoDam;
    }

    public void setEmissaoDam(Date emissaoDam) {
        this.emissaoDam = emissaoDam;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public String getCampo3() {
        return campo3;
    }

    public void setCampo3(String campo3) {
        this.campo3 = campo3;
    }

    public String getCampo4() {
        return campo4;
    }

    public void setCampo4(String campo4) {
        this.campo4 = campo4;
    }

    public String getCampo5() {
        return campo5;
    }

    public void setCampo5(String campo5) {
        this.campo5 = campo5;
    }

    public String getNossoNumero() {
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getJuros() {
        return juros;
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public BigDecimal getCorrecao() {
        return correcao;
    }

    public void setCorrecao(BigDecimal correcao) {
        this.correcao = correcao;
    }

    public Date getDataRemissao() {
        return dataRemissao;
    }

    public void setDataRemissao(Date dataRemissao) {
        this.dataRemissao = dataRemissao;
    }

    public Integer getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Integer competencia) {
        this.competencia = competencia;
    }
}
