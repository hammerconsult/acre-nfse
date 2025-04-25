package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ArquivoDesifRegistro0440NfseDTO extends AbstractEntity implements RowMapper<ArquivoDesifRegistro0440NfseDTO>, BatchPreparedStatementSetter {

    private Long linha;
    private Long idArquivo;
    private String cnpjDependencia;
    private CodigoTributacaoNfseDTO codigoTributacao;
    private BigDecimal valorReceitaTributavel;
    private BigDecimal valorDeducaoConta;
    private BigDecimal valorDeducaoConsolidado;
    private String discriminacaoDeducao;
    private BigDecimal baseCalculo;
    private BigDecimal aliquota;
    private BigDecimal valorIssqn;
    private BigDecimal valorIssqnRetido;
    private BigDecimal valorIncentivoConta;
    private BigDecimal valorIncentivoConsolidado;
    private String discriminacaoIncentivo;
    private BigDecimal valorCompensacao;
    private String discriminacaoCompensacao;
    private BigDecimal valorIssqnRecolhido;
    private ExigibilidadeNfseDTO exigibilidadeSuspensao;
    private String processoSuspensao;
    private BigDecimal valorIssqnRecolher;

    public ArquivoDesifRegistro0440NfseDTO() {
        super();
        valorReceitaTributavel = BigDecimal.ZERO;
        valorDeducaoConta = BigDecimal.ZERO;
        valorDeducaoConsolidado = BigDecimal.ZERO;
        baseCalculo = BigDecimal.ZERO;
        aliquota = BigDecimal.ZERO;
        valorIssqn = BigDecimal.ZERO;
        valorIssqnRetido = BigDecimal.ZERO;
        valorIncentivoConta = BigDecimal.ZERO;
        valorIncentivoConsolidado = BigDecimal.ZERO;
        valorCompensacao = BigDecimal.ZERO;
        valorIssqnRecolhido = BigDecimal.ZERO;
        valorIssqnRecolher = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLinha() {
        return linha;
    }

    public void setLinha(Long linha) {
        this.linha = linha;
    }

    public Long getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Long idArquivo) {
        this.idArquivo = idArquivo;
    }

    public String getCnpjDependencia() {
        return cnpjDependencia;
    }

    public void setCnpjDependencia(String cnpjDependencia) {
        this.cnpjDependencia = cnpjDependencia;
    }

    public CodigoTributacaoNfseDTO getCodigoTributacao() {
        return codigoTributacao;
    }

    public void setCodigoTributacao(CodigoTributacaoNfseDTO codigoTributacao) {
        this.codigoTributacao = codigoTributacao;
    }

    public BigDecimal getValorReceitaTributavel() {
        return valorReceitaTributavel;
    }

    public void setValorReceitaTributavel(BigDecimal valorReceitaTributavel) {
        this.valorReceitaTributavel = valorReceitaTributavel;
    }

    public BigDecimal getValorDeducaoConta() {
        return valorDeducaoConta;
    }

    public void setValorDeducaoConta(BigDecimal valorDeducaoConta) {
        this.valorDeducaoConta = valorDeducaoConta;
    }

    public BigDecimal getValorDeducaoConsolidado() {
        return valorDeducaoConsolidado;
    }

    public void setValorDeducaoConsolidado(BigDecimal valorDeducaoConsolidado) {
        this.valorDeducaoConsolidado = valorDeducaoConsolidado;
    }

    public String getDiscriminacaoDeducao() {
        return discriminacaoDeducao;
    }

    public void setDiscriminacaoDeducao(String discriminacaoDeducao) {
        this.discriminacaoDeducao = discriminacaoDeducao;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public BigDecimal getValorIssqn() {
        return valorIssqn;
    }

    public void setValorIssqn(BigDecimal valorIssqn) {
        this.valorIssqn = valorIssqn;
    }

    public BigDecimal getValorIssqnRetido() {
        return valorIssqnRetido;
    }

    public void setValorIssqnRetido(BigDecimal valorIssqnRetido) {
        this.valorIssqnRetido = valorIssqnRetido;
    }

    public BigDecimal getValorIncentivoConta() {
        return valorIncentivoConta;
    }

    public void setValorIncentivoConta(BigDecimal valorIncentivoConta) {
        this.valorIncentivoConta = valorIncentivoConta;
    }

    public BigDecimal getValorIncentivoConsolidado() {
        return valorIncentivoConsolidado;
    }

    public void setValorIncentivoConsolidado(BigDecimal valorIncentivoConsolidado) {
        this.valorIncentivoConsolidado = valorIncentivoConsolidado;
    }

    public String getDiscriminacaoIncentivo() {
        return discriminacaoIncentivo;
    }

    public void setDiscriminacaoIncentivo(String discriminacaoIncentivo) {
        this.discriminacaoIncentivo = discriminacaoIncentivo;
    }

    public BigDecimal getValorCompensacao() {
        return valorCompensacao;
    }

    public void setValorCompensacao(BigDecimal valorCompensacao) {
        this.valorCompensacao = valorCompensacao;
    }

    public String getDiscriminacaoCompensacao() {
        return discriminacaoCompensacao;
    }

    public void setDiscriminacaoCompensacao(String discriminacaoCompensacao) {
        this.discriminacaoCompensacao = discriminacaoCompensacao;
    }

    public BigDecimal getValorIssqnRecolhido() {
        return valorIssqnRecolhido;
    }

    public void setValorIssqnRecolhido(BigDecimal valorIssqnRecolhido) {
        this.valorIssqnRecolhido = valorIssqnRecolhido;
    }

    public ExigibilidadeNfseDTO getExigibilidadeSuspensao() {
        return exigibilidadeSuspensao;
    }

    public void setExigibilidadeSuspensao(ExigibilidadeNfseDTO exigibilidadeSuspensao) {
        this.exigibilidadeSuspensao = exigibilidadeSuspensao;
    }

    public String getProcessoSuspensao() {
        return processoSuspensao;
    }

    public void setProcessoSuspensao(String processoSuspensao) {
        this.processoSuspensao = processoSuspensao;
    }

    public BigDecimal getValorIssqnRecolher() {
        return valorIssqnRecolher;
    }

    public void setValorIssqnRecolher(BigDecimal valorIssqnRecolher) {
        this.valorIssqnRecolher = valorIssqnRecolher;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, linha);
        preparedStatement.setLong(3, idArquivo);
        preparedStatement.setString(4, cnpjDependencia);
        if (codigoTributacao != null) {
            preparedStatement.setLong(5, codigoTributacao.getId());
        } else {
            preparedStatement.setNull(5, Types.NULL);
        }
        preparedStatement.setBigDecimal(6, valorReceitaTributavel);
        preparedStatement.setBigDecimal(7, valorDeducaoConta);
        preparedStatement.setBigDecimal(8, valorDeducaoConsolidado);
        preparedStatement.setString(9, discriminacaoDeducao);
        preparedStatement.setBigDecimal(10, baseCalculo);
        preparedStatement.setBigDecimal(11, aliquota);
        preparedStatement.setBigDecimal(12, valorIssqn);
        preparedStatement.setBigDecimal(13, valorIssqnRetido);
        preparedStatement.setBigDecimal(14, valorIncentivoConta);
        preparedStatement.setBigDecimal(15, valorIncentivoConsolidado);
        preparedStatement.setString(16, discriminacaoIncentivo);
        preparedStatement.setBigDecimal(17, valorCompensacao);
        preparedStatement.setString(18, discriminacaoCompensacao);
        preparedStatement.setBigDecimal(19, valorIssqnRecolhido);
        if (exigibilidadeSuspensao != null) {
            preparedStatement.setString(20, exigibilidadeSuspensao.name());
        } else {
            preparedStatement.setNull(20, Types.NULL);
        }
        preparedStatement.setString(21, processoSuspensao);
        preparedStatement.setBigDecimal(22, valorIssqnRecolher);
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifRegistro0440NfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifRegistro0440NfseDTO dto = new ArquivoDesifRegistro0440NfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setLinha(resultSet.getLong("linha"));
        dto.setIdArquivo(resultSet.getLong("arquivodesif_id"));
        dto.setCnpjDependencia(resultSet.getString("cnpjdependencia"));
        if (resultSet.getLong("codigotributacao_id") != 0) {
            dto.setCodigoTributacao(new CodigoTributacaoNfseDTO());
            dto.getCodigoTributacao().setId(resultSet.getLong("codigotributacao_id"));
        }
        dto.setValorReceitaTributavel(resultSet.getBigDecimal("valorreceitatributavel"));
        dto.setValorDeducaoConta(resultSet.getBigDecimal("valordeducaoconta"));
        dto.setValorDeducaoConsolidado(resultSet.getBigDecimal("valordeducaoconsolidado"));
        dto.setDiscriminacaoDeducao(resultSet.getString("discriminacaodeducao"));
        dto.setBaseCalculo(resultSet.getBigDecimal("basecalculo"));
        dto.setAliquota(resultSet.getBigDecimal("aliquota"));
        dto.setValorIssqn(resultSet.getBigDecimal("valorissqn"));
        dto.setValorIssqnRetido(resultSet.getBigDecimal("valorissqnretido"));
        dto.setValorIncentivoConta(resultSet.getBigDecimal("valorincentivoconta"));
        dto.setValorIncentivoConsolidado(resultSet.getBigDecimal("valorincentivoconsolidado"));
        dto.setDiscriminacaoIncentivo(resultSet.getString("discriminacaoincentivo"));
        dto.setValorCompensacao(resultSet.getBigDecimal("valorcompensacao"));
        dto.setDiscriminacaoCompensacao(resultSet.getString("discriminacaocompensacao"));
        dto.setValorIssqnRecolhido(resultSet.getBigDecimal("valorissqnrecolhido"));
        if (!Strings.isNullOrEmpty(resultSet.getString("exigibilidadesuspensao"))) {
            dto.setExigibilidadeSuspensao(ExigibilidadeNfseDTO.valueOf(resultSet.getString("exigibilidadesuspensao")));
        }
        dto.setProcessoSuspensao(resultSet.getString("processosuspensao"));
        dto.setValorIssqnRecolher(resultSet.getBigDecimal("valorissqnrecolher"));
        return dto;
    }
}
