package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.IntermediarioServicoNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntermediarioMapper implements RowMapper<IntermediarioServicoNfseDTO> {

    @Override
    public IntermediarioServicoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        IntermediarioServicoNfseDTO dto = new IntermediarioServicoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        if (resultSet.getLong("PESSOA_ID") != 0) {
            dto.setIdPessoa(resultSet.getLong("PESSOA_ID"));
        }
        dto.setCpfCnpj(resultSet.getString("CPFCNPJ"));
        dto.setNomeRazaoSocial(resultSet.getString("NOMERAZAOSOCIAL"));
        return dto;
    }

}
