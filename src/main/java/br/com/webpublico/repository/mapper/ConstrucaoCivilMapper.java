package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.ConstrucaoCivilStatusNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConstrucaoCivilMapper implements RowMapper<ConstrucaoCivilNfseDTO> {

    @Override
    public ConstrucaoCivilNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ConstrucaoCivilNfseDTO dto = new ConstrucaoCivilNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setArt(resultSet.getString("ART"));
        dto.setNumeroAlvara(resultSet.getString("NUMEROALVARA"));
        if (resultSet.getLong("RESPONSAVEL_ID") != 0) {
            dto.setResponsavel(new PessoaNfseDTO());
            dto.getResponsavel().setId(resultSet.getLong("RESPONSAVEL_ID"));
        }
        if (resultSet.getLong("CONSTRUCAOCIVILLOCALIZACAO_ID") != 0) {
            dto.setLocalizacao(new ConstrucaoCivilLocalizacaoNfseDTO());
            dto.getLocalizacao().setId(resultSet.getLong("CONSTRUCAOCIVILLOCALIZACAO_ID"));
        }
        dto.setDataAprovacaoProjeto(resultSet.getDate("DATAAPROVACAOPROJETO"));
        dto.setDataInicio(resultSet.getDate("DATAINICIO"));
        dto.setDataConclusao(resultSet.getDate("DATACONCLUSAO"));
        dto.setIncorporacao(resultSet.getBoolean("INCORPORACAO"));
        dto.setMatriculaImovel(resultSet.getString("MATRICULAIMOVEL"));
        if (resultSet.getLong("CADASTROIMOBILIARIO_ID") != 0) {
            dto.setImovel(new CadastroImobiliarioNfseDTO());
            dto.getImovel().setId(resultSet.getLong("CADASTROIMOBILIARIO_ID"));
        }
        dto.setNumeroHabitese(resultSet.getString("NUMEROHABITESE"));
        if (resultSet.getString("CONSTRUCAOCIVILSTATUS") != null &&
                !resultSet.getString("CONSTRUCAOCIVILSTATUS").isEmpty()) {
            dto.setStatus(ConstrucaoCivilStatusNfseDTO.valueOf(resultSet.getString("CONSTRUCAOCIVILSTATUS")));
        }
        dto.setCodigoObra(""+resultSet.getLong("CODIGOOBRA"));
        dto.setPrestador(new PrestadorServicoNfseDTO());
        dto.getPrestador().setId(resultSet.getLong("CADASTROECONOMICO_ID"));
        return dto;
    }

}
