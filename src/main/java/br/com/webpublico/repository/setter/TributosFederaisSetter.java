package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.TributosFederaisNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class TributosFederaisSetter implements BatchPreparedStatementSetter {

    private TributosFederaisNfseDTO dto;
    private boolean update;

    public TributosFederaisSetter(TributosFederaisNfseDTO dto, boolean update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        AtomicInteger index = new AtomicInteger(0);
        if (update) {
            ps.setBigDecimal(index.incrementAndGet(), dto.getPis());
            ps.setBigDecimal(index.incrementAndGet(), dto.getCofins());
            ps.setBigDecimal(index.incrementAndGet(), dto.getInss());
            ps.setBigDecimal(index.incrementAndGet(), dto.getIrrf());
            ps.setBigDecimal(index.incrementAndGet(), dto.getCsll());
            ps.setBigDecimal(index.incrementAndGet(), dto.getOutrasRetencoes());
            ps.setBigDecimal(index.incrementAndGet(), dto.getCpp());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoPis());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoCofins());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoInss());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoIrrf());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoCsll());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoCpp());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoOutrasRetencoes());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualPis());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualCofins());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualInss());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualIrrf());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualCsll());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualCpp());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualOutrasRetencoes());
            ps.setLong(index.incrementAndGet(), dto.getId());
        } else {
            ps.setLong(index.incrementAndGet(), dto.getId());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPis());
            ps.setBigDecimal(index.incrementAndGet(), dto.getCofins());
            ps.setBigDecimal(index.incrementAndGet(), dto.getInss());
            ps.setBigDecimal(index.incrementAndGet(), dto.getIrrf());
            ps.setBigDecimal(index.incrementAndGet(), dto.getCsll());
            ps.setBigDecimal(index.incrementAndGet(), dto.getOutrasRetencoes());
            ps.setBigDecimal(index.incrementAndGet(), dto.getCpp());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoPis());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoCofins());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoInss());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoIrrf());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoCsll());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoCpp());
            ps.setBoolean(index.incrementAndGet(), dto.getRetencaoOutrasRetencoes());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualPis());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualCofins());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualInss());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualIrrf());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualCsll());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualCpp());
            ps.setBigDecimal(index.incrementAndGet(), dto.getPercentualOutrasRetencoes());
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
