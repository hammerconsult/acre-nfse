package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicoDeclaracaoMensalNfseDTO implements Serializable, RowMapper<ServicoDeclaracaoMensalNfseDTO> {

    private Long idServico;
    private BigDecimal aliquota;
    private BigDecimal totalServicos;
    private BigDecimal baseCalculo;
    private BigDecimal iss;

    public ServicoDeclaracaoMensalNfseDTO() {
    }

    public Long getIdServico() {
        return idServico;
    }

    public void setIdServico(Long idServico) {
        this.idServico = idServico;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getIss() {
        return iss;
    }

    public void setIss(BigDecimal iss) {
        this.iss = iss;
    }

    @Override
    public ServicoDeclaracaoMensalNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ServicoDeclaracaoMensalNfseDTO dto = new ServicoDeclaracaoMensalNfseDTO();
        dto.setIdServico(resultSet.getLong("id_servico"));
        dto.setAliquota(resultSet.getBigDecimal("aliquota"));
        dto.setTotalServicos(resultSet.getBigDecimal("total_servicos"));
        dto.setBaseCalculo(resultSet.getBigDecimal("base_calculo"));
        dto.setIss(resultSet.getBigDecimal("iss"));
        return dto;
    }
}
