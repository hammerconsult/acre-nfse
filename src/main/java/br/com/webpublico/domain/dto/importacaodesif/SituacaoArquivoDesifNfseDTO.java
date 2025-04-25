package br.com.webpublico.domain.dto.importacaodesif;

public enum SituacaoArquivoDesifNfseDTO {
    AGUARDANDO("Aguardando"),
    EM_PROCESSAMENTO("Em processamento"),
    PROCESSADO("Processado"),
    INCONSISTENTE("Inconsistênte");

    private String descricao;

    SituacaoArquivoDesifNfseDTO(String descricao) {
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
