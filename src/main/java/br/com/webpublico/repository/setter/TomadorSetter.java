package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.TomadorServicoDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class TomadorSetter implements BatchPreparedStatementSetter {

    private TomadorServicoDTO dto;

    public TomadorSetter(TomadorServicoDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        if (dto.getPrestador() != null && dto.getPrestador().getId() != null) {
            ps.setLong(2, dto.getPrestador().getId());
        } else {
            ps.setNull(2, Types.NULL);
        }
        if (dto.getDadosPessoais() != null && dto.getDadosPessoais().getId() != null) {
            ps.setLong(3, dto.getDadosPessoais().getId());
        } else {
            ps.setNull(3, Types.NULL);
        }
        ps.setBoolean(4, dto.getAtivo());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
