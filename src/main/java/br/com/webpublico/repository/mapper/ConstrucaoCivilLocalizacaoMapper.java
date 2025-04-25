package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.ConstrucaoCivilLocalizacaoNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConstrucaoCivilLocalizacaoMapper implements RowMapper<ConstrucaoCivilLocalizacaoNfseDTO> {

    @Override
    public ConstrucaoCivilLocalizacaoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ConstrucaoCivilLocalizacaoNfseDTO dto = new ConstrucaoCivilLocalizacaoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        if (dto.getMunicipio() != null) {
            dto.setMunicipio(new MunicipioNfseDTO());
            dto.getMunicipio().setId(resultSet.getLong("CIDADE_ID"));
        }
        dto.setLogradouro(resultSet.getString("LOGRADOURO"));
        dto.setNumero(resultSet.getString("NUMERO"));
        dto.setBairro(resultSet.getString("BAIRRO"));
        dto.setCep(resultSet.getString("CEP"));
        dto.setNomeEmpreendimento(resultSet.getString("NOMEEMPREENDIMENTO"));
        return dto;
    }

}
