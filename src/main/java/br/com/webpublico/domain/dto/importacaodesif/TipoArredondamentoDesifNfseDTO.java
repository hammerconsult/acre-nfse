package br.com.webpublico.domain.dto.importacaodesif;

public enum TipoArredondamentoDesifNfseDTO {
    TIPO_1(1, "1 - Arredondado"),
    TIPO_2(2, "2 - Truncado");

    private Integer codigo;
    private String descricao;

    TipoArredondamentoDesifNfseDTO(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoArredondamentoDesifNfseDTO findByCodigo(Integer codigo) {
        for (TipoArredondamentoDesifNfseDTO tipo : TipoArredondamentoDesifNfseDTO.values()) {
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
