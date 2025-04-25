package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventoContabilDesifNfseDTO extends AbstractEntity implements RowMapper<EventoContabilDesifNfseDTO> {

    private Integer codigo;
    private String descricao;
    private Boolean credito;
    private Boolean debito;

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

    public Boolean getCredito() {
        return credito;
    }

    public void setCredito(Boolean credito) {
        this.credito = credito;
    }

    public Boolean getDebito() {
        return debito;
    }

    public void setDebito(Boolean debito) {
        this.debito = debito;
    }

    @Override
    public EventoContabilDesifNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        EventoContabilDesifNfseDTO dto = new EventoContabilDesifNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setCodigo(resultSet.getInt("codigo"));
        dto.setDescricao(resultSet.getString("descricao"));
        dto.setCredito(resultSet.getBoolean("credito"));
        dto.setDebito(resultSet.getBoolean("debito"));
        return dto;
    }
}
