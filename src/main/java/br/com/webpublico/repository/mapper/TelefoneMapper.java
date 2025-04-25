package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.TelefoneNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TelefoneMapper implements RowMapper<TelefoneNfseDTO> {
    @Override
    public TelefoneNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TelefoneNfseDTO dto = new TelefoneNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setTipoTelefone(resultSet.getString("TIPOTELEFONE"));
        dto.setTelefone(resultSet.getString("TELEFONE"));
        dto.setPrincipal(resultSet.getBoolean("PRINCIPAL"));
        return dto;
    }
}
