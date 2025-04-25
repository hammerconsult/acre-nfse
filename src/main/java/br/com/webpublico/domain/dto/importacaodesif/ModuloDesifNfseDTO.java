package br.com.webpublico.domain.dto.importacaodesif;

public enum ModuloDesifNfseDTO {
    MODULO_1(1, "1 - Demonstrativo Contábil"),
    MODULO_2(2, "2 - Apuração Mensal do ISSQN"),
    MODULO_3(3, "3 - Informações Comuns aos Municípios"),
    MODULO_4(4, "4 - Partidas dos Lançamentos Contábeis");

    private Integer codigo;
    private String descricao;

    ModuloDesifNfseDTO(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static ModuloDesifNfseDTO findByCodigo(Integer codigo) {
        for (ModuloDesifNfseDTO modulo : ModuloDesifNfseDTO.values()) {
            if (modulo.getCodigo().equals(codigo)) {
                return modulo;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
