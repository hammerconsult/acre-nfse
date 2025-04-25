package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.NfseDTO;

public enum SituacaoDam implements NfseDTO {
    ABERTO("Em Aberto"), CANCELADO("Cancelado"), SUBISTITUIDO("Subistituído"), PAGO("Pago");

    private String descricao;

    SituacaoDam(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
