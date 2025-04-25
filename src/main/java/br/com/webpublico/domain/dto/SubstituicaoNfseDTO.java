package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.CancelamentoNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.domain.dto.RpsNfseDTO;

import java.io.Serializable;

public class SubstituicaoNfseDTO implements Serializable {

    private CancelamentoNfseDTO cancelar;
    private RpsNfseDTO rpsSubstituido;
    private NotaFiscalNfseDTO notaFiscal;

    public SubstituicaoNfseDTO() {
    }

    public SubstituicaoNfseDTO(CancelamentoNfseDTO cancelar, RpsNfseDTO rpsSubstituido, NotaFiscalNfseDTO notaFiscal) {
        this.cancelar = cancelar;
        this.rpsSubstituido = rpsSubstituido;
        this.notaFiscal = notaFiscal;
    }

    public CancelamentoNfseDTO getCancelar() {
        return cancelar;
    }

    public void setCancelar(CancelamentoNfseDTO cancelar) {
        this.cancelar = cancelar;
    }

    public RpsNfseDTO getRpsSubstituido() {
        return rpsSubstituido;
    }

    public void setRpsSubstituido(RpsNfseDTO rpsSubstituido) {
        this.rpsSubstituido = rpsSubstituido;
    }

    public NotaFiscalNfseDTO getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(NotaFiscalNfseDTO notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
}
