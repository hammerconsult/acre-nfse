package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanDTO implements RowMapper<BooleanDTO> {

    private Boolean value;

    public BooleanDTO() {
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public BooleanDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        BooleanDTO dto = new BooleanDTO();
        dto.setValue(resultSet.getBoolean("VALUE"));
        return dto;
    }
}
