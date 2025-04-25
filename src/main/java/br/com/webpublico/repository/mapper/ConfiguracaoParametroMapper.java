package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.ConfiguracaoNfseParametroNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoParametroNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracaoParametroMapper implements RowMapper<ConfiguracaoNfseParametroNfseDTO> {
    @Override
    public ConfiguracaoNfseParametroNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ConfiguracaoNfseParametroNfseDTO dto = new ConfiguracaoNfseParametroNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setTipoParametroNfseDTO(TipoParametroNfseDTO.valueOf(resultSet.getString("TIPOPARAMETRO")));
        dto.setValor(resultSet.getString("VALOR"));
        return dto;
    }
}
