package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotaFiscalEmailSearchMapper implements RowMapper<NotaFiscalNfseDTO> {

    @Override
    public NotaFiscalNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        NotaFiscalNfseDTO dto = new NotaFiscalNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setNumero(resultSet.getLong("numero"));
        dto.setHomologacao(resultSet.getBoolean("homologacao"));
        dto.setEmails(resultSet.getString("email"));

        return dto;
    }

}
