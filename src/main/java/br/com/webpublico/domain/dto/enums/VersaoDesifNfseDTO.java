package br.com.webpublico.domain.dto.enums;

public enum VersaoDesifNfseDTO {
    VERSAO_1_0("Versão 1.0"),
    VERSAO_ABRASF_3_2("Versão Abrasf 3.2");

    private String descricao;

    VersaoDesifNfseDTO(String descricao) {
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
