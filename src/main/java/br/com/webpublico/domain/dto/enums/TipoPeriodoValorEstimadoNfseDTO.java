package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum TipoPeriodoValorEstimadoNfseDTO implements NfseEnumDTO {

    MENSAL("Mensal"),
    ANUAL("Anual");
    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    private TipoPeriodoValorEstimadoNfseDTO(String descricao) {
        this.descricao = descricao;
    }
}
