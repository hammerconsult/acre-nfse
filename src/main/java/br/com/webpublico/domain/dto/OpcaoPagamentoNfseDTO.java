package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OpcaoPagamentoNfseDTO implements Serializable, RowMapper<OpcaoPagamentoNfseDTO> {

    private Long id;
    private Integer diaVencimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    @Override
    public OpcaoPagamentoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        OpcaoPagamentoNfseDTO dto = new OpcaoPagamentoNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setDiaVencimento(resultSet.getInt("diavencimento"));
        return dto;
    }
}
