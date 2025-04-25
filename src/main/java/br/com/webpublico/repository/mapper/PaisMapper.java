package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.PaisNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaisMapper implements RowMapper<PaisNfseDTO> {
    @Override
    public PaisNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        PaisNfseDTO dto = new PaisNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setNome(resultSet.getString("NOME"));
        dto.setCodigo(resultSet.getString("CODIGO"));
        dto.setSigla(resultSet.getString("SIGLA"));
        return dto;
    }
}
