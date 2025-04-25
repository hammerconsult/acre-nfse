package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.util.Util;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class NotaFiscalSetter implements BatchPreparedStatementSetter {

    private NotaFiscalNfseDTO dto;

    public NotaFiscalSetter(NotaFiscalNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        ps.setString(2, dto.getCodigoVerificacao());
        ps.setLong(3, dto.getDeclaracaoPrestacaoServico().getId());
        ps.setDate(4, DateUtils.toSQLDate(dto.getEmissao()));
        ps.setString(5, dto.getEmails());
        if (dto.getRps() != null && dto.getRps().getId() != null) {
            ps.setLong(6, dto.getRps().getId());
        } else {
            ps.setNull(6, Types.NULL);
        }
        if (dto.getTomador() != null && dto.getTomador().getId() != null) {
            ps.setLong(7, dto.getTomador().getId());
        } else {
            ps.setNull(7, Types.NULL);
        }
        ps.setLong(8, dto.getPrestador().getId());
        ps.setString(9, dto.getDescriminacaoServico());
        ps.setString(10, dto.getChaveAcesso());
        ps.setBoolean(11, dto.getHomologacao());
        ps.setString(12, dto.getInformacoesAdicionais());
        ps.setBoolean(13, dto.getSubstitutoTributario());
        ps.setBoolean(14, Boolean.FALSE);
        ps.setBoolean(15, Boolean.FALSE);
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
