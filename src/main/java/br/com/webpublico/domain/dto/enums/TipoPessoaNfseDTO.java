package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum TipoPessoaNfseDTO implements NfseEnumDTO {
    FISICA("Física"), JURIDICA("Jurídica");

    private String descricao;

    TipoPessoaNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
