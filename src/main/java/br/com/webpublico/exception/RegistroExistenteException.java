package br.com.webpublico.exception;

/**
 * Created by peixe on 24/08/2015.
 */
public class RegistroExistenteException extends RuntimeException {

    public RegistroExistenteException(String message) {
        super(message);
    }
}
