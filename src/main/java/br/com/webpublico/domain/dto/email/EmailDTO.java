package br.com.webpublico.domain.dto.email;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class EmailDTO implements Serializable {

    private String para;
    private String assunto;
    private String conteudo;
    private Boolean html = Boolean.FALSE;
    private List<AnexoDTO> anexos;
    private Boolean aplicacaoProducao = Boolean.FALSE;

    public EmailDTO() {
        this.anexos = Lists.newArrayList();
    }

    public EmailDTO(String para, String assunto, String conteudo,
                    Boolean html, List<AnexoDTO> anexos, Boolean aplicacaoProducao) {
        this();
        this.para = para;
        this.assunto = assunto;
        this.conteudo = conteudo;
        this.html = html;
        this.anexos = anexos;
        this.aplicacaoProducao = aplicacaoProducao;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Boolean getHtml() {
        return html;
    }

    public void setHtml(Boolean html) {
        this.html = html;
    }

    public List<AnexoDTO> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<AnexoDTO> anexos) {
        this.anexos = anexos;
    }

    public Boolean getAplicacaoProducao() {
        return aplicacaoProducao;
    }

    public void setAplicacaoProducao(Boolean aplicacaoProducao) {
        this.aplicacaoProducao = aplicacaoProducao;
    }
}
