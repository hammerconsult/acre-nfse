package br.com.webpublico.util.errorvalidation;

public class FieldErrorDTO {

    private String campo;
    private String entidade;
    private String codigo;
    private String mensagem;

    public FieldErrorDTO(String entidade, String campo, String mensagem, String codigo) {
        this.entidade = entidade;
        this.campo = campo;
        this.mensagem = mensagem;
        this.codigo = codigo;
    }

    public String getCampo() {
        return campo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEntidade() {
        return entidade;
    }
}
