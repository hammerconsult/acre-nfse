package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum RegimeTributarioNfseDTO implements NfseEnumDTO {


    SIMPLES_NACIONAL("Simples Nacional"),
    LUCRO_PRESUMIDO("Lucro Presumido");

    private String descricao;

    private RegimeTributarioNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;

    }
}
