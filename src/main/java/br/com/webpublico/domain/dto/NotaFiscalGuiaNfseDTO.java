package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class NotaFiscalGuiaNfseDTO implements RowMapper<NotaFiscalGuiaNfseDTO> {

    private BigDecimal numeroDocumento;
    private String tipoDocumento;
    private Date emissao;
    private String cpfCnpj;
    private String nomeRazaoSocial;
    private BigDecimal totalServicos;
    private BigDecimal iss;

    public BigDecimal getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(BigDecimal numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
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

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getIss() {
        return iss;
    }

    public void setIss(BigDecimal iss) {
        this.iss = iss;
    }

    @Override
    public NotaFiscalGuiaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        NotaFiscalGuiaNfseDTO dto = new NotaFiscalGuiaNfseDTO();
        dto.setNumeroDocumento(resultSet.getBigDecimal("NUMERO_DOCUMENTO"));
        dto.setTipoDocumento(resultSet.getString("TIPO_DOCUMENTO"));
        dto.setEmissao(resultSet.getDate("EMISSAO"));
        dto.setCpfCnpj(resultSet.getString("CPF_CNPJ"));
        dto.setNomeRazaoSocial(resultSet.getString("NOME_RAZAOSOCIAL"));
        dto.setTotalServicos(resultSet.getBigDecimal("TOTAL_SERVICOS"));
        dto.setIss(resultSet.getBigDecimal("ISS"));
        return dto;
    }
}
