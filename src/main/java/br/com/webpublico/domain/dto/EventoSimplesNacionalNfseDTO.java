package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.NfseDTO;

import java.util.Date;

public class EventoSimplesNacionalNfseDTO implements NfseDTO {

    private String usuarioImportacao;
    private Date dataImportacao;

    public EventoSimplesNacionalNfseDTO() {
    }

    public String getUsuarioImportacao() {
        return usuarioImportacao;
    }

    public void setUsuarioImportacao(String usuarioImportacao) {
        this.usuarioImportacao = usuarioImportacao;
    }

    public Date getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(Date dataImportacao) {
        this.dataImportacao = dataImportacao;
    }
}
