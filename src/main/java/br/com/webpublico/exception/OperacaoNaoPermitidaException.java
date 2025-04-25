package br.com.webpublico.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Felipe Marinzeck
 */

public class OperacaoNaoPermitidaException extends RuntimeException {

    private final List<String> mensagens;

    public OperacaoNaoPermitidaException() {
        super();
        mensagens = new ArrayList<>();
    }

    public OperacaoNaoPermitidaException(String message) {
        super(message);
        mensagens = new ArrayList<>();
    }

    public OperacaoNaoPermitidaException(String message, Throwable e) {
        super(message, e);
        mensagens = new ArrayList<>();
    }

    public OperacaoNaoPermitidaException(String message, List<String> mensagens) {
        super(message);
        this.mensagens = mensagens;
    }

    public boolean temMensagens() {
        return !mensagens.isEmpty();
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    public void adicionarMensagem(String mensagem) {
        mensagens.add(mensagem);
    }

    public void lancarExcecao() throws OperacaoNaoPermitidaException {
        if (temMensagens()) {
            throw this;
        }
    }
}
