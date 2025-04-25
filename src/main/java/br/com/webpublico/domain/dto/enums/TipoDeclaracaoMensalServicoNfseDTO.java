package br.com.webpublico.domain.dto.enums;

public enum TipoDeclaracaoMensalServicoNfseDTO implements NfseEnumDTO {
    PRINCIPAL("Principal"), COMPLEMENTAR("Complementar");

    private String descricao;

    TipoDeclaracaoMensalServicoNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
