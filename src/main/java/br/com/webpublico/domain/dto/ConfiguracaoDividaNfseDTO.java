package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracaoDividaNfseDTO implements Serializable, RowMapper<ConfiguracaoDividaNfseDTO> {

    private Long idDivida;
    private Long idTributo;

    public ConfiguracaoDividaNfseDTO() {
    }

    public Long getIdDivida() {
        return idDivida;
    }

    public void setIdDivida(Long idDivida) {
        this.idDivida = idDivida;
    }

    public Long getIdTributo() {
        return idTributo;
    }

    public void setIdTributo(Long idTributo) {
        this.idTributo = idTributo;
    }

    @Override
    public ConfiguracaoDividaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ConfiguracaoDividaNfseDTO dto = new ConfiguracaoDividaNfseDTO();
        dto.setIdDivida(resultSet.getLong("dividanfse_id"));
        dto.setIdTributo(resultSet.getLong("tributo_id"));
        return dto;
    }
}
