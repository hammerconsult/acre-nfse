package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TarifaBancariaNfseDTO extends AbstractEntity implements RowMapper<TarifaBancariaNfseDTO> {

    private Integer codigo;
    private String descricao;
    private GrupoTarifaBancariaNfseDTO grupo;
    private PeriodicidadeTarifaBancariaNfseDTO periodicidade;

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

    public GrupoTarifaBancariaNfseDTO getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoTarifaBancariaNfseDTO grupo) {
        this.grupo = grupo;
    }

    public PeriodicidadeTarifaBancariaNfseDTO getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(PeriodicidadeTarifaBancariaNfseDTO periodicidade) {
        this.periodicidade = periodicidade;
    }

    @Override
    public TarifaBancariaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TarifaBancariaNfseDTO dto = new TarifaBancariaNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setCodigo(resultSet.getInt("CODIGO"));
        dto.setDescricao(resultSet.getString("DESCRICAO"));
        dto.setGrupo(GrupoTarifaBancariaNfseDTO.valueOf(resultSet.getString("GRUPO")));
        dto.setPeriodicidade(PeriodicidadeTarifaBancariaNfseDTO.valueOf(resultSet.getString("PERIODICIDADE")));
        return dto;
    }
}
