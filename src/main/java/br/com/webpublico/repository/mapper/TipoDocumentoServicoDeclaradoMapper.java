package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.TipoDocumentoServicoDeclaradoNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TipoDocumentoServicoDeclaradoMapper implements RowMapper<TipoDocumentoServicoDeclaradoNfseDTO> {
    @Override
    public TipoDocumentoServicoDeclaradoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TipoDocumentoServicoDeclaradoNfseDTO dto = new TipoDocumentoServicoDeclaradoNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setDescricao(resultSet.getString("descricao"));
        dto.setSubSerie(resultSet.getString("subserie"));
        dto.setAtivo(resultSet.getBoolean("ativo"));
        return dto;
    }
}
