package br.com.webpublico.repository.mapper;

import br.com.webpublico.web.rest.dto.SubstitutoTributarioDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubstitutoTributarioMapper implements RowMapper<SubstitutoTributarioDTO> {
    @Override
    public SubstitutoTributarioDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        SubstitutoTributarioDTO dto = new SubstitutoTributarioDTO();
        dto.setCpf_cnpj(resultSet.getString("CPF_CNPJ"));
        dto.setInscricao(resultSet.getString("INSCRICAO"));
        dto.setNome_razaosocial(resultSet.getString("NOME_RAZAOSOCIAL"));
        dto.setSubstitutoTributario(resultSet.getBoolean("SUBSTITUTO"));
        return dto;
    }
}
