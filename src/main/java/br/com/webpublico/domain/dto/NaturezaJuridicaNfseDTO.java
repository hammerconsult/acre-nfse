package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.NfseDTO;

public class NaturezaJuridicaNfseDTO implements NfseDTO {
    private Long id;
    private String descricao;

    public NaturezaJuridicaNfseDTO() {
    }

    public NaturezaJuridicaNfseDTO(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
