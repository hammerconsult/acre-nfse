package br.com.webpublico.web.rest.dto;

public class PerfilAppDto {

    private String perfil;

    public PerfilAppDto() {

    }

    public PerfilAppDto(String perfil) {
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
