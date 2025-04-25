package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.TributosFederaisNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TributosFederaisMapper implements RowMapper<TributosFederaisNfseDTO> {

    @Override
    public TributosFederaisNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TributosFederaisNfseDTO dto = new TributosFederaisNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setPis(resultSet.getBigDecimal("PIS"));
        dto.setCofins(resultSet.getBigDecimal("COFINS"));
        dto.setInss(resultSet.getBigDecimal("INSS"));
        dto.setIrrf(resultSet.getBigDecimal("IRRF"));
        dto.setCsll(resultSet.getBigDecimal("CSLL"));
        dto.setOutrasRetencoes(resultSet.getBigDecimal("OUTRASRETENCOES"));
        dto.setCpp(resultSet.getBigDecimal("CPP"));
        dto.setRetencaoPis(resultSet.getBoolean("RETENCAOPIS"));
        dto.setRetencaoCofins(resultSet.getBoolean("RETENCAOCOFINS"));
        dto.setRetencaoInss(resultSet.getBoolean("RETENCAOINSS"));
        dto.setRetencaoIrrf(resultSet.getBoolean("RETENCAOIRRF"));
        dto.setRetencaoCsll(resultSet.getBoolean("RETENCAOCSLL"));
        dto.setRetencaoCpp(resultSet.getBoolean("RETENCAOCPP"));
        dto.setRetencaoOutrasRetencoes(resultSet.getBoolean("RETENCAOOUTRASRETENCOES"));
        dto.setPercentualPis(resultSet.getBigDecimal("PERCENTUALPIS"));
        dto.setPercentualCofins(resultSet.getBigDecimal("PERCENTUALCOFINS"));
        dto.setPercentualInss(resultSet.getBigDecimal("PERCENTUALINSS"));
        dto.setPercentualIrrf(resultSet.getBigDecimal("PERCENTUALIRRF"));
        dto.setPercentualCsll(resultSet.getBigDecimal("PERCENTUALCSLL"));
        dto.setPercentualCpp(resultSet.getBigDecimal("PERCENTUALCPP"));
        dto.setPercentualOutrasRetencoes(resultSet.getBigDecimal("PERCENTUALOUTRASRETENCOES"));
        return dto;
    }


}
