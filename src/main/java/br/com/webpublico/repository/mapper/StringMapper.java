package br.com.webpublico.repository.mapper;

import com.beust.jcommander.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringMapper implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet resultSet, int i) throws SQLException {
        return !Strings.isStringEmpty(resultSet.getString("VALUE")) ? resultSet.getString("VALUE") : "";
    }
}
