package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.PessoaNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PessoaSetter implements BatchPreparedStatementSetter {

    private PessoaNfseDTO dto;
    private boolean update = false;

    public PessoaSetter(PessoaNfseDTO dto) {
        this.dto = dto;
    }

    public PessoaSetter(PessoaNfseDTO dto, boolean update) {
        this(dto);
        this.update = update;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setString(1, dto.getDadosPessoais().getEmail());
        preparedStatement.setString(2, dto.getDadosPessoais().getSite());
        preparedStatement.setLong(3, dto.getId());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
