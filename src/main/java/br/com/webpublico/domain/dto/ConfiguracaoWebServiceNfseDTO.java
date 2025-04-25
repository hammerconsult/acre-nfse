package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracaoWebServiceNfseDTO extends AbstractEntity implements RowMapper<ConfiguracaoWebServiceNfseDTO> {

    private String chave;
    private String url;
    private String usuario;
    private String senha;
    private String detalhe;

    public ConfiguracaoWebServiceNfseDTO() {
        super();
    }

    public ConfiguracaoWebServiceNfseDTO(Long id, String chave, String url, String usuario, String senha, String detalhe) {
        this();
        this.id = id;
        this.chave = chave;
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
        this.detalhe = detalhe;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

    @Override
    public ConfiguracaoWebServiceNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ConfiguracaoWebServiceNfseDTO(resultSet.getLong("id"), resultSet.getString("chave"),
                resultSet.getString("url"), resultSet.getString("usuario"), resultSet.getString("senha"),
                resultSet.getString("detalhe"));
    }
}
