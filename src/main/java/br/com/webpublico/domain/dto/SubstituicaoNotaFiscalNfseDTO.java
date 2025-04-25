package br.com.webpublico.domain.dto;


import br.com.webpublico.domain.dto.enums.MotivoCancelamentoNfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubstituicaoNotaFiscalNfseDTO {

    private NotaFiscalNfseDTO substituida;
    private NotaFiscalNfseDTO substituidora;
    private MotivoCancelamentoNfseDTO motivoCancelamento;

    public SubstituicaoNotaFiscalNfseDTO() {
    }

    public NotaFiscalNfseDTO getSubstituida() {
        return substituida;
    }

    public void setSubstituida(NotaFiscalNfseDTO substituida) {
        this.substituida = substituida;
    }

    public NotaFiscalNfseDTO getSubstituidora() {
        return substituidora;
    }

    public void setSubstituidora(NotaFiscalNfseDTO substituidora) {
        this.substituidora = substituidora;
    }

    public MotivoCancelamentoNfseDTO getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(MotivoCancelamentoNfseDTO motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }
}
