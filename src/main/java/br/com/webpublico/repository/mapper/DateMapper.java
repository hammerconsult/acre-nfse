package br.com.webpublico.repository.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DateMapper implements RowMapper<Date> {

    @Override
    public Date mapRow(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getDate("VALUE");
    }

}
