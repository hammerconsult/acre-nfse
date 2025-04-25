package br.com.webpublico.domain.dto.importacaodesif;

public enum TipoPartidaNfseDTO {
    CREDITO(1, "1 - Crédito"),
    DEBITO(2, "2 - Débito");

    private Integer codigo;
    private String descricao;

    TipoPartidaNfseDTO(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoPartidaNfseDTO findByCodigo(Integer codigo) {
        for (TipoPartidaNfseDTO tipoPartida : TipoPartidaNfseDTO.values()) {
            if (tipoPartida.getCodigo().compareTo(codigo) == 0) {
                return tipoPartida;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
