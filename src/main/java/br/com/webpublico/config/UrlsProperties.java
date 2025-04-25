package br.com.webpublico.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "urls")
public class UrlsProperties {

    private String nfse;
    private String webpublico;
    private String damService;
    private String rpsService;

    public String getNfse() {
        return nfse;
    }

    public void setNfse(String nfse) {
        this.nfse = nfse;
    }

    public String getWebpublico() {
        return webpublico;
    }

    public void setWebpublico(String webpublico) {
        this.webpublico = webpublico;
    }

    public String getDamService() {
        return damService;
    }

    public void setDamService(String damService) {
        this.damService = damService;
    }

    public String getRpsService() {
        return rpsService;
    }

    public void setRpsService(String rpsService) {
        this.rpsService = rpsService;
    }

    public String getWebpublicoPathNfse() {
        return getWebpublico() + "/spring/nfse";
    }

    public String getWebpublicoPathTributario() {
        return getWebpublico() + "/spring/tributario";
    }
}
