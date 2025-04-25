package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum ResponsavelRetencaoNfseDTO implements NfseEnumDTO {
    TOMADADOR("Tomador"),
    INTERMEDIARIO("Intermediário");

    String descricao;

    ResponsavelRetencaoNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
