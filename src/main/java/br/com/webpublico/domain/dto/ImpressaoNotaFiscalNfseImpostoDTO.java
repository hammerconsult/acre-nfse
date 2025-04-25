package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.NfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImpressaoNotaFiscalNfseImpostoDTO implements NfseDTO {

    private String descricao;
    private BigDecimal aliquota;
    private BigDecimal valor;
    private Boolean retido;

    public ImpressaoNotaFiscalNfseImpostoDTO() {
    }

    public ImpressaoNotaFiscalNfseImpostoDTO(String descricao, BigDecimal aliquota, BigDecimal valor, Boolean retido) {
        this.descricao = descricao;
        this.aliquota = aliquota;
        this.valor = valor;
        this.retido = retido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getRetido() {
        return retido;
    }

    public void setRetido(Boolean retido) {
        this.retido = retido;
    }
}
