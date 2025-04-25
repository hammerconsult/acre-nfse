package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.AbstractEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ArquivoDesifRegistro0410NfseDTO extends AbstractEntity implements RowMapper<ArquivoDesifRegistro0410NfseDTO>, BatchPreparedStatementSetter {

    private Long id;
    private Long linha;
    private Long idArquivo;
    private String codigoDependencia;
    private Date competencia;
    private PlanoGeralContasComentadoNfseDTO conta;
    private BigDecimal saldoInicial;
    private BigDecimal valorDebito;
    private BigDecimal valorCredito;
    private BigDecimal saldoFinal;

    public ArquivoDesifRegistro0410NfseDTO() {
        saldoInicial = BigDecimal.ZERO;
        valorDebito = BigDecimal.ZERO;
        valorCredito = BigDecimal.ZERO;
        saldoFinal = BigDecimal.ZERO;
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

    public String getCodigoDependencia() {
        return codigoDependencia;
    }

    public void setCodigoDependencia(String codigoDependencia) {
        this.codigoDependencia = codigoDependencia;
    }

    public Date getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Date competencia) {
        this.competencia = competencia;
    }

    public PlanoGeralContasComentadoNfseDTO getConta() {
        return conta;
    }

    public void setConta(PlanoGeralContasComentadoNfseDTO conta) {
        this.conta = conta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getValorDebito() {
        return valorDebito;
    }

    public void setValorDebito(BigDecimal valorDebito) {
        this.valorDebito = valorDebito;
    }

    public BigDecimal getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(BigDecimal valorCredito) {
        this.valorCredito = valorCredito;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, linha);
        preparedStatement.setLong(3, idArquivo);
        preparedStatement.setString(4, codigoDependencia);
        preparedStatement.setDate(5, DateUtils.toSQLDate(competencia));
        preparedStatement.setLong(6, conta.getId());
        preparedStatement.setBigDecimal(7, saldoInicial);
        preparedStatement.setBigDecimal(8, valorDebito);
        preparedStatement.setBigDecimal(9, valorCredito);
        preparedStatement.setBigDecimal(10, saldoFinal);


    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifRegistro0410NfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifRegistro0410NfseDTO dto = new ArquivoDesifRegistro0410NfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setLinha(resultSet.getLong("linha"));
        dto.setIdArquivo(resultSet.getLong("arquivodesif_id"));
        dto.setCodigoDependencia(resultSet.getString("codigodependencia"));
        dto.setCompetencia(resultSet.getDate("competencia"));
        dto.setConta(new PlanoGeralContasComentadoNfseDTO());
        dto.getConta().setId(resultSet.getLong("conta_id"));
        dto.setSaldoInicial(resultSet.getBigDecimal("saldoinicial"));
        dto.setValorDebito(resultSet.getBigDecimal("valordebito"));
        dto.setValorCredito(resultSet.getBigDecimal("valorcredito"));
        dto.setSaldoFinal(resultSet.getBigDecimal("saldofinal"));
        return dto;
    }
}
