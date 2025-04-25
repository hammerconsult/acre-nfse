package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.ConstrucaoCivilLocalizacaoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ConstrucaoCivilLocalizacaoSetter implements BatchPreparedStatementSetter {

    private ConstrucaoCivilLocalizacaoNfseDTO dto;

    public ConstrucaoCivilLocalizacaoSetter(ConstrucaoCivilLocalizacaoNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        if (dto.getMunicipio() != null) {
            ps.setLong(2, dto.getMunicipio().getId());
        } else {
            ps.setNull(2, Types.NULL);
        }
        ps.setString(3, dto.getLogradouro());
        ps.setString(4, dto.getNumero());
        ps.setString(5, dto.getBairro());
        ps.setString(6, dto.getCep());
        ps.setString(7, dto.getNomeEmpreendimento());
    }

    @Override
    public int getBatchSize() {
        return 0;
    }
}
