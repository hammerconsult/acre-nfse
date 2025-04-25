package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.PessoaNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class EnderecoCorreioSetter implements BatchPreparedStatementSetter {

    private Long id;
    private PessoaNfseDTO dto;

    public EnderecoCorreioSetter(Long id, PessoaNfseDTO dto) {
        this.id = id;
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, dto.getDadosPessoais().getBairro());
        preparedStatement.setString(3, dto.getDadosPessoais().getCep());
        preparedStatement.setString(4, dto.getDadosPessoais().getComplemento());
        if (dto.getDadosPessoais().getMunicipio() != null) {
            preparedStatement.setString(5, dto.getDadosPessoais().getMunicipio().getNome());
            preparedStatement.setString(6, dto.getDadosPessoais().getMunicipio().getEstado());
        } else {
            preparedStatement.setNull(5, Types.NULL);
            preparedStatement.setNull(6, Types.NULL);
        }
        preparedStatement.setString(7, dto.getDadosPessoais().getLogradouro());
        preparedStatement.setString(8, dto.getDadosPessoais().getNumero());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
