package br.com.webpublico.util.sucessvalidation;


public class SimpleValidationSuccessDTO {

    String title;
    String message;

    public String getMessage() {
        return message;
    }

    public SimpleValidationSuccessDTO(String title, String message) {
        this.message = message;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
