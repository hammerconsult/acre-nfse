package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.DadosPessoaisNfseDTO;
import br.com.webpublico.domain.dto.EscritorioContabilNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EscritorioContabilMapper implements RowMapper<EscritorioContabilNfseDTO> {

    @Override
    public EscritorioContabilNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        EscritorioContabilNfseDTO dto = new EscritorioContabilNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setCodigo(resultSet.getLong("CODIGO"));
        dto.setCrcContador(resultSet.getString("CRCCONTADOR"));
        dto.setCrcEscritorio(resultSet.getString("CRCESCRITORIO"));
        if (resultSet.getLong("PESSOA_ID") != 0) {
            dto.setDadosPessoais(new DadosPessoaisNfseDTO());
            dto.getDadosPessoais().setId(resultSet.getLong("PESSOA_ID"));
        }
        if (resultSet.getLong("RESPONSAVEL_ID") != 0) {
            dto.setResponsavel(new DadosPessoaisNfseDTO());
            dto.getResponsavel().setId(resultSet.getLong("RESPONSAVEL_ID"));
        }
        return dto;
    }
}
