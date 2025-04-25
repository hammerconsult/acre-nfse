package br.com.webpublico.domain.dto.enums;

public enum SituacaoDeclaracaoNfseDTO {
    ABERTA("Aberta"), EMITIDA("Emitida"), PAGA("Paga"), CANCELADA("Cancelada"), NAO_EMITIDA("Não Emitida");

    private String descricao;

    SituacaoDeclaracaoNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
            return descricao;
        }
}
