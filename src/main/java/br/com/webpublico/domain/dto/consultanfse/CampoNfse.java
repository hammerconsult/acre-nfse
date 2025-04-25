package br.com.webpublico.domain.dto.consultanfse;

public enum CampoNfse {
    NUMERO(" obj.numero "),
    EMISSAO(" obj.emissao "),
    SITUACAO(" dps.situacao "),
    PRESTADOR_INSCRICAO_MUNICIPAL(" cep.inscricaoCadastral "),
    PRESTADOR_CPF_CNPJ(" dppnf.cpfcnpj "),
    RPS_ID(" rps.id "),
    RPS_NUMERO(" rps.numero "),
    RPS_SERIE(" rps.serie "),
    RPS_TIPO(" rps.tiporps "),
    LOTERPS_ID(" rps.loterps_id "),
    TOMADOR_DADOS_PESSOAIS_CPF_CNPJ(" dptnf.cpfcnpj "),
    TOMADOR_DADOS_PESSOAIS_INSCRICAO_MUNICIPAL(" dptnf.inscricaomunicipal "),
    INTERMEDIARIO_CPF_CNPJ(" i.cpfcnpj ");

    private String campo;

    CampoNfse(String campo) {
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
