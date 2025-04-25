package br.com.webpublico.domain.dto;

import br.com.webpublico.DadosGovBr;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracaoGovBrDTO extends AbstractEntity implements RowMapper<ConfiguracaoGovBrDTO>, DadosGovBr  {

    private Long id;
    private String urlProvider;
    private String clientId;
    private String secret;
    private String redirectUri;
    private String codeVerifier;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlProvider() {
        return urlProvider;
    }

    public void setUrlProvider(String urlProvider) {
        this.urlProvider = urlProvider;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

    @Override
    public ConfiguracaoGovBrDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ConfiguracaoGovBrDTO dto = new ConfiguracaoGovBrDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setUrlProvider(resultSet.getString("urlprovider"));
        dto.setClientId(resultSet.getString("clientid"));
        dto.setSecret(resultSet.getString("secret"));
        dto.setRedirectUri(resultSet.getString("redirecturi"));
        dto.setCodeVerifier(resultSet.getString("codeverifier"));
        return dto;
    }
}
