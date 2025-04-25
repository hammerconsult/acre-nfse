package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.PrestadorUsuarioNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioWebMapper implements RowMapper<UserNfseDTO> {
    @Override
    public UserNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        UserNfseDTO dto = new UserNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setLogin(resultSet.getString("LOGIN"));
        dto.setNome(resultSet.getString("NOME"));
        dto.setEmail(resultSet.getString("EMAIL"));
        dto.setPassword(resultSet.getString("PASSWORD"));
        dto.setActivated(resultSet.getBoolean("ACTIVATED"));
        dto.setPessoaFisica(resultSet.getBoolean("IS_PESSOAFISICA"));
        if (resultSet.getLong("PESSOA_ID") != 0) {
            dto.setPessoaId(resultSet.getLong("PESSOA_ID"));
        }
        if (resultSet.getLong("USEREMPRESA_ID") > 0) {
            dto.setEmpresa(new PrestadorUsuarioNfseDTO());
            dto.getEmpresa().setId(resultSet.getLong("USEREMPRESA_ID"));
            dto.getEmpresa().setPrestador(new PrestadorServicoNfseDTO());
            dto.getEmpresa().getPrestador().setId(resultSet.getLong("PRESTADOR_ID"));
        }
        dto.setPasswordTemporary(resultSet.getBoolean("PASSWORDTEMPORARY"));
        return dto;
    }
}
