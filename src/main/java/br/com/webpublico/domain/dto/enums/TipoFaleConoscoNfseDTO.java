package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum TipoFaleConoscoNfseDTO implements NfseEnumDTO {
    DENUNCIA("Denúncia"),
    RECLAMACAO("Reclamação"),
    SOLICITACAO("Solicitação"),
    SUGESTAO("Sugestão"),
    ELOGIO("Elogio");

    private String descricao;

    TipoFaleConoscoNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
