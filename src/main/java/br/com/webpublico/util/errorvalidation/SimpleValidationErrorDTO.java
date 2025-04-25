package br.com.webpublico.util.errorvalidation;


public class SimpleValidationErrorDTO {

    String title;
    String message;

    public String getMessage() {
        return message;
    }

    public SimpleValidationErrorDTO(String title, String message) {
        this.message = message;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
