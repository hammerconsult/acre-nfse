package br.com.webpublico.web.rest.dto;


import java.util.Date;

public class PrestadorServicoDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private Date credenciadoEm;

    public PrestadorServicoDTO() {
    }

    public PrestadorServicoDTO(Long id, String nome, String cnpj, Date credenciadoEm) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.credenciadoEm = credenciadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Date getCredenciadoEm() {
        return credenciadoEm;
    }

    public void setCredenciadoEm(Date credenciadoEm) {
        this.credenciadoEm = credenciadoEm;
    }

    @Override
    public String toString() {
        return "PrestadorServicoDTO{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", cnpj='" + cnpj + '\'' +
            ", credenciadoEm='" + credenciadoEm + '\'' +
            '}';
    }
}
