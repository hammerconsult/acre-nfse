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

public class ArquivoDesifRegistro0430NfseDTO extends AbstractEntity implements RowMapper<ArquivoDesifRegistro0430NfseDTO>, BatchPreparedStatementSetter {

    private Long linha;
    private Long idArquivo;
    private String codigoDependencia;
    private PlanoGeralContasComentadoNfseDTO conta;
    private CodigoTributacaoNfseDTO codigoTributacao;
    private BigDecimal valorCredito;
    private BigDecimal valorDebito;
    private BigDecimal valorReceitaTributavel;
    private BigDecimal valorDeducaoLegal;
    private String discriminacaoDeducao;
    private BigDecimal baseCalculo;
    private BigDecimal aliquota;
    private BigDecimal valorIncentivoFiscal;
    private String discriminacaoIncentivo;
    private BigDecimal valorIssqnRetido;
    private ExigibilidadeNfseDTO exigibilidadeSuspensao;
    private String processoSuspensao;

    public ArquivoDesifRegistro0430NfseDTO() {
        super();
        valorCredito = BigDecimal.ZERO;
        valorDebito = BigDecimal.ZERO;
        valorReceitaTributavel = BigDecimal.ZERO;
        valorDeducaoLegal = BigDecimal.ZERO;
        baseCalculo = BigDecimal.ZERO;
        aliquota = BigDecimal.ZERO;
        valorIncentivoFiscal = BigDecimal.ZERO;
        valorIssqnRetido = BigDecimal.ZERO;
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

    public String getCodigoDependencia() {
        return codigoDependencia;
    }

    public void setCodigoDependencia(String codigoDependencia) {
        this.codigoDependencia = codigoDependencia;
    }

    public PlanoGeralContasComentadoNfseDTO getConta() {
        return conta;
    }

    public void setConta(PlanoGeralContasComentadoNfseDTO conta) {
        this.conta = conta;
    }

    public CodigoTributacaoNfseDTO getCodigoTributacao() {
        return codigoTributacao;
    }

    public void setCodigoTributacao(CodigoTributacaoNfseDTO codigoTributacao) {
        this.codigoTributacao = codigoTributacao;
    }

    public BigDecimal getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(BigDecimal valorCredito) {
        this.valorCredito = valorCredito;
    }

    public BigDecimal getValorDebito() {
        return valorDebito;
    }

    public void setValorDebito(BigDecimal valorDebito) {
        this.valorDebito = valorDebito;
    }

    public BigDecimal getValorReceitaTributavel() {
        return valorReceitaTributavel;
    }

    public void setValorReceitaTributavel(BigDecimal valorReceitaTributavel) {
        this.valorReceitaTributavel = valorReceitaTributavel;
    }

    public BigDecimal getValorDeducaoLegal() {
        return valorDeducaoLegal;
    }

    public void setValorDeducaoLegal(BigDecimal valorDeducaoLegal) {
        this.valorDeducaoLegal = valorDeducaoLegal;
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

    public BigDecimal getValorIncentivoFiscal() {
        return valorIncentivoFiscal;
    }

    public void setValorIncentivoFiscal(BigDecimal valorIncentivoFiscal) {
        this.valorIncentivoFiscal = valorIncentivoFiscal;
    }

    public String getDiscriminacaoIncentivo() {
        return discriminacaoIncentivo;
    }

    public void setDiscriminacaoIncentivo(String discriminacaoIncentivo) {
        this.discriminacaoIncentivo = discriminacaoIncentivo;
    }

    public BigDecimal getValorIssqnRetido() {
        return valorIssqnRetido;
    }

    public void setValorIssqnRetido(BigDecimal valorIssqnRetido) {
        this.valorIssqnRetido = valorIssqnRetido;
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

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, linha);
        preparedStatement.setLong(3, idArquivo);
        preparedStatement.setString(4, codigoDependencia);
        preparedStatement.setLong(5, conta.getId());
        preparedStatement.setLong(6, codigoTributacao.getId());
        preparedStatement.setBigDecimal(7, valorCredito);
        preparedStatement.setBigDecimal(8, valorDebito);
        preparedStatement.setBigDecimal(9, valorReceitaTributavel);
        preparedStatement.setBigDecimal(10, valorDeducaoLegal);
        preparedStatement.setString(11, discriminacaoDeducao);
        preparedStatement.setBigDecimal(12, baseCalculo);
        preparedStatement.setBigDecimal(13, aliquota);
        preparedStatement.setBigDecimal(14, valorIncentivoFiscal);
        preparedStatement.setString(15, discriminacaoIncentivo);
        preparedStatement.setBigDecimal(16, valorIssqnRetido);
        if (exigibilidadeSuspensao != null) {
            preparedStatement.setString(17, exigibilidadeSuspensao.name());
        } else {
            preparedStatement.setNull(17, Types.NULL);
        }
        if (!Strings.isNullOrEmpty(processoSuspensao)) {
            preparedStatement.setString(18, processoSuspensao);
        } else {
            preparedStatement.setNull(18, Types.NULL);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifRegistro0430NfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifRegistro0430NfseDTO dto = new ArquivoDesifRegistro0430NfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setLinha(resultSet.getLong("linha"));
        dto.setIdArquivo(resultSet.getLong("arquivodesif_id"));
        dto.setCodigoDependencia(resultSet.getString("codigodependencia"));
        dto.setConta(new PlanoGeralContasComentadoNfseDTO());
        dto.getConta().setId(resultSet.getLong("conta_id"));
        dto.setCodigoTributacao(new CodigoTributacaoNfseDTO());
        dto.getCodigoTributacao().setId(resultSet.getLong("codigotributacao_id"));
        dto.setValorCredito(resultSet.getBigDecimal("valorcredito"));
        dto.setValorDebito(resultSet.getBigDecimal("valordebito"));
        dto.setValorReceitaTributavel(resultSet.getBigDecimal("valorreceitatributavel"));
        dto.setValorDeducaoLegal(resultSet.getBigDecimal("valordeducaolegal"));
        dto.setDiscriminacaoDeducao(resultSet.getString("discriminacaodeducao"));
        dto.setBaseCalculo(resultSet.getBigDecimal("basecalculo"));
        dto.setAliquota(resultSet.getBigDecimal("aliquota"));
        dto.setValorIncentivoFiscal(resultSet.getBigDecimal("valorincentivofiscal"));
        dto.setDiscriminacaoIncentivo(resultSet.getString("discriminacaoincentivo"));
        dto.setValorIssqnRetido(resultSet.getBigDecimal("valorissqnretido"));
        if (!Strings.isNullOrEmpty(resultSet.getString("exigibilidadesuspensao"))) {
            dto.setExigibilidadeSuspensao(ExigibilidadeNfseDTO.valueOf(resultSet.getString("exigibilidadesuspensao")));
        }
        dto.setProcessoSuspensao(resultSet.getString("processosuspensao"));
        return dto;
    }
}
