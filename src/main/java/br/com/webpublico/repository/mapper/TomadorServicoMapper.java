package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.TomadorServicoDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TomadorServicoMapper implements RowMapper<TomadorServicoDTO> {
    @Override
    public TomadorServicoDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TomadorServicoDTO dto = new TomadorServicoDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setDadosPessoais(new DadosPessoaisNfseDTO());
        dto.getDadosPessoais().setNomeRazaoSocial(resultSet.getString("nomerazaosocial"));
        dto.getDadosPessoais().setNomeFantasia(resultSet.getString("nomefantasia"));
        dto.getDadosPessoais().setCpfCnpj(resultSet.getString("cpfcnpj"));
        dto.getDadosPessoais().setEmail(resultSet.getString("email"));
        dto.getDadosPessoais().setApelido(resultSet.getString("apelido"));
        return dto;
    }
}
