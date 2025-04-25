package br.com.webpublico.web.rest.dto;

import br.com.webpublico.domain.enumeration.Mes;

import java.math.BigDecimal;


public class NotaEmitidaPorMes {
    private Mes mes;
    private Integer emitidas;
    private Integer normais;
    private Integer retidas;
    private Integer canceladas;
    private BigDecimal totalServicos;
    private BigDecimal totalIss;

    public NotaEmitidaPorMes() {
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Integer getEmitidas() {
        return emitidas;
    }

    public void setEmitidas(Integer emitidas) {
        this.emitidas = emitidas;
    }

    public Integer getNormais() {
        return normais;
    }

    public void setNormais(Integer normais) {
        this.normais = normais;
    }

    public Integer getRetidas() {
        return retidas;
    }

    public void setRetidas(Integer retidas) {
        this.retidas = retidas;
    }

    public Integer getCanceladas() {
        return canceladas;
    }

    public void setCanceladas(Integer canceladas) {
        this.canceladas = canceladas;
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
}
