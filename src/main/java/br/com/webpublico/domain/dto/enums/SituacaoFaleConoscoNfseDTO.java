package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum SituacaoFaleConoscoNfseDTO implements NfseEnumDTO {
    ABERTO("Aberto"), ENCERRADO("Encerrado");

    private String descricao;

    SituacaoFaleConoscoNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
