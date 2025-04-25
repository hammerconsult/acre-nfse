package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.ArquivoComposicaoNfseDTO;
import br.com.webpublico.domain.dto.DetentorArquivoComposicaoNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetentorArquivoComposicaoMapper implements RowMapper<DetentorArquivoComposicaoNfseDTO> {
    @Override
    public DetentorArquivoComposicaoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        DetentorArquivoComposicaoNfseDTO dto = new DetentorArquivoComposicaoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        if (resultSet.getLong("ARQUIVOCOMPOSICAO_ID") != 0) {
            dto.setArquivo(new ArquivoComposicaoNfseDTO());
            dto.getArquivo().setId(resultSet.getLong("ARQUIVOCOMPOSICAO_ID"));
        }
        return dto;
    }
}
