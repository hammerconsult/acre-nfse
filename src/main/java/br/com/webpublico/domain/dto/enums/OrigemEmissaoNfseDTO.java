package br.com.webpublico.domain.dto.enums;

public enum OrigemEmissaoNfseDTO {
    WEB("Web"),
    APP("App"),
    RPS("RPS");

    private String descricao;

    OrigemEmissaoNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
