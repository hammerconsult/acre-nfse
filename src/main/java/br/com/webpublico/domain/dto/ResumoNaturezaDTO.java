package br.com.webpublico.domain.dto;

import java.math.BigDecimal;

public class ResumoNaturezaDTO {

    private String natureza;
    private Integer qtdNotas = 0;
    private BigDecimal totalServicos = BigDecimal.ZERO;
    private BigDecimal totalIss = BigDecimal.ZERO;
    private BigDecimal totalIssRetido = BigDecimal.ZERO;
    private BigDecimal totalIssDevido = BigDecimal.ZERO;

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public Integer getQtdNotas() {
        return qtdNotas;
    }

    public void setQtdNotas(Integer qtdNotas) {
        this.qtdNotas = qtdNotas;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getTotalIss() {
        return totalIss;
    }

    public void setTotalIss(BigDecimal totalIss) {
        this.totalIss = totalIss;
    }

    public BigDecimal getTotalIssRetido() {
        return totalIssRetido;
    }

    public void setTotalIssRetido(BigDecimal totalIssRetido) {
        this.totalIssRetido = totalIssRetido;
    }

    public BigDecimal getTotalIssDevido() {
        return totalIssDevido;
    }

    public void setTotalIssDevido(BigDecimal totalIssDevido) {
        this.totalIssDevido = totalIssDevido;
    }
}
