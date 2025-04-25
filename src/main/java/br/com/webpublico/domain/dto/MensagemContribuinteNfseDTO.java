package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoMensagemContribuinteNfseDTO;

import java.io.Serializable;
import java.util.Date;

public class MensagemContribuinteNfseDTO implements Serializable {

    private Long id;
    private TipoMensagemContribuinteNfseDTO tipo;
    private Date emitidaEm;
    private String titulo;
    private String conteudo;
    private DetentorArquivoComposicaoNfseDTO detentorArquivoComposicao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMensagemContribuinteNfseDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoMensagemContribuinteNfseDTO tipo) {
        this.tipo = tipo;
    }

    public Date getEmitidaEm() {
        return emitidaEm;
    }

    public void setEmitidaEm(Date emitidaEm) {
        this.emitidaEm = emitidaEm;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public DetentorArquivoComposicaoNfseDTO getDetentorArquivoComposicao() {
        return detentorArquivoComposicao;
    }

    public void setDetentorArquivoComposicao(DetentorArquivoComposicaoNfseDTO detentorArquivoComposicao) {
        this.detentorArquivoComposicao = detentorArquivoComposicao;
    }
}
