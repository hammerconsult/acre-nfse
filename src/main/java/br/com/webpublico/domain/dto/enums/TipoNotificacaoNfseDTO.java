package br.com.webpublico.domain.dto.enums;

public enum TipoNotificacaoNfseDTO {
    CANCELAMENTO_NFS_ELETRONICA("Cancelamento de Nota Fiscal Eletrônica"),
    FALE_CONOSCO_PORTAL_NFSE("Fale Conosco (Nfs-e)"),
    RESPOSTA_QUESTIONAMENTO_NFSE("Resposta de questionamento da nota fiscal eletrônica de serviços");

    private String descricao;

    TipoNotificacaoNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
