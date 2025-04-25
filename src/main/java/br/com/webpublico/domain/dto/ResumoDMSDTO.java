package br.com.webpublico.domain.dto;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

public class ResumoDMSDTO {

    private Integer qtdNotas = 0;
    private BigDecimal totalServicos = BigDecimal.ZERO;
    private BigDecimal totalIss = BigDecimal.ZERO;
    private BigDecimal totalIssRetido = BigDecimal.ZERO;
    private BigDecimal totalIssDevido = BigDecimal.ZERO;
    private List<ResumoNaturezaDTO> naturezas = Lists.newArrayList();

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

    public List<ResumoNaturezaDTO> getNaturezas() {
        return naturezas;
    }

    public void setNaturezas(List<ResumoNaturezaDTO> naturezas) {
        this.naturezas = naturezas;
    }

    public void addNaturezas(List<ResumoNaturezaDTO> naturezas) {
        naturezas.forEach(n -> {
            this.naturezas.add(n);
            qtdNotas = qtdNotas + n.getQtdNotas();
            totalServicos = totalServicos.add(n.getTotalServicos());
            totalIss = totalIss.add(n.getTotalIss());
            totalIssRetido = totalIssRetido.add(n.getTotalIssRetido());
            totalIssDevido = totalIssDevido.add(n.getTotalIssDevido());
        });
    }
}
