package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoRelatorioNfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmissaoRelatorioRpsDTO implements Serializable {

    private ConsultaGenericaNfseDTO consultaGenerica;
    private String criteriosUtilizados;
    private TipoRelatorioNfseDTO tipoRelatorio;

    public ConsultaGenericaNfseDTO getConsultaGenerica() {
        return consultaGenerica;
    }

    public void setConsultaGenerica(ConsultaGenericaNfseDTO consultaGenerica) {
        this.consultaGenerica = consultaGenerica;
    }

    public String getCriteriosUtilizados() {
        return criteriosUtilizados;
    }

    public void setCriteriosUtilizados(String criteriosUtilizados) {
        this.criteriosUtilizados = criteriosUtilizados;
    }

    public TipoRelatorioNfseDTO getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(TipoRelatorioNfseDTO tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }
}
