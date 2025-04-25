package br.com.webpublico.domain.dto.enums;

import br.com.webpublico.domain.dto.enums.NfseEnumDTO;

public enum TipoIssqnNfseDTO implements NfseEnumDTO {
    ESTIMADO("Estimado"),
    FIXO("Fixo Anual"),
    IMUNE("Imune"),
    ISENTO("Isento"),
    MEI("MEI"),
    MENSAL("Mensal"),
    NAO_INCIDENCIA("Não Incidência"),
    SIMPLES_NACIONAL("Simples Nacional"),
    SUBLIMITE_ULTRAPASSADO("Sublimite Ultrapassado");

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    TipoIssqnNfseDTO(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
