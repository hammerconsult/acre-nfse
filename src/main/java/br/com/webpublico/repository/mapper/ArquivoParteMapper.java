package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import br.com.webpublico.domain.dto.ArquivoParteNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArquivoParteMapper implements RowMapper<ArquivoParteNfseDTO> {
    @Override
    public ArquivoParteNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoParteNfseDTO dto = new ArquivoParteNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setDados(resultSet.getBytes("DADOS"));
        return dto;
    }
}
