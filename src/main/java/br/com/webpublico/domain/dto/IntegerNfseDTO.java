package br.com.webpublico.domain.dto;

import java.io.Serializable;

public class IntegerNfseDTO implements Serializable {

    private Integer value;

    public IntegerNfseDTO() {
    }

    public IntegerNfseDTO(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
