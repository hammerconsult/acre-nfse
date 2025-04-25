package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum SituacaoSolicitacaoRPSNfseDTO implements NfseEnumDTO {
    AGUARDANDO("Aguardando"),
    DEFERIDA("Deferida"),
    INDEFERIDA("Indeferida");

    private String descricao;

    SituacaoSolicitacaoRPSNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
