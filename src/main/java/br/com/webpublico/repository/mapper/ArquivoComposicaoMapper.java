package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.ArquivoComposicaoNfseDTO;
import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArquivoComposicaoMapper implements RowMapper<ArquivoComposicaoNfseDTO> {
    @Override
    public ArquivoComposicaoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoComposicaoNfseDTO dto = new ArquivoComposicaoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setArquivoNfseDTO(new ArquivoNfseDTO());
        dto.getArquivoNfseDTO().setId(resultSet.getLong("ARQUIVO_ID"));
        dto.setDataUpload(resultSet.getDate("DATAUPLOAD"));
        return dto;
    }
}
