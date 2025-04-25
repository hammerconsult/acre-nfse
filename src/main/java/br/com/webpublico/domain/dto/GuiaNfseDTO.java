package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.SituacaoDam;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class GuiaNfseDTO implements RowMapper<GuiaNfseDTO> {

    private String numeroDam;
    private String referencia;
    private Date vencimentoDebito;
    private Date vencimentoDam;
    private Date emissaoDam;
    private BigDecimal valorDam;
    private String situacaoDam;
    private List<NotaFiscalGuiaNfseDTO> notasFiscais;

    public String getNumeroDam() {
        return numeroDam;
    }

    public void setNumeroDam(String numeroDam) {
        this.numeroDam = numeroDam;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Date getVencimentoDebito() {
        return vencimentoDebito;
    }

    public void setVencimentoDebito(Date vencimentoDebito) {
        this.vencimentoDebito = vencimentoDebito;
    }

    public Date getVencimentoDam() {
        return vencimentoDam;
    }

    public void setVencimentoDam(Date vencimentoDam) {
        this.vencimentoDam = vencimentoDam;
    }

    public Date getEmissaoDam() {
        return emissaoDam;
    }

    public void setEmissaoDam(Date emissaoDam) {
        this.emissaoDam = emissaoDam;
    }

    public BigDecimal getValorDam() {
        return valorDam;
    }

    public void setValorDam(BigDecimal valorDam) {
        this.valorDam = valorDam;
    }

    public String getSituacaoDam() {
        return situacaoDam;
    }

    public void setSituacaoDam(String situacaoDam) {
        this.situacaoDam = situacaoDam;
    }

    public List<NotaFiscalGuiaNfseDTO> getNotasFiscais() {
        return notasFiscais;
    }

    public void setNotasFiscais(List<NotaFiscalGuiaNfseDTO> notasFiscais) {
        this.notasFiscais = notasFiscais;
    }

    @Override
    public GuiaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        GuiaNfseDTO dto = new GuiaNfseDTO();
        dto.setNumeroDam(resultSet.getString("NUMERO_DAM"));
        dto.setReferencia(resultSet.getString("REFERENCIA_DEBITO"));
        dto.setVencimentoDebito(resultSet.getDate("VENCIMENTO_DEBITO"));
        dto.setVencimentoDam(resultSet.getDate("VENCIMENTO_DAM"));
        dto.setEmissaoDam(resultSet.getDate("EMISSAO_DAM"));
        dto.setValorDam(resultSet.getBigDecimal("VALOR_DAM"));
        if (!Strings.isNullOrEmpty(resultSet.getString("SITUACAO_DAM"))) {
            dto.setSituacaoDam(SituacaoDam.valueOf(resultSet.getString("SITUACAO_DAM")).getDescricao());
        }
        return dto;
    }
}
