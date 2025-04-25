package br.com.webpublico.domain.dto.consultanfse;

public enum Operador {
    MAIOR(">"), MENOR("<"), IGUAL("="), DIFERENTE("<>"), MAIOR_IGUAL(">="), MENOR_IGUAL("<="), LIKE("like"), IN("in"), NOT_IN("not in"), IS("is"), IS_NOT_NULL("is not null");
    private String operador;

    private Operador(String operador) {
        this.operador = operador;
    }

    public String getOperador() {
        return operador;
    }
}

