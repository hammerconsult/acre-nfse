package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum LocalExecucaoTrabalho implements NfseEnumDTO {
    LOCAL("No Local"),
    DOMICIO_PRESTADOR("No domicilio do prestador"),
    ESTABELECIMENTO_PRESTADOR("No estabelecimento do prestador");

    private String descricao;

    LocalExecucaoTrabalho(String descricao) {
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
