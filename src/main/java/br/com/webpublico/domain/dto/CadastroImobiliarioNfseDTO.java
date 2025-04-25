package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.NfseDTO;

public class CadastroImobiliarioNfseDTO implements NfseDTO {

    private Long id;
    private String inscricaoCadastral;

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
}
