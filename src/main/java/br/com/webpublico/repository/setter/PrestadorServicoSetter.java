package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrestadorServicoSetter implements BatchPreparedStatementSetter {

    private PrestadorServicoNfseDTO dto;

    public PrestadorServicoSetter(PrestadorServicoNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, dto.getChaveAcesso());
        ps.setString(2, dto.getNomeParaContato());
        ps.setString(3, dto.getTelefoneParaContato());
        ps.setString(4, dto.getResumoSobreEmpresa());
        ps.setLong(5, dto.getId());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
