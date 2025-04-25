package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.DeclaracaoContaBancariaNfseDTO;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

public class LivroFiscalCompetenciaNfseDTO implements NfseDTO {

    private Long prestadorId;
    private Integer exercicio;
    private Integer mes;
    private TipoMovimentoMensalNfseDTO tipoMovimento;

    //Documentos escriturados
    private Integer quantidade;
    private BigDecimal issqnProprio;
    private BigDecimal issqnRetido;
    private BigDecimal issqnPago;
    private BigDecimal saldoIssqn;
    private BigDecimal valorServico;
    private List<NotaFiscalSearchDTO> documentos;
    private List<TotalPorNaturezaSituacaoNfseDTO> totalPorNaturezaSituacao;

    //Documentos não escrituradas
    private Integer quantidadeNaoDeclarado;
    private BigDecimal issqnProprioNaoDeclarado;
    private BigDecimal issqnRetidoNaoDeclarado;
    private BigDecimal issqnPagoNaoDeclarado;
    private BigDecimal saldoIssqnNaoDeclarado;
    private BigDecimal valorServicoNaoDeclarado;
    private List<NotaFiscalSearchDTO> documentosNaoDeclarado;
    private List<TotalPorNaturezaSituacaoNfseDTO> totalPorNaturezaSituacaoNaoDeclarado;

    private List<DeclaracaoContaBancariaNfseDTO> contasDesif;

    private Boolean detalhado;

    public LivroFiscalCompetenciaNfseDTO() {
        quantidade = 0;
        issqnProprio = BigDecimal.ZERO;
        issqnRetido = BigDecimal.ZERO;
        issqnPago = BigDecimal.ZERO;
        saldoIssqn = BigDecimal.ZERO;
        valorServico = BigDecimal.ZERO;
        documentos = Lists.newArrayList();
        totalPorNaturezaSituacao = Lists.newArrayList();

        quantidadeNaoDeclarado = 0;
        issqnProprioNaoDeclarado = BigDecimal.ZERO;
        issqnRetidoNaoDeclarado = BigDecimal.ZERO;
        issqnPagoNaoDeclarado = BigDecimal.ZERO;
        saldoIssqnNaoDeclarado = BigDecimal.ZERO;
        valorServicoNaoDeclarado = BigDecimal.ZERO;
        documentosNaoDeclarado = Lists.newArrayList();
        totalPorNaturezaSituacaoNaoDeclarado = Lists.newArrayList();

        contasDesif = Lists.newArrayList();

        detalhado = Boolean.TRUE;
    }

    public Long getPrestadorId() {
        return prestadorId;
    }

    public void setPrestadorId(Long prestadorId) {
        this.prestadorId = prestadorId;
    }

    public Integer getExercicio() {
        return exercicio;
    }

    public void setExercicio(Integer exercicio) {
        this.exercicio = exercicio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public TipoMovimentoMensalNfseDTO getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(TipoMovimentoMensalNfseDTO tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getIssqnProprio() {
        return issqnProprio;
    }

    public void setIssqnProprio(BigDecimal issqnProprio) {
        this.issqnProprio = issqnProprio;
    }

    public BigDecimal getIssqnRetido() {
        return issqnRetido;
    }

    public void setIssqnRetido(BigDecimal issqnRetido) {
        this.issqnRetido = issqnRetido;
    }

    public BigDecimal getIssqnPago() {
        return issqnPago;
    }

    public void setIssqnPago(BigDecimal issqnPago) {
        this.issqnPago = issqnPago;
    }

    public BigDecimal getSaldoIssqn() {
        return saldoIssqn;
    }

    public void setSaldoIssqn(BigDecimal saldoIssqn) {
        this.saldoIssqn = saldoIssqn;
    }

    public BigDecimal getValorServico() {
        return valorServico;
    }

    public void setValorServico(BigDecimal valorServico) {
        this.valorServico = valorServico;
    }

    public List<NotaFiscalSearchDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<NotaFiscalSearchDTO> documentos) {
        this.documentos = documentos;
    }

    public List<TotalPorNaturezaSituacaoNfseDTO> getTotalPorNaturezaSituacao() {
        return totalPorNaturezaSituacao;
    }

    public void setTotalPorNaturezaSituacao(List<TotalPorNaturezaSituacaoNfseDTO> totalPorNaturezaSituacao) {
        this.totalPorNaturezaSituacao = totalPorNaturezaSituacao;
    }

    public Integer getQuantidadeNaoDeclarado() {
        return quantidadeNaoDeclarado;
    }

    public void setQuantidadeNaoDeclarado(Integer quantidadeNaoDeclarado) {
        this.quantidadeNaoDeclarado = quantidadeNaoDeclarado;
    }

    public BigDecimal getIssqnProprioNaoDeclarado() {
        return issqnProprioNaoDeclarado;
    }

    public void setIssqnProprioNaoDeclarado(BigDecimal issqnProprioNaoDeclarado) {
        this.issqnProprioNaoDeclarado = issqnProprioNaoDeclarado;
    }

    public BigDecimal getIssqnRetidoNaoDeclarado() {
        return issqnRetidoNaoDeclarado;
    }

    public void setIssqnRetidoNaoDeclarado(BigDecimal issqnRetidoNaoDeclarado) {
        this.issqnRetidoNaoDeclarado = issqnRetidoNaoDeclarado;
    }

    public BigDecimal getIssqnPagoNaoDeclarado() {
        return issqnPagoNaoDeclarado;
    }

    public void setIssqnPagoNaoDeclarado(BigDecimal issqnPagoNaoDeclarado) {
        this.issqnPagoNaoDeclarado = issqnPagoNaoDeclarado;
    }

    public BigDecimal getSaldoIssqnNaoDeclarado() {
        return saldoIssqnNaoDeclarado;
    }

    public void setSaldoIssqnNaoDeclarado(BigDecimal saldoIssqnNaoDeclarado) {
        this.saldoIssqnNaoDeclarado = saldoIssqnNaoDeclarado;
    }

    public BigDecimal getValorServicoNaoDeclarado() {
        return valorServicoNaoDeclarado;
    }

    public void setValorServicoNaoDeclarado(BigDecimal valorServicoNaoDeclarado) {
        this.valorServicoNaoDeclarado = valorServicoNaoDeclarado;
    }

    public List<NotaFiscalSearchDTO> getDocumentosNaoDeclarado() {
        return documentosNaoDeclarado;
    }

    public void setDocumentosNaoDeclarado(List<NotaFiscalSearchDTO> documentosNaoDeclarado) {
        this.documentosNaoDeclarado = documentosNaoDeclarado;
    }

    public List<TotalPorNaturezaSituacaoNfseDTO> getTotalPorNaturezaSituacaoNaoDeclarado() {
        return totalPorNaturezaSituacaoNaoDeclarado;
    }

    public void setTotalPorNaturezaSituacaoNaoDeclarado(List<TotalPorNaturezaSituacaoNfseDTO> totalPorNaturezaSituacaoNaoDeclarado) {
        this.totalPorNaturezaSituacaoNaoDeclarado = totalPorNaturezaSituacaoNaoDeclarado;
    }

    public List<DeclaracaoContaBancariaNfseDTO> getContasDesif() {
        return contasDesif;
    }

    public void setContasDesif(List<DeclaracaoContaBancariaNfseDTO> contasDesif) {
        this.contasDesif = contasDesif;
    }

    public Boolean getDetalhado() {
        return detalhado;
    }

    public void setDetalhado(Boolean detalhado) {
        this.detalhado = detalhado;
    }
}
