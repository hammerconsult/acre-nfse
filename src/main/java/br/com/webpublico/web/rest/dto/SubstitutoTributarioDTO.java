package br.com.webpublico.web.rest.dto;

/**
 * Created by Camila on 08/09/2020.
 */
public class SubstitutoTributarioDTO {
    private String cpf_cnpj;
    private String inscricao;
    private String nome_razaosocial;
    private boolean substitutoTributario;

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String getInscricao() {
        return inscricao;
    }

    public void setInscricao(String inscricao) {
        this.inscricao = inscricao;
    }

    public String getNome_razaosocial() {
        return nome_razaosocial;
    }

    public void setNome_razaosocial(String nome_razaosocial) {
        this.nome_razaosocial = nome_razaosocial;
    }


    public boolean isSubstitutoTributario() {
        return substitutoTributario;
    }

    public void setSubstitutoTributario(boolean substitutoTributario) {
        this.substitutoTributario = substitutoTributario;
    }
}
