package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.ManualDTO;
import br.com.webpublico.domain.dto.TipoManualDTO;

import java.util.List;

/**
 * Created by william on 11/09/17.
 */
public class TipoManualManualDTO {
    private TipoManualDTO tipoManualDTO;
    private List<ManualDTO> manualDTO;

    public TipoManualDTO getTipoManualDTO() {
        return tipoManualDTO;
    }

    public void setTipoManualDTO(TipoManualDTO tipoManualDTO) {
        this.tipoManualDTO = tipoManualDTO;
    }

    public List<ManualDTO> getManualDTO() {
        return manualDTO;
    }

    public void setManualDTO(List<ManualDTO> manualDTO) {
        this.manualDTO = manualDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        br.com.webpublico.domain.dto.TipoManualManualDTO that = (br.com.webpublico.domain.dto.TipoManualManualDTO) o;

        return tipoManualDTO != null ? tipoManualDTO.equals(that.tipoManualDTO) : that.tipoManualDTO == null;
    }

    @Override
    public int hashCode() {
        return tipoManualDTO != null ? tipoManualDTO.hashCode() : 0;
    }
}
