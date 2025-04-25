package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.DeclaracaoPrestacaoServicoNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.domain.dto.TipoDocumentoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class DeclaracaoPrestacaoServicoNfseSetter implements BatchPreparedStatementSetter {

    private NotaFiscalNfseDTO dto;

    public DeclaracaoPrestacaoServicoNfseSetter(NotaFiscalNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        DeclaracaoPrestacaoServicoNfseDTO dec = dto.getDeclaracaoPrestacaoServico();
        ps.setLong(1, dec.getId());
        ps.setString(2, dec.getNaturezaOperacao().name());
        ps.setBoolean(3, dec.getOptanteSimplesNacional());
        ps.setDate(4, DateUtils.toSQLDate(dec.getCompetencia()));
        if (dec.getIntermediario() != null && dec.getIntermediario().getId() != null) {
            ps.setLong(5, dec.getIntermediario().getId());
        } else {
            ps.setNull(5, Types.NULL);
        }
        if (dec.getConstrucaoCivil() != null && dec.getConstrucaoCivil().getId() != null) {
            ps.setLong(6, dec.getConstrucaoCivil().getId());
        } else {
            ps.setNull(6, Types.NULL);
        }
        if (dec.getCondicaoPagamento() != null && dec.getCondicaoPagamento().getId() != null) {
            ps.setLong(7, dec.getCondicaoPagamento().getId());
        } else {
            ps.setNull(7, Types.NULL);
        }
        if (dec.getTributosFederais() != null && dec.getTributosFederais().getId() != null) {
            ps.setLong(8, dec.getTributosFederais().getId());
        } else {
            ps.setNull(8, Types.NULL);
        }
        ps.setBoolean(9, dec.getIssRetido());
        if (dec.getResponsavelRetencao() != null) {
            ps.setString(10, dec.getResponsavelRetencao().name());
        } else {
            ps.setNull(10, Types.NULL);
        }
        ps.setLong(11, dto.getDeclaracaoPrestacaoServico().getDadosPessoaisPrestador().getId());
        if (dto.getTomador() != null && dto.getTomador().getDadosPessoais() != null) {
            ps.setLong(12, dto.getTomador().getDadosPessoais().getId());
        } else {
            ps.setNull(12, Types.NULL);
        }
        ps.setString(13, TipoDocumentoNfseDTO.NFSE.name());
        ps.setBigDecimal(14, dto.getDeducoesLegais());
        ps.setBigDecimal(15, dto.getDescontosIncondicionais());
        ps.setBigDecimal(16, dto.getDescontosCondicionais());
        ps.setBigDecimal(17, dto.getValorLiquido());
        ps.setBigDecimal(18, dto.getRetencoesFederais());
        ps.setBigDecimal(19, dto.getTotalServicos());
        ps.setBigDecimal(20, dto.getBaseCalculo());
        ps.setString(21, dto.getModalidade().name());
        ps.setString(22, dto.getSituacao().name());
        ps.setBigDecimal(23, dto.getIssCalculado());
        ps.setBigDecimal(24, dto.getIssPagar());
        ps.setBigDecimal(25, dto.getTotalNota());
        if (dec.getOrigemEmissao() != null) {
            ps.setString(26, dec.getOrigemEmissao().name());
        } else {
            ps.setNull(26, Types.NULL);
        }

    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
