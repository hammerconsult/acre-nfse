package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoCacheSistemaNfseDTO;

import java.io.Serializable;
import java.util.Objects;

public class CacheSistemaNfseDTO implements Serializable {

    private String uuid;
    private TipoCacheSistemaNfseDTO tipo;
    private String chave;

    public CacheSistemaNfseDTO(TipoCacheSistemaNfseDTO tipo, String chave) {
        this.tipo = tipo;
        this.chave = chave;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public TipoCacheSistemaNfseDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoCacheSistemaNfseDTO tipo) {
        this.tipo = tipo;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheSistemaNfseDTO that = (CacheSistemaNfseDTO) o;
        return tipo == that.tipo && chave.equals(that.chave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, chave);
    }
}
