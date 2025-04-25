package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoDeclaracaoMensalServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.domain.enumeration.Mes;
import com.beust.jcommander.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RelatorioDeclaracaoMensalServicosNfseDTO implements RowMapper<RelatorioDeclaracaoMensalServicosNfseDTO> {

    private Long idPrestador;
    private Long idDeclaracao;
    private String inscricaoCadastral;
    private String cpfCnpj;
    private String nomeRazaoSocial;
    private String nomeFantasia;
    private String classificacaoAtividade;
    private Mes mesDeclaracao;
    private Integer anoDeclaracao;
    private String tipoDeclaracao;
    private String tipoMovimento;
    private String situacaoDeclaracao;
    private Date dataAberturaDeclaracao;
    private Date dataEncerramentoDeclaracao;
    private BigDecimal valorTotalDeclarado;
    private BigDecimal valorTotalIss;
    private Long idCalculo;
    private Date dataVencimentoIss;
    private String situacaoParcela;
    private BigDecimal valorTotalMulta;
    private BigDecimal valorTotalJuros;
    private BigDecimal valorTotalCorrecao;
    private Integer numeroNota;
    private Date emissaoNota;
    private String situacaoNota;
    private String cpfCnpjTomadorNota;
    private String nomeRazaoSocialTomadorNota;
    private boolean simplesNacionalNota;
    private BigDecimal valorNota;
    private BigDecimal valorIssNota;
    private boolean issRetidoNota;
    private String exigibilidadeNota;
    private String conta;
    private String contaDescricao;
    private BigDecimal saldoAnterior;
    private BigDecimal debito;
    private BigDecimal credito;
    private BigDecimal valorMovimento;

    public RelatorioDeclaracaoMensalServicosNfseDTO() {
        this.valorTotalDeclarado = BigDecimal.ZERO;
        this.valorTotalIss = BigDecimal.ZERO;
        this.valorTotalMulta = BigDecimal.ZERO;
        this.valorTotalJuros = BigDecimal.ZERO;
        this.valorTotalCorrecao = BigDecimal.ZERO;
        this.valorNota = BigDecimal.ZERO;
        this.valorIssNota = BigDecimal.ZERO;
        this.saldoAnterior = BigDecimal.ZERO;
        this.debito = BigDecimal.ZERO;
        this.credito = BigDecimal.ZERO;
        this.valorMovimento = BigDecimal.ZERO;
    }

    public Long getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(Long idPrestador) {
        this.idPrestador = idPrestador;
    }

    public Long getIdDeclaracao() {
        return idDeclaracao;
    }

    public void setIdDeclaracao(Long idDeclaracao) {
        this.idDeclaracao = idDeclaracao;
    }

    public Long getIdCalculo() {
        return idCalculo;
    }

    public void setIdCalculo(Long idCalculo) {
        this.idCalculo = idCalculo;
    }

    public String getInscricaoCadastral() {
        return inscricaoCadastral;
    }

    public void setInscricaoCadastral(String inscricaoCadastral) {
        this.inscricaoCadastral = inscricaoCadastral;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getNomeRazaoSocial() {
        return nomeRazaoSocial;
    }

    public void setNomeRazaoSocial(String nomeRazaoSocial) {
        this.nomeRazaoSocial = nomeRazaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getClassificacaoAtividade() {
        return classificacaoAtividade;
    }

    public void setClassificacaoAtividade(String classificacaoAtividade) {
        this.classificacaoAtividade = classificacaoAtividade;
    }

    public Mes getMesDeclaracao() {
        return mesDeclaracao;
    }

    public void setMesDeclaracao(Mes mesDeclaracao) {
        this.mesDeclaracao = mesDeclaracao;
    }

    public Integer getAnoDeclaracao() {
        return anoDeclaracao;
    }

    public void setAnoDeclaracao(Integer anoDeclaracao) {
        this.anoDeclaracao = anoDeclaracao;
    }

    public String getTipoDeclaracao() {
        return tipoDeclaracao;
    }

    public void setTipoDeclaracao(String tipoDeclaracao) {
        this.tipoDeclaracao = tipoDeclaracao;
    }

    public String getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public String getSituacaoDeclaracao() {
        return situacaoDeclaracao;
    }

    public void setSituacaoDeclaracao(String situacaoDeclaracao) {
        this.situacaoDeclaracao = situacaoDeclaracao;
    }

    public String getSituacaoParcela() {
        return situacaoParcela;
    }

    public void setSituacaoParcela(String situacaoParcela) {
        this.situacaoParcela = situacaoParcela;
    }

    public Date getDataAberturaDeclaracao() {
        return dataAberturaDeclaracao;
    }

    public void setDataAberturaDeclaracao(Date dataAberturaDeclaracao) {
        this.dataAberturaDeclaracao = dataAberturaDeclaracao;
    }

    public Date getDataEncerramentoDeclaracao() {
        return dataEncerramentoDeclaracao;
    }

    public void setDataEncerramentoDeclaracao(Date dataEncerramentoDeclaracao) {
        this.dataEncerramentoDeclaracao = dataEncerramentoDeclaracao;
    }

    public Date getDataVencimentoIss() {
        return dataVencimentoIss;
    }

    public void setDataVencimentoIss(Date dataVencimentoIss) {
        this.dataVencimentoIss = dataVencimentoIss;
    }

    public BigDecimal getValorTotalDeclarado() {
        return valorTotalDeclarado;
    }

    public void setValorTotalDeclarado(BigDecimal valorTotalDeclarado) {
        this.valorTotalDeclarado = valorTotalDeclarado;
    }

    public BigDecimal getValorTotalIss() {
        return valorTotalIss;
    }

    public void setValorTotalIss(BigDecimal valorTotalIss) {
        this.valorTotalIss = valorTotalIss;
    }

    public BigDecimal getValorTotalMulta() {
        return valorTotalMulta;
    }

    public void setValorTotalMulta(BigDecimal valorTotalMulta) {
        this.valorTotalMulta = valorTotalMulta;
    }

    public BigDecimal getValorTotalJuros() {
        return valorTotalJuros;
    }

    public void setValorTotalJuros(BigDecimal valorTotalJuros) {
        this.valorTotalJuros = valorTotalJuros;
    }

    public BigDecimal getValorTotalCorrecao() {
        return valorTotalCorrecao;
    }

    public void setValorTotalCorrecao(BigDecimal valorTotalCorrecao) {
        this.valorTotalCorrecao = valorTotalCorrecao;
    }

    public Integer getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Integer numeroNota) {
        this.numeroNota = numeroNota;
    }

    public Date getEmissaoNota() {
        return emissaoNota;
    }

    public void setEmissaoNota(Date emissaoNota) {
        this.emissaoNota = emissaoNota;
    }

    public String getSituacaoNota() {
        return situacaoNota;
    }

    public void setSituacaoNota(String situacaoNota) {
        this.situacaoNota = situacaoNota;
    }

    public String getCpfCnpjTomadorNota() {
        return cpfCnpjTomadorNota;
    }

    public void setCpfCnpjTomadorNota(String cpfCnpjTomadorNota) {
        this.cpfCnpjTomadorNota = cpfCnpjTomadorNota;
    }

    public String getNomeRazaoSocialTomadorNota() {
        return nomeRazaoSocialTomadorNota;
    }

    public void setNomeRazaoSocialTomadorNota(String nomeRazaoSocialTomadorNota) {
        this.nomeRazaoSocialTomadorNota = nomeRazaoSocialTomadorNota;
    }

    public boolean isSimplesNacionalNota() {
        return simplesNacionalNota;
    }

    public void setSimplesNacionalNota(boolean simplesNacionalNota) {
        this.simplesNacionalNota = simplesNacionalNota;
    }

    public BigDecimal getValorNota() {
        return valorNota;
    }

    public void setValorNota(BigDecimal valorNota) {
        this.valorNota = valorNota;
    }

    public BigDecimal getValorIssNota() {
        return valorIssNota;
    }

    public void setValorIssNota(BigDecimal valorIssNota) {
        this.valorIssNota = valorIssNota;
    }

    public boolean isIssRetidoNota() {
        return issRetidoNota;
    }

    public void setIssRetidoNota(boolean issRetidoNota) {
        this.issRetidoNota = issRetidoNota;
    }

    public String getExigibilidadeNota() {
        return exigibilidadeNota;
    }

    public void setExigibilidadeNota(String exigibilidadeNota) {
        this.exigibilidadeNota = exigibilidadeNota;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getContaDescricao() {
        return contaDescricao;
    }

    public void setContaDescricao(String contaDescricao) {
        this.contaDescricao = contaDescricao;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public BigDecimal getValorMovimento() {
        return valorMovimento;
    }

    public void setValorMovimento(BigDecimal valorMovimento) {
        this.valorMovimento = valorMovimento;
    }

    @Override
    public RelatorioDeclaracaoMensalServicosNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        RelatorioDeclaracaoMensalServicosNfseDTO dto = new RelatorioDeclaracaoMensalServicosNfseDTO();
        dto.setIdPrestador(resultSet.getLong("cmc_id"));
        dto.setInscricaoCadastral(resultSet.getString("cmc_inscricao"));
        dto.setCpfCnpj(resultSet.getString("cmc_identificacao"));
        dto.setNomeRazaoSocial(resultSet.getString("cmc_nome"));
        dto.setNomeFantasia(resultSet.getString("cmc_fantasia"));
        if (!Strings.isStringEmpty(resultSet.getString("cmc_classificacao")))
            dto.setClassificacaoAtividade(ClassificacaoAtividadeNfseDTO.valueOf(resultSet.getString("cmc_classificacao")).getDescricao());
        dto.setMesDeclaracao(Mes.valueOf(resultSet.getString("dms_mes")));
        dto.setAnoDeclaracao(resultSet.getInt("dms_ano"));
        dto.setTipoDeclaracao(TipoDeclaracaoMensalServicoNfseDTO.valueOf(resultSet.getString("dms_tipo")).getDescricao());
        dto.setSituacaoDeclaracao(resultSet.getString("dms_situacao"));
        dto.setValorTotalDeclarado(resultSet.getBigDecimal("dms_total_servicos"));
        dto.setValorTotalIss(resultSet.getBigDecimal("dms_total_iss"));
        dto.setDataAberturaDeclaracao(resultSet.getDate("dms_abertura"));
        dto.setDataEncerramentoDeclaracao(resultSet.getDate("dms_encerramento"));
        dto.setIdDeclaracao(resultSet.getLong("dms_id"));
        dto.setTipoMovimento(TipoMovimentoMensalNfseDTO.valueOf(resultSet.getString("dms_tipo_movimento")).getDescricao());
        dto.setCpfCnpjTomadorNota(resultSet.getString("destinado_cpf"));
        dto.setNomeRazaoSocialTomadorNota(resultSet.getString("destinado_nome"));
        dto.setValorNota(resultSet.getBigDecimal("doc_total_servicos"));
        dto.setValorIssNota(resultSet.getBigDecimal("doc_iss_pagar"));
        dto.setEmissaoNota(resultSet.getDate("doc_emissao"));
        dto.setSituacaoNota(SituacaoDeclaracaoNfseDTO.valueOf(resultSet.getString("doc_situacao")).getDescricao());
        dto.setIssRetidoNota(resultSet.getBoolean("doc_retido"));
        dto.setExigibilidadeNota(ExigibilidadeNfseDTO.valueOf(resultSet.getString("doc_natureza_op")).getDescricao());
        dto.setNumeroNota(resultSet.getInt("doc_numero"));
        dto.setSituacaoParcela("");
        dto.setValorTotalMulta(BigDecimal.ZERO);
        dto.setValorTotalJuros(BigDecimal.ZERO);
        dto.setValorTotalCorrecao(BigDecimal.ZERO);
        dto.setIdCalculo(resultSet.getLong("calculo_id"));
        dto.setConta(resultSet.getString("if_conta"));
        dto.setContaDescricao(resultSet.getString("if_descricao"));
        dto.setSaldoAnterior(resultSet.getBigDecimal("if_saldo_anterior"));
        dto.setDebito(resultSet.getBigDecimal("if_credito"));
        dto.setCredito(resultSet.getBigDecimal("if_debito"));
        dto.setValorMovimento(resultSet.getBigDecimal("if_valor_declarado"));
        return dto;
    }
}
