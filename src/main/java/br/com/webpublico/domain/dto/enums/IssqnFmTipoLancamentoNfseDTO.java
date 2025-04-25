package br.com.webpublico.domain.dto.enums;

public enum IssqnFmTipoLancamentoNfseDTO {
    PROPRIO("Próprio"), SUBSTITUTO("Substituição");

    private String descricao;

    IssqnFmTipoLancamentoNfseDTO(String descricao) {
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
