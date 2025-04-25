package br.com.webpublico.domain.dto;

import java.io.Serializable;

public class LongNfseDTO implements Serializable {

    private Long value;

    public LongNfseDTO() {
    }

    public LongNfseDTO(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
