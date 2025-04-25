package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum TipoAnexoTipoManualDTO implements NfseEnumDTO {
    ARQUIVO("Arquivo"), VIDEO("Vídeo");

    private String descricao;

    TipoAnexoTipoManualDTO(String descricao) {

        this.descricao = descricao;
    }
}
