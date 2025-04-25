package br.com.webpublico.domain.dto.importacaodesif;

public class ValidacaoDesifNfseDTO {

    private TipoValidacaoDesifNfseDTO tipoValidacao;
    private String mensagem;

    public TipoValidacaoDesifNfseDTO getTipoValidacao() {
        return tipoValidacao;
    }

    public void setTipoValidacao(TipoValidacaoDesifNfseDTO tipoValidacao) {
        this.tipoValidacao = tipoValidacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
