package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoPessoaNfseDTO;

/**
 * Created by rodolfo on 19/10/17.
 */
public class PessoaNfseDTO implements NfseDTO {

    private Long id;
    private TipoPessoaNfseDTO tipoPessoa;
    private DadosPessoaisNfseDTO dadosPessoais;


    public PessoaNfseDTO() {
    }

    public PessoaNfseDTO(Long id, DadosPessoaisNfseDTO dadosPessoais) {
        this.id = id;
        this.dadosPessoais = dadosPessoais;
    }

    public TipoPessoaNfseDTO getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaNfseDTO tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public DadosPessoaisNfseDTO getDadosPessoais() {
        return dadosPessoais;
    }

    public void setDadosPessoais(DadosPessoaisNfseDTO dadosPessoais) {
        this.dadosPessoais = dadosPessoais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return dadosPessoais.toString();
    }
}
