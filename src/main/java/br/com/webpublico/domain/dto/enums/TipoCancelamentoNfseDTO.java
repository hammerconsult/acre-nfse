package br.com.webpublico.domain.dto.enums;

public enum TipoCancelamentoNfseDTO {
    MANUAL("Manual"), AUTOMATICO("Automático");

    private String descricao;

    TipoCancelamentoNfseDTO(String descricao) {
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
