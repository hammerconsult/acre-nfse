package br.com.webpublico.domain.dto.consultanfse;

public enum CampoLoteRPS {
    NUMERO(" l.numero "),
    PROTOCOLO(" l.protocolo "),
    PRESTADOR_INSCRICAO_MUNICIPAL(" cep.inscricaocadastral ");

    private String campo;

    CampoLoteRPS(String campo) {
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
