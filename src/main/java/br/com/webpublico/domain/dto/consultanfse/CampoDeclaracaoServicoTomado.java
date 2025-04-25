package br.com.webpublico.domain.dto.consultanfse;

public enum CampoDeclaracaoServicoTomado {
    NUMERO(" dados.numero "),
    EMISSAO(" dados.emissao "),
    SITUACAO(" dados.situacao "),
    TOMADOR_INSCRICAO_MUNICIPAL(" dados.tomador_inscricaomunicipal "),
    TOMADOR_CPF_CNPJ(" dados.tomador_cpfcnpj "),
    PRESTADOR_DADOS_PESSOAIS_CPF_CNPJ(" dados.prestador_cpfcnpj "),
    PRESTADOR_DADOS_PESSOAIS_INSCRICAO_MUNICIPAL(" dados.prestador_inscricaomunicipal "),
    INTERMEDIARIO_CPF_CNPJ(" dados.intermediario_cpfcnpj ");

    private String campo;

    CampoDeclaracaoServicoTomado(String campo) {
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
