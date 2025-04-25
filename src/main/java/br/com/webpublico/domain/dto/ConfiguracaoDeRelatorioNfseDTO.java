package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracaoDeRelatorioNfseDTO extends AbstractEntity implements RowMapper<ConfiguracaoDeRelatorioNfseDTO> {

    private String url;
    private String urlWebpublico;
    private String chave;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlWebpublico() {
        return urlWebpublico;
    }

    public void setUrlWebpublico(String urlWebpublico) {
        this.urlWebpublico = urlWebpublico;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getParametroBancoUrl(){
        return "?bancodados=" + getChave().toLowerCase();
    }

    public String getUrlCompleta(String api){
        return  getUrl() + api + getParametroBancoUrl();
    }

    @Override
    public ConfiguracaoDeRelatorioNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ConfiguracaoDeRelatorioNfseDTO dto = new ConfiguracaoDeRelatorioNfseDTO();
        dto.setUrl(resultSet.getString("url"));
        dto.setUrlWebpublico(resultSet.getString("urlwebpublico"));
        dto.setChave(resultSet.getString("chave"));
        return dto;
    }
}
