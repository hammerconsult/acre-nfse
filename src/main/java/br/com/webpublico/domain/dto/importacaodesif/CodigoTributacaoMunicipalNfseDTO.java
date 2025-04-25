package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CodigoTributacaoMunicipalNfseDTO extends AbstractEntity implements RowMapper<CodigoTributacaoMunicipalNfseDTO> {

    private String codigo;
    private CodigoTributacaoNfseDTO codigoTributacao;
    private BigDecimal aliquota;
    private Date inicioVigencia;
    private Date fimVigencia;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public CodigoTributacaoNfseDTO getCodigoTributacao() {
        return codigoTributacao;
    }

    public void setCodigoTributacao(CodigoTributacaoNfseDTO codigoTributacao) {
        this.codigoTributacao = codigoTributacao;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public Date getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public Date getFimVigencia() {
        return fimVigencia;
    }

    public void setFimVigencia(Date fimVigencia) {
        this.fimVigencia = fimVigencia;
    }

    @Override
    public CodigoTributacaoMunicipalNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        CodigoTributacaoMunicipalNfseDTO dto = new CodigoTributacaoMunicipalNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setCodigo(resultSet.getString("CODIGO"));
        dto.setCodigoTributacao(new CodigoTributacaoNfseDTO());
        dto.getCodigoTributacao().setId(resultSet.getLong("CODIGOTRIBUTACAO_ID"));
        dto.setAliquota(resultSet.getBigDecimal("ALIQUOTA"));
        dto.setInicioVigencia(resultSet.getDate("INICIOVIGENCIA"));
        dto.setFimVigencia(resultSet.getDate("FIMVIGENCIA"));
        return dto;
    }
}
