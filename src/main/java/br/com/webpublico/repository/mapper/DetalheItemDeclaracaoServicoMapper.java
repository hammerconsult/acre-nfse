package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalheItemDeclaracaoServicoMapper implements RowMapper<DetalheItemDeclaracaoServicoNfseDTO> {

    @Override
    public DetalheItemDeclaracaoServicoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        DetalheItemDeclaracaoServicoNfseDTO dto = new DetalheItemDeclaracaoServicoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setQuantidade(resultSet.getBigDecimal("QUANTIDADE"));
        dto.setValorServico(resultSet.getBigDecimal("VALORSERVICO"));
        dto.setDescricao(resultSet.getString("DESCRICAO"));
        return dto;
    }

}
