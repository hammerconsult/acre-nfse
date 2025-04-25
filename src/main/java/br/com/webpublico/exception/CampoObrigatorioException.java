package br.com.webpublico.exception;


/**
 * @author Felipe Marinzeck
 */
public class CampoObrigatorioException extends OperacaoNaoPermitidaException {

    public CampoObrigatorioException() {
        super();
    }

    public CampoObrigatorioException(String message) {
        super(message);
    }

    public CampoObrigatorioException(String message, Exception cause) {
        super(message, cause);
    }

    public void lancarExcecao() throws CampoObrigatorioException {
        if (temMensagens()) {
            throw this;
        }
    }
}
