package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum TipoNotaFiscalServicoNfseDTO implements NfseEnumDTO{
    ELETRONICA("Eletrônica"),
    CONVENCIONAL("Convencional"),
    NAO_EMITE("Não Emite");

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    private TipoNotaFiscalServicoNfseDTO(String descricao) {
        this.descricao = descricao;
    }
}
