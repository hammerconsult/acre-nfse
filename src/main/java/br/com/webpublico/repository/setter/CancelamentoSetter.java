package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.CancelamentoNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class CancelamentoSetter implements BatchPreparedStatementSetter {

    private CancelamentoNfseDTO dto;

    public CancelamentoSetter(CancelamentoNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        ps.setDate(2, DateUtils.toSQLDate(dto.getDataCancelamento()));
        ps.setString(3, dto.getMotivoCancelamento().name());
        ps.setString(4, dto.getSituacaoFiscal().name());
        ps.setString(5, dto.getObservacoesCancelamento());
        ps.setString(6, dto.getObservacoesFiscal());
        ps.setDate(7, DateUtils.toSQLDate(dto.getDataDeferimentoFiscal()));
        if (dto.getNotaFiscalSubstituta() != null) {
            ps.setLong(8, dto.getNotaFiscalSubstituta().getDeclaracaoPrestacaoServico().getId());
        } else {
            ps.setNull(8, Types.NULL);
        }
        if (dto.getUsuarioTomador() != null) {
            ps.setLong(9, dto.getUsuarioTomador().getId());
        } else {
            ps.setNull(9, Types.NULL);
        }
        ps.setString(10, dto.getSituacaoTomador().name());
        if (dto.getDataDeferimentoTomador() != null) {
            ps.setDate(11, DateUtils.toSQLDate(dto.getDataDeferimentoTomador()));
        } else {
            ps.setNull(11, Types.NULL);
        }
        ps.setString(12, dto.getTipoCancelamento().name());
        ps.setString(13, dto.getSolicitante());
        if (dto.getNotaFiscal() != null) {
            ps.setLong(14, dto.getNotaFiscal().getDeclaracaoPrestacaoServico().getId());
        } else {
            ps.setLong(14, dto.getServicoDeclarado().getDeclaracaoPrestacaoServico().getId());
        }
        ps.setString(15, dto.getTipoDocumento().name());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
