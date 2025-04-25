package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.LoteRpsNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoteRpsSetter implements BatchPreparedStatementSetter {

    private LoteRpsNfseDTO dto;
    private Boolean insert;

    public LoteRpsSetter(LoteRpsNfseDTO dto, Boolean insert) {
        this.dto = dto;
        this.insert = insert;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        if (insert) {
            ps.setLong(1, dto.getId());
            ps.setLong(2, dto.getPrestador().getId());
            ps.setString(3, dto.getNumero().toString());
            ps.setString(4, dto.getSituacao().name());
            ps.setDate(5, DateUtils.toSQLDate(dto.getDataRecebimento()));
            ps.setString(6, dto.getProtocolo());
            ps.setBoolean(7, dto.getReprocessar());
            ps.setString(8, dto.getVersaoSistema());
            ps.setString(9, dto.getVersaoAbrasf());
            ps.setBoolean(10, dto.isHomologacao());
        } else {
            ps.setString(1, dto.getSituacao().name());
            ps.setBoolean(2, dto.getReprocessar());
            ps.setLong(3, dto.getId());
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
