package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TipoDependenciaDesifNfseDTO extends AbstractEntity implements RowMapper<TipoDependenciaDesifNfseDTO> {

    private Integer codigo;
    private String descricao;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public TipoDependenciaDesifNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TipoDependenciaDesifNfseDTO dto = new TipoDependenciaDesifNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setCodigo(resultSet.getInt("codigo"));
        dto.setDescricao(resultSet.getString("descricao"));
        return dto;
    }
}
