package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.PessoaNfseDTO;
import br.com.webpublico.domain.dto.SocioNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SocioMapper implements RowMapper<SocioNfseDTO> {
    @Override
    public SocioNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        SocioNfseDTO dto = new SocioNfseDTO();
        dto.setProporcao(resultSet.getDouble("PROPORCAO"));
        dto.setSocio(new PessoaNfseDTO());
        dto.getSocio().setId(resultSet.getLong("SOCIO_ID"));
        return dto;
    }
}
