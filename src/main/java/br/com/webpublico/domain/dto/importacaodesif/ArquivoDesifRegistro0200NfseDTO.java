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

public class ArquivoDesifRegistro0200NfseDTO extends AbstractEntity implements RowMapper<ArquivoDesifRegistro0200NfseDTO>, BatchPreparedStatementSetter {

    private Long linha;
    private Long idArquivo;
    private TarifaBancariaNfseDTO tarifaBancaria;
    private Date inicioVigencia;
    private BigDecimal valorUnitario;
    private BigDecimal valorPercentual;
    private String conta;
    private String desdobramento;

    public ArquivoDesifRegistro0200NfseDTO() {
        super();
        valorUnitario = BigDecimal.ZERO;
        valorPercentual = BigDecimal.ZERO;
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

    public TarifaBancariaNfseDTO getTarifaBancaria() {
        return tarifaBancaria;
    }

    public void setTarifaBancaria(TarifaBancariaNfseDTO tarifaBancaria) {
        this.tarifaBancaria = tarifaBancaria;
    }

    public Date getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorPercentual() {
        return valorPercentual;
    }

    public void setValorPercentual(BigDecimal valorPercentual) {
        this.valorPercentual = valorPercentual;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getDesdobramento() {
        return desdobramento;
    }

    public void setDesdobramento(String desdobramento) {
        this.desdobramento = desdobramento;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, linha);
        preparedStatement.setLong(3, idArquivo);
        preparedStatement.setLong(4, tarifaBancaria.getId());
        preparedStatement.setDate(5, DateUtils.toSQLDate(inicioVigencia));
        preparedStatement.setBigDecimal(6, valorUnitario);
        preparedStatement.setBigDecimal(7, valorPercentual);
        preparedStatement.setString(8, conta);
        preparedStatement.setString(9, desdobramento);
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifRegistro0200NfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifRegistro0200NfseDTO dto = new ArquivoDesifRegistro0200NfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setLinha(resultSet.getLong("linha"));
        dto.setIdArquivo(resultSet.getLong("arquivodesif_id"));
        dto.setTarifaBancaria(new TarifaBancariaNfseDTO());
        dto.getTarifaBancaria().setId(resultSet.getLong("tarifabancaria_id"));
        dto.setInicioVigencia(resultSet.getDate("iniciovigencia"));
        dto.setValorUnitario(resultSet.getBigDecimal("valorunitario"));
        dto.setValorPercentual(resultSet.getBigDecimal("valorpercentual"));
        dto.setConta(resultSet.getString("conta"));
        dto.setDesdobramento(resultSet.getString("desdobramento"));
        return dto;
    }
}
