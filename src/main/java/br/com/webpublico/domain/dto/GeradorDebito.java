package br.com.webpublico.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeradorDebito implements Serializable {

    private String descricao;
    private Date dataLancamento;
    private Long idExercicio;
    private Integer mesReferencia;
    private Long idDivida;
    private Long idTributo;
    private Long idCadastro;
    private Long idPessoa;
    private Boolean ausenciaMovimento;
    private Long sequenciaLancamento;
    private BigDecimal aliquota;
    private BigDecimal baseCalculo;
    private BigDecimal faturamento;
    private BigDecimal valorCalculado;
    private Long subDivida;
    private Date dataVencimento;
    private Long idOpcaoPagamento;
    private String referencia;
    private List<GeradorDebitoItem> itens;

    public GeradorDebito(String descricao,
                         Date dataLancamento,
                         Long idExercicio,
                         Integer mesReferencia,
                         Long idDivida,
                         Long idTributo,
                         Long idCadastro,
                         Long idPessoa,
                         Boolean ausenciaMovimento,
                         Long sequenciaLancamento,
                         BigDecimal aliquota,
                         BigDecimal baseCalculo,
                         BigDecimal faturamento,
                         BigDecimal valorCalculado,
                         Long subDivida,
                         Date dataVencimento,
                         Long idOpcaoPagamento,
                         String referencia,
                         List<GeradorDebitoItem> itens) {
        this.descricao = descricao;
        this.dataLancamento = dataLancamento;
        this.idExercicio = idExercicio;
        this.mesReferencia = mesReferencia;
        this.idDivida = idDivida;
        this.idTributo = idTributo;
        this.idCadastro = idCadastro;
        this.idPessoa = idPessoa;
        this.ausenciaMovimento = ausenciaMovimento;
        this.sequenciaLancamento = sequenciaLancamento;
        this.aliquota = aliquota;
        this.baseCalculo = baseCalculo;
        this.faturamento = faturamento;
        this.valorCalculado = valorCalculado;
        this.subDivida = subDivida;
        this.dataVencimento = dataVencimento;
        this.idOpcaoPagamento = idOpcaoPagamento;
        this.referencia = referencia;
        this.itens = itens;
    }

    public DebitoNfseDTO toDebito() {
        DebitoNfseDTO debito = new DebitoNfseDTO();
        debito.setDescricao(descricao);
        debito.setDataLancamento(dataLancamento);
        debito.setIdExercicio(idExercicio);
        debito.setMesReferencia(mesReferencia);
        debito.setIdDivida(idDivida);
        debito.setIdTributo(idTributo);
        debito.setIdCadastro(idCadastro);
        debito.setIdPessoa(idPessoa);
        debito.setAusenciaMovimento(ausenciaMovimento);
        debito.setSequenciaLancamento(sequenciaLancamento);
        debito.setAliquota(aliquota);
        debito.setBaseCalculo(baseCalculo);
        debito.setFaturamento(faturamento);
        debito.setValorCalculado(valorCalculado);
        debito.setSubDivida(subDivida);
        debito.setDataVencimento(dataVencimento);
        debito.setIdOpcaoPagamento(idOpcaoPagamento);
        debito.setReferencia(referencia);
        debito.setItens(new ArrayList());
        if (itens != null) {
            for (GeradorDebitoItem geradorDebitoItem : itens) {
                debito.getItens().add(geradorDebitoItem.toDebitoItem());
            }
        }
        return debito;
    }
}
