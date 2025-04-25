package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.TomadorServicoDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TomadorMapper implements RowMapper<TomadorServicoDTO> {

    @Override
    public TomadorServicoDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TomadorServicoDTO dto = new TomadorServicoDTO();
        dto.setId(resultSet.getLong("ID"));
        if (resultSet.getLong("PRESTADOR_ID") != 0) {
            dto.setPrestador(new PrestadorServicoNfseDTO());
            dto.getPrestador().setId(resultSet.getLong("PRESTADOR_ID"));
        }
        dto.setDadosPessoais(new DadosPessoaisNfseDTO());
        dto.getDadosPessoais().setId(resultSet.getLong("DADOSPESSOAISNFSE_ID"));
        return dto;
    }

}
