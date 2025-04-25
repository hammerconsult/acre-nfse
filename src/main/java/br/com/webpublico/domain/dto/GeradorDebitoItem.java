package br.com.webpublico.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class GeradorDebitoItem implements Serializable {

    private Long idServico;
    private BigDecimal aliquota;
    private BigDecimal baseCalculo;
    private BigDecimal valorServico;
    private BigDecimal iss;

    public GeradorDebitoItem(Long idServico,
                             BigDecimal aliquota,
                             BigDecimal baseCalculo,
                             BigDecimal valorServico,
                             BigDecimal iss) {
        this.idServico = idServico;
        this.aliquota = aliquota;
        this.baseCalculo = baseCalculo;
        this.valorServico = valorServico;
        this.iss = iss;
    }

    public DebitoItemNfseDTO toDebitoItem() {
        DebitoItemNfseDTO debitoItem = new DebitoItemNfseDTO();
        debitoItem.setIdServico(idServico);
        debitoItem.setAliquota(aliquota);
        debitoItem.setBaseCalculo(baseCalculo);
        debitoItem.setValorServico(valorServico);
        debitoItem.setIss(iss);
        return debitoItem;
    }
}
