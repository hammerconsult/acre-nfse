package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum RegimeEspecialTributacaoNfseDTO implements NfseEnumDTO {
    PADRAO("Padrão"),
    INSTITUICAO_FINANCEIRA("Instiruição Finanaceira");

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    RegimeEspecialTributacaoNfseDTO(String descricao) {
        this.descricao = descricao;
    }
}
