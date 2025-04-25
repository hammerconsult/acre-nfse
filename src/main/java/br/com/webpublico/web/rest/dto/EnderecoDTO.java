package br.com.webpublico.web.rest.dto;

import br.com.webpublico.domain.dto.MunicipioNfseDTO;

import java.io.Serializable;
import java.util.Objects;


public class EnderecoDTO implements Serializable {

    private Long id;

    private String cep;

    private String numero;

    private String logradouro;

    private String bairro;

    private String complemento;

    private MunicipioNfseDTO municipio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        if (cep != null && !cep.isEmpty()) {
            return cep = cep.replace("-", "");
        }
        return null;
    }

    public void setCep(String cep) {
        if (cep != null && !cep.isEmpty()) {
            cep = cep.replace("-", "");
        }
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public MunicipioNfseDTO getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioNfseDTO municipio) {
        this.municipio = municipio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EnderecoDTO endereco = (EnderecoDTO) o;

        if (!Objects.equals(id, endereco.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Endereco{" +
            "id=" + id +
            ", cep='" + cep + "'" +
            ", numero='" + numero + "'" +
            ", logradouro='" + logradouro + "'" +
            ", bairro='" + bairro + "'" +
            ", complemento='" + complemento + "'" +
            '}';
    }

    @Override
    public EnderecoDTO clone() throws CloneNotSupportedException {
        EnderecoDTO cloneable = new EnderecoDTO();
        cloneable.cep = cep;
        cloneable.numero = numero;
        cloneable.logradouro = logradouro;
        cloneable.bairro = bairro;
        cloneable.complemento = complemento;
        cloneable.municipio = municipio;
        return cloneable;
    }

    public String getEndereco() {
        StringBuilder sb = new StringBuilder();
        if (logradouro != null) {
            sb.append(logradouro);
            sb.append(" ");
        }
        if (numero != null) {
            sb.append("Número: ");
            sb.append(numero);
            sb.append(" ");
        }
        if (bairro != null) {
            sb.append("Bairro: ");
            sb.append(bairro);
            sb.append(" ");
        }
        if (cep != null) {
            sb.append("CEP: ");
            sb.append(cep);
            sb.append(" ");
        }
        return sb.toString();
    }
}
