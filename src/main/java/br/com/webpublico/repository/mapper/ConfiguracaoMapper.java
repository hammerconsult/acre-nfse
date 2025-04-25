package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracaoMapper implements RowMapper<ConfiguracaoNfseDTO> {

    @Override
    public ConfiguracaoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ConfiguracaoNfseDTO dto = new ConfiguracaoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        if (resultSet.getLong("CIDADE_ID") != 0) {
            dto.setMunicipio(new MunicipioNfseDTO());
            dto.getMunicipio().setId(resultSet.getLong("CIDADE_ID"));
        }
        if (resultSet.getLong("PADRAOSERVICOPRESTADO_ID") != 0) {
            dto.setPadraoServicoPrestado(new ServicoNfseDTO());
            dto.getPadraoServicoPrestado().setId(resultSet.getLong("PADRAOSERVICOPRESTADO_ID"));
        }
        dto.setSecretaria(resultSet.getString("SECRETARIA"));
        dto.setDepartamento(resultSet.getString("DEPARTAMENTO"));
        dto.setEndereco(resultSet.getString("ENDERECO"));
        if (resultSet.getLong("ARQUIVOBRASAO_ID") != 0) {
            dto.setArquivoBrasao(new DetentorArquivoComposicaoNfseDTO());
            dto.getArquivoBrasao().setId(resultSet.getLong("ARQUIVOBRASAO_ID"));
        }
        if (resultSet.getLong("TIPOMANUAL_DESIF") != 0) {
            dto.setTipoManualDesif(new TipoManualDTO());
            dto.getTipoManualDesif().setId(resultSet.getLong("TIPOMANUAL_DESIF"));
        }
        return dto;
    }
}
