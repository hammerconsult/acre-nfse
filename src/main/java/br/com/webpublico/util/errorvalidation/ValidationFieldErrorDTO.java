package br.com.webpublico.util.errorvalidation;

import java.util.ArrayList;
import java.util.List;

public class ValidationFieldErrorDTO {

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public ValidationFieldErrorDTO() {

    }

    public void addFieldError(String entidade, String field, String mensagem, String codigo) {
        FieldErrorDTO error = new FieldErrorDTO(entidade, field, mensagem, codigo);
        fieldErrors.add(error);
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}
