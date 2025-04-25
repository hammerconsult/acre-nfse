package br.com.webpublico.domain.dto.exception;


public class RpsJaExistenteException extends Exception{
    final String mensagem;

    public RpsJaExistenteException(String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
    }
}
