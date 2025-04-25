package br.com.webpublico.domain.dto.enums;

public enum TipoMovimentoMensalNfseDTO implements NfseEnumDTO {
    NORMAL("Normal"), RETENCAO("Retenção"), INSTITUICAO_FINANCEIRA("Instituição Financeira"), ISS_RETIDO("ISS Retido");

    private String descricao;

    TipoMovimentoMensalNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
