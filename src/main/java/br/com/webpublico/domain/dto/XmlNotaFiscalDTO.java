package br.com.webpublico.domain.dto;

import java.util.Objects;

public class XmlNotaFiscalDTO {

    private Long id;
    private String inscricaoCadastral;
    private String conteudo;
    private Integer percentual;
    private Boolean erro;

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public XmlNotaFiscalDTO(String conteudo) {
        this.conteudo = conteudo;
    }

    public XmlNotaFiscalDTO() {
    }

    public Integer getPercentual() {
        return percentual;
    }

    public void setPercentual(Integer percentual) {
        this.percentual = percentual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInscricaoCadastral() {
        return inscricaoCadastral;
    }

    public void setInscricaoCadastral(String inscricaoCadastral) {
        this.inscricaoCadastral = inscricaoCadastral;
    }

    public Boolean getErro() {
        return erro;
    }

    public void setErro(Boolean erro) {
        this.erro = erro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlNotaFiscalDTO that = (XmlNotaFiscalDTO) o;
        return Objects.equals(inscricaoCadastral, that.inscricaoCadastral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inscricaoCadastral);
    }
}
