package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.IntermediarioServicoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class IntermediarioServicoSetter implements BatchPreparedStatementSetter {

    private IntermediarioServicoNfseDTO dto;

    public IntermediarioServicoSetter(IntermediarioServicoNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        if (dto.getIdPessoa() != null) {
            ps.setLong(2, dto.getIdPessoa());
        } else {
            ps.setNull(2, Types.NULL);
        }
        ps.setString(3, dto.getCpfCnpj());
        ps.setString(4, dto.getNomeRazaoSocial());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
