package br.com.webpublico.domain.dto.enums;

public enum TipoRelatorioNfseDTO {
    RESUMIDO("Resumido"), DETALHADO("Detalhado");

    private String descricao;

    TipoRelatorioNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
