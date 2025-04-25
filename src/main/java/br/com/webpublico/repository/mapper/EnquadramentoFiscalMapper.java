package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnquadramentoFiscalMapper implements RowMapper<EnquadramentoFiscalNfseDTO> {
    @Override
    public EnquadramentoFiscalNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        EnquadramentoFiscalNfseDTO dto = new EnquadramentoFiscalNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        if (!StringUtils.isEmpty(resultSet.getString("PORTE"))) {
            dto.setPorte(TipoPorteNfeDTO.valueOf(resultSet.getString("PORTE")));
        }
        if (!StringUtils.isEmpty(resultSet.getString("TIPOCONTRIBUINTE"))) {
            dto.setTipoContribuinte(TipoContribuinteNfseDTO.valueOf(resultSet.getString("TIPOCONTRIBUINTE")));
        }
        if (!StringUtils.isEmpty(resultSet.getString("CLASSIFICACAOATIVIDADE"))) {
            dto.setClassificacaoAtividade(ClassificacaoAtividadeNfseDTO.valueOf(resultSet.getString("CLASSIFICACAOATIVIDADE")));
        }
        if (!StringUtils.isEmpty(resultSet.getString("TIPONOTAFISCALSERVICO"))) {
            dto.setTipoNotaFiscal(TipoNotaFiscalServicoNfseDTO.valueOf(resultSet.getString("TIPONOTAFISCALSERVICO")));
        }
        dto.setSubstitutoTributario(resultSet.getBoolean("SUBSTITUTOTRIBUTARIO"));
        if (!StringUtils.isEmpty(resultSet.getString("REGIMETRIBUTARIO"))) {
            dto.setRegimeTributario(RegimeTributarioNfseDTO.valueOf(resultSet.getString("REGIMETRIBUTARIO")));
        }
        if (!StringUtils.isEmpty(resultSet.getString("TIPOISSQN"))) {
            dto.setTipoIssqn(TipoIssqnNfseDTO.valueOf(resultSet.getString("TIPOISSQN")));
            dto.setSimplesNacional(TipoIssqnNfseDTO.SIMPLES_NACIONAL.equals(dto.getTipoIssqn()));
        }
        if (!StringUtils.isEmpty(resultSet.getString("REGIMEESPECIALTRIBUTACAO"))) {
            dto.setRegimeEspecialTributacao(RegimeEspecialTributacaoNfseDTO.valueOf(resultSet.getString("REGIMEESPECIALTRIBUTACAO")));
            dto.setInstituicaoFinanceira(RegimeEspecialTributacaoNfseDTO.INSTITUICAO_FINANCEIRA.equals(dto.getRegimeEspecialTributacao()));
        }
        if (resultSet.getLong("BANCO_ID") != 0) {
            dto.setBanco(new BancoNfseDTO());
            dto.getBanco().setId(resultSet.getLong("BANCO_ID"));
        }
        if (!StringUtils.isEmpty(resultSet.getString("VERSAODESIF"))) {
            dto.setVersaoDesif(VersaoDesifNfseDTO.valueOf(resultSet.getString("VERSAODESIF")));
        }
        if (resultSet.getLong("ESCRITORIOCONTABIL_ID") != 0) {
            dto.setEscritorioContabil(new EscritorioContabilNfseDTO());
            dto.getEscritorioContabil().setId(resultSet.getLong("ESCRITORIOCONTABIL_ID"));
        }
        return dto;
    }
}
