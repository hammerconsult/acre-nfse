package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.ArquivoComposicaoNfseDTO;
import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArquivoMapper implements RowMapper<ArquivoNfseDTO> {
    @Override
    public ArquivoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoNfseDTO dto = new ArquivoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setDescricao(resultSet.getString("DESCRICAO"));
        dto.setMimeType(resultSet.getString("MIMETYPE"));
        dto.setNome(resultSet.getString("NOME"));
        dto.setTamanho(resultSet.getLong("TAMANHO"));
        return dto;
    }
}
