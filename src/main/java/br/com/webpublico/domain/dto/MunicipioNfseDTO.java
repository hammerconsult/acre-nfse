package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rodolfo on 09/10/17.
 */
public class MunicipioNfseDTO extends AbstractEntity implements RowMapper<MunicipioNfseDTO> {

    private String codigoIbge;
    private String nome;
    private String estado;

    public MunicipioNfseDTO() {
    }

    public MunicipioNfseDTO(String nome, String estado) {
        this.nome = nome;
        this.estado = estado;
    }

    public MunicipioNfseDTO(String codigoIbge, String nome, String estado) {
        this.codigoIbge = codigoIbge;
        this.nome = nome;
        this.estado = estado;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public MunicipioNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        MunicipioNfseDTO dto = new MunicipioNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setCodigoIbge(resultSet.getString("codigoibge"));
        dto.setNome(resultSet.getString("nome"));
        dto.setEstado(resultSet.getString("estado"));
        return dto;
    }
}
