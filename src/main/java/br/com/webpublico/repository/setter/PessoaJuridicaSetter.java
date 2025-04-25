package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.PessoaNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PessoaJuridicaSetter implements BatchPreparedStatementSetter {

    private PessoaNfseDTO dto;

    public PessoaJuridicaSetter(PessoaNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, dto.getId());
        preparedStatement.setString(2, dto.getDadosPessoais().getCpfCnpj());
        preparedStatement.setString(3, dto.getDadosPessoais().getNomeRazaoSocial());
        preparedStatement.setString(4, dto.getDadosPessoais().getNomeFantasia());
        preparedStatement.setString(5, dto.getDadosPessoais().getInscricaoEstadualRg());
        preparedStatement.setString(6, dto.getDadosPessoais().getNomeFantasia());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
