package br.com.webpublico.domain.dto.importacaodesif;

public enum IdentificacaoDependenciaNfseDTO {
    INSCRICAO_MUNICIPAL(1, "1 - Inscrição Municipal"),
    CODIGO(2, "2 - Código");

    private Integer codigo;
    private String descricao;

    IdentificacaoDependenciaNfseDTO(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static IdentificacaoDependenciaNfseDTO findByCodigo(Integer codigo) {
        for (IdentificacaoDependenciaNfseDTO identificacao : IdentificacaoDependenciaNfseDTO.values()) {
            if (identificacao.getCodigo().compareTo(codigo) == 0) {
                return identificacao;
            }
        }
        return null;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
