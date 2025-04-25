package br.com.webpublico.domain.dto;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DebitoNfseDTO implements NfseDTO {

    private Long idProcesso;
    private String descricao;
    private Date dataLancamento;
    private Long idExercicio;
    private Integer mesReferencia;
    private Long idCalculo;
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
    private List<DebitoItemNfseDTO> itens;
    private Long idValorDivida;
    private Long idItemValorDivida;
    private Long idParcelaValorDivida;
    private Date dataVencimento;
    private Long idOpcaoPagamento;
    private Long idItemParcelaValorDivida;
    private Long idSituacaoParcelaValorDivida;
    private String referencia;

    public DebitoNfseDTO() {
        itens = Lists.newArrayList();
    }

    public Long getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(Long idProcesso) {
        this.idProcesso = idProcesso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Long getIdExercicio() {
        return idExercicio;
    }

    public void setIdExercicio(Long idExercicio) {
        this.idExercicio = idExercicio;
    }

    public Integer getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(Integer mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public Long getIdCalculo() {
        return idCalculo;
    }

    public void setIdCalculo(Long idCalculo) {
        this.idCalculo = idCalculo;
    }

    public Long getIdDivida() {
        return idDivida;
    }

    public void setIdDivida(Long idDivida) {
        this.idDivida = idDivida;
    }

    public Long getIdTributo() {
        return idTributo;
    }

    public void setIdTributo(Long idTributo) {
        this.idTributo = idTributo;
    }

    public Long getIdCadastro() {
        return idCadastro;
    }

    public void setIdCadastro(Long idCadastro) {
        this.idCadastro = idCadastro;
    }

    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Boolean getAusenciaMovimento() {
        return ausenciaMovimento;
    }

    public void setAusenciaMovimento(Boolean ausenciaMovimento) {
        this.ausenciaMovimento = ausenciaMovimento;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public Long getSequenciaLancamento() {
        return sequenciaLancamento;
    }

    public void setSequenciaLancamento(Long sequenciaLancamento) {
        this.sequenciaLancamento = sequenciaLancamento;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getFaturamento() {
        return faturamento;
    }

    public void setFaturamento(BigDecimal faturamento) {
        this.faturamento = faturamento;
    }

    public BigDecimal getValorCalculado() {
        return valorCalculado;
    }

    public void setValorCalculado(BigDecimal valorCalculado) {
        this.valorCalculado = valorCalculado;
    }

    public Long getSubDivida() {
        return subDivida;
    }

    public void setSubDivida(Long subDivida) {
        this.subDivida = subDivida;
    }

    public List<DebitoItemNfseDTO> getItens() {
        return itens;
    }

    public void setItens(List<DebitoItemNfseDTO> itens) {
        this.itens = itens;
    }

    public Long getIdValorDivida() {
        return idValorDivida;
    }

    public void setIdValorDivida(Long idValorDivida) {
        this.idValorDivida = idValorDivida;
    }

    public Long getIdItemValorDivida() {
        return idItemValorDivida;
    }

    public void setIdItemValorDivida(Long idItemValorDivida) {
        this.idItemValorDivida = idItemValorDivida;
    }

    public Long getIdParcelaValorDivida() {
        return idParcelaValorDivida;
    }

    public void setIdParcelaValorDivida(Long idParcelaValorDivida) {
        this.idParcelaValorDivida = idParcelaValorDivida;
    }

    public Long getIdOpcaoPagamento() {
        return idOpcaoPagamento;
    }

    public void setIdOpcaoPagamento(Long idOpcaoPagamento) {
        this.idOpcaoPagamento = idOpcaoPagamento;
    }

    public Long getIdItemParcelaValorDivida() {
        return idItemParcelaValorDivida;
    }

    public void setIdItemParcelaValorDivida(Long idItemParcelaValorDivida) {
        this.idItemParcelaValorDivida = idItemParcelaValorDivida;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Long getIdSituacaoParcelaValorDivida() {
        return idSituacaoParcelaValorDivida;
    }

    public void setIdSituacaoParcelaValorDivida(Long idSituacaoParcelaValorDivida) {
        this.idSituacaoParcelaValorDivida = idSituacaoParcelaValorDivida;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
