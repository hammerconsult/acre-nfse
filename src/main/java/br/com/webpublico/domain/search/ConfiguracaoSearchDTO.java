package br.com.webpublico.domain.search;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.NfseDTO;

public class ConfiguracaoSearchDTO implements WPSearchDTO {

    private Long id;
    private MunicipioNfseDTO municipio;
    private Boolean verificarAidfe;
    private String logoMunicipio;

    public ConfiguracaoSearchDTO() {
    }

    public ConfiguracaoSearchDTO(ConfiguracaoNfseDTO configuracao) {
        this.id = configuracao.getId();
        this.municipio = configuracao.getMunicipio();
        this.verificarAidfe = configuracao.getVerificarAidfe();
        this.logoMunicipio = configuracao.getLogoMunicipio();
    }

    @Override
    public NfseDTO toNfseDto() {
        ConfiguracaoNfseDTO configuracao = new ConfiguracaoNfseDTO();
        configuracao.setId(this.getId());
        configuracao.setLogoMunicipio(this.getLogoMunicipio());
        configuracao.setMunicipio(this.getMunicipio());
        configuracao.setVerificarAidfe(this.getVerificarAidfe());
        return configuracao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MunicipioNfseDTO getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioNfseDTO municipio) {
        this.municipio = municipio;
    }

    public Boolean getVerificarAidfe() {
        return verificarAidfe;
    }

    public void setVerificarAidfe(Boolean verificarAidfe) {
        this.verificarAidfe = verificarAidfe;
    }

    public String getLogoMunicipio() {
        return logoMunicipio;
    }

    public void setLogoMunicipio(String logoMunicipio) {
        this.logoMunicipio = logoMunicipio;
    }
}
