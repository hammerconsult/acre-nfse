package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.DeclaracaoPrestacaoServicoNfseDTO;
import br.com.webpublico.domain.dto.ServicoDeclaradoNfseDTO;
import br.com.webpublico.domain.dto.TipoDocumentoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class DeclaracaoPrestacaoServicoDeclaradoSetter implements BatchPreparedStatementSetter {

    private ServicoDeclaradoNfseDTO dto;

    public DeclaracaoPrestacaoServicoDeclaradoSetter(ServicoDeclaradoNfseDTO dto) {
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
        if (dto.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador() != null) {
            ps.setLong(12, dto.getDeclaracaoPrestacaoServico().getDadosPessoaisTomador().getId());
        } else {
            ps.setNull(12, Types.NULL);
        }
        ps.setString(13, TipoDocumentoNfseDTO.SERVICO_DECLARADO.name());
        ps.setBigDecimal(14, dto.getDeclaracaoPrestacaoServico().getDeducoesLegais());
        ps.setBigDecimal(15, BigDecimal.ZERO);
        ps.setBigDecimal(16, BigDecimal.ZERO);
        ps.setBigDecimal(17, dto.getDeclaracaoPrestacaoServico().getBaseCalculo());
        ps.setBigDecimal(18, BigDecimal.ZERO);
        ps.setBigDecimal(19, dto.getDeclaracaoPrestacaoServico().getTotalServicos());
        ps.setBigDecimal(20, dto.getDeclaracaoPrestacaoServico().getBaseCalculo());
        ps.setString(21, dto.getDeclaracaoPrestacaoServico().getModalidade().name());
        ps.setString(22, dto.getDeclaracaoPrestacaoServico().getSituacao().name());
        ps.setNull(23, Types.NULL);
        ps.setNull(24, Types.NULL);
        ps.setBigDecimal(25, dto.getDeclaracaoPrestacaoServico().getIssCalculado());
        ps.setBigDecimal(26, dto.getDeclaracaoPrestacaoServico().getIssPagar());
        ps.setBigDecimal(27, dto.getDeclaracaoPrestacaoServico().getTotalServicos());
        if (dec.getOrigemEmissao() != null) {
            ps.setString(28, dec.getOrigemEmissao().name());
        } else {
            ps.setNull(28, Types.NULL);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
