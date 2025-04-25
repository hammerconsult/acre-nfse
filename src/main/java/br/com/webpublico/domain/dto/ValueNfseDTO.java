package br.com.webpublico.domain.dto;

public class ValueNfseDTO {

    private Object value;

    public ValueNfseDTO(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
