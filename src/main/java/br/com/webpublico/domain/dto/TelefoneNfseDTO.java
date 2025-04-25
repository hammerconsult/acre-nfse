package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.NfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class TelefoneNfseDTO implements NfseDTO {

    private Long id;
    private String tipoTelefone;
    private String telefone;
    private Boolean principal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    @JsonIgnore
    public static TelefoneNfseDTO getTelefonePorTipo(String tipoTelefone, List<TelefoneNfseDTO> telefones) {
        if (telefones != null) {
            for (TelefoneNfseDTO telefone : telefones) {
                if (telefone.getTipoTelefone() != null && telefone.getTipoTelefone().equals(tipoTelefone)) {
                    return telefone;
                }
            }
        }
        return null;
    }
}
