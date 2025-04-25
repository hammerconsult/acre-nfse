package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.CartaCorrecaoNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartaCorrecaoMapper implements RowMapper<CartaCorrecaoNfseDTO> {
    @Override
    public CartaCorrecaoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        CartaCorrecaoNfseDTO dto = new CartaCorrecaoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setDataEmissao(resultSet.getDate("DATAEMISSAO"));
        dto.setIdNotaFiscal(resultSet.getLong("NOTAFISCAL_ID"));
        dto.setSequencialCartaCorrecao(resultSet.getLong("SEQUENCIALCARTACORRECAO"));
        dto.setDescricaoAlteracao(resultSet.getString("DESCRICAOALTERACAO"));
        return dto;
    }
}
