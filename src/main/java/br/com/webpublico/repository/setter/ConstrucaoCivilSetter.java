package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.ConstrucaoCivilNfseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ConstrucaoCivilSetter implements BatchPreparedStatementSetter {

    private ConstrucaoCivilNfseDTO dto;

    public ConstrucaoCivilSetter(ConstrucaoCivilNfseDTO dto) {
        this.dto = dto;
    }


    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        ps.setString(2, dto.getArt());
        if (dto.getResponsavel() != null) {
            ps.setLong(3, dto.getResponsavel().getId());
        } else {
            ps.setNull(3, Types.NULL);
        }
        if (dto.getLocalizacao() != null) {
            ps.setLong(4, dto.getLocalizacao().getId());
        } else {
            ps.setNull(4, Types.NULL);
        }
        ps.setDate(5, DateUtils.toSQLDate(dto.getDataAprovacaoProjeto()));
        ps.setDate(6, DateUtils.toSQLDate(dto.getDataInicio()));
        ps.setDate(7, DateUtils.toSQLDate(dto.getDataConclusao()));
        ps.setBoolean(8, dto.getIncorporacao());
        ps.setString(9, dto.getMatriculaImovel());
        if (dto.getImovel() != null) {
            ps.setLong(10, dto.getImovel().getId());
        } else {
            ps.setNull(10, Types.NULL);
        }
        ps.setString(11, dto.getNumeroHabitese());
        ps.setString(12, dto.getStatus().name());
        if(dto.getCodigoObra() != null){
            ps.setLong(13, Long.parseLong(dto.getCodigoObra().replaceAll("\\d+", "")));
        }else{
            ps.setNull(13, Types.NULL);
        }
        if (dto.getPrestador() != null) {
            ps.setLong(14, dto.getPrestador().getId());
        } else {
            ps.setNull(14, Types.NULL);
        }
        ps.setString(15, dto.getNumeroAlvara());
    }

    @Override
    public int getBatchSize() {
        return 0;
    }
}
