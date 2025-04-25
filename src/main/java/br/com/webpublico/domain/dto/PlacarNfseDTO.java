package br.com.webpublico.domain.dto;

import java.io.Serializable;

public class PlacarNfseDTO implements Serializable {

    private Long id;
    private Long prestadoresAutorizados;
    private Long usuariosAtivos;
    private Long notasFiscaisEmitidas;

    public PlacarNfseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuariosAtivos() {
        return usuariosAtivos;
    }

    public void setUsuariosAtivos(Long usuariosAtivos) {
        this.usuariosAtivos = usuariosAtivos;
    }

    public Long getPrestadoresAutorizados() {
        return prestadoresAutorizados;
    }

    public void setPrestadoresAutorizados(Long prestadoresAutorizados) {
        this.prestadoresAutorizados = prestadoresAutorizados;
    }

    public Long getNotasFiscaisEmitidas() {
        return notasFiscaisEmitidas;
    }

    public void setNotasFiscaisEmitidas(Long notasFiscaisEmitidas) {
        this.notasFiscaisEmitidas = notasFiscaisEmitidas;
    }
}
