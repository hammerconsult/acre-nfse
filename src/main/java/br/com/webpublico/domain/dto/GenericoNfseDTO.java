package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenericoNfseDTO implements RowMapper<GenericoNfseDTO> {

    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public GenericoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        GenericoNfseDTO dto = new GenericoNfseDTO();
        dto.setValue(resultSet.getObject("VALUE"));
        return dto;
    }
}
