package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum SituacaoAidfeNfseDTO implements NfseEnumDTO {
    AGUARDANDO("Aguardando"),
    DEFERIDA("Deferida"),
    PARCIALMENTE("Deferida Parcialmente"),
    INDEFERIDA("Indeferida");

    private String descricao;

    SituacaoAidfeNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
