package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoRelatorioNfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmissaoRelatorioServicoDeclaradoDTO implements Serializable {

    private ConsultaServicoDeclaradoDTO consultaServicoDeclarado;
    private String criteriosUtilizados;
    private TipoRelatorioNfseDTO tipoRelatorio;

    public ConsultaServicoDeclaradoDTO getConsultaServicoDeclarado() {
        return consultaServicoDeclarado;
    }

    public void setConsultaServicoDeclarado(ConsultaServicoDeclaradoDTO consultaServicoDeclarado) {
        this.consultaServicoDeclarado = consultaServicoDeclarado;
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
