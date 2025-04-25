package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.TemplateNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TemplateMapper implements RowMapper<TemplateNfseDTO> {
    @Override
    public TemplateNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TemplateNfseDTO dto = new TemplateNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setConteudo(resultSet.getString("CONTEUDO"));
        return dto;
    }
}
