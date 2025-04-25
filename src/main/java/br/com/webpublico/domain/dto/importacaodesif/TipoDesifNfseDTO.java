package br.com.webpublico.domain.dto.importacaodesif;

public enum TipoDesifNfseDTO {
    TIPO_1(1, "1 - Normal"),
    TIPO_2(2, "2 - Retificadora");

    private Integer codigo;
    private String descricao;

    TipoDesifNfseDTO(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoDesifNfseDTO findByCodigo(Integer codigo) {
        for (TipoDesifNfseDTO tipo : TipoDesifNfseDTO.values()) {
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
