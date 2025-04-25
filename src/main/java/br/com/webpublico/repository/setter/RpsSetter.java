package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.RpsNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class RpsSetter implements BatchPreparedStatementSetter {

    private RpsNfseDTO dto;

    public RpsSetter(RpsNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        ps.setString(2, dto.getNumero());
        ps.setString(3, dto.getSerie());
        ps.setDate(4, DateUtils.toSQLDate(dto.getDataEmissao()));
        ps.setLong(5, dto.getPrestador().getId());
        ps.setString(6, dto.getNaturezaOperacao().name());
        ps.setBoolean(7, dto.getOptanteSimplesNacional());
        ps.setDate(8, DateUtils.toSQLDate(dto.getCompetencia()));
        ps.setLong(9, dto.getDadosPessoaisPrestador().getId());
        if (dto.getDadosPessoaisTomador() != null) {
            ps.setLong(10, dto.getDadosPessoaisTomador().getId());
        } else {
            ps.setNull(10, Types.NULL);
        }
        if (dto.getTributosFederais() != null) {
            ps.setLong(11, dto.getTributosFederais().getId());
        } else {
            ps.setNull(11, Types.NULL);
        }
        ps.setBoolean(12, dto.getIssRetido());
        ps.setBigDecimal(13, dto.getTotalServicos());
        ps.setBigDecimal(14, dto.getDeducoesLegais());
        ps.setBigDecimal(15, dto.getDescontosIncondicionais());
        ps.setBigDecimal(16, dto.getDescontosCondicionais());
        ps.setBigDecimal(17, dto.getRetencoesFederais());
        ps.setBigDecimal(18, dto.getBaseCalculo());
        ps.setBigDecimal(19, dto.getIssCalculado());
        ps.setBigDecimal(20, dto.getIssPagar());
        ps.setBigDecimal(21, dto.getValorLiquido());
        if (dto.getIdLote() != null) {
            ps.setLong(22, dto.getIdLote());
        } else {
            ps.setNull(22, Types.NULL);
        }
        ps.setString(23, dto.getDescriminacaoServico());
        if (dto.getTipoRps() != null) {
            ps.setString(24, dto.getTipoRps().name());
        } else {
            ps.setNull(24, Types.NULL);
        }
        ps.setBoolean(25, dto.getIncentivoFiscal());
        if (dto.getConstrucaoCivil() != null) {
            ps.setLong(26, dto.getConstrucaoCivil().getId());
        } else {
            ps.setNull(26, Types.NULL);
        }
        ps.setBigDecimal(27, dto.getTotalNota());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
