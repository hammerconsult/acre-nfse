package br.com.webpublico.domain.dto.importacaodesif;

public enum TipoConsolidacaoDesifNfseDTO {
    TIPO_1(1, "1 – Instituição e alíquota"),
    TIPO_2(2, "2 – Instituição, alíquota e código de tributação DES-IF"),
    TIPO_3(3, "3 – Dependência e alíquota"),
    TIPO_4(4, "4 – Dependência, alíquota e código de tributação DES-IF");

    private Integer codigo;
    private String descricao;

    TipoConsolidacaoDesifNfseDTO(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoConsolidacaoDesifNfseDTO findByCodigo(Integer codigo) {
        for (TipoConsolidacaoDesifNfseDTO tipo : TipoConsolidacaoDesifNfseDTO.values()) {
            if (tipo.getCodigo().equals(codigo)) {
                return tipo;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
