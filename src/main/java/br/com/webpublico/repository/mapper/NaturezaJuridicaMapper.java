package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.NaturezaJuridicaNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NaturezaJuridicaMapper implements RowMapper<NaturezaJuridicaNfseDTO> {

    @Override
    public NaturezaJuridicaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        NaturezaJuridicaNfseDTO dto = new NaturezaJuridicaNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setDescricao(resultSet.getString("DESCRICAO"));
        return dto;
    }

}
