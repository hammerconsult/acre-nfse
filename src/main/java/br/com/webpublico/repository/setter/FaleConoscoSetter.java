package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.FaleConoscoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FaleConoscoSetter implements BatchPreparedStatementSetter {

    private FaleConoscoNfseDTO dto;

    public FaleConoscoSetter(FaleConoscoNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        ps.setString(2, dto.getSituacao().name());
        ps.setString(3, dto.getTipo().name());
        ps.setString(4, dto.getAssunto());
        ps.setDate(5, DateUtils.toSQLDate(dto.getDataEnvio()));
        ps.setString(6, dto.getNome());
        ps.setString(7, dto.getEmail());
        ps.setString(8, dto.getTelefone());
        ps.setString(9, dto.getMensagem());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
