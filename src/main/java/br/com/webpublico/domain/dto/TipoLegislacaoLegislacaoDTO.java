package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.LegislacaoDTO;
import br.com.webpublico.domain.dto.TipoLegislacaoDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wellington on 06/09/17.
 */
public class TipoLegislacaoLegislacaoDTO implements Serializable {
    private TipoLegislacaoDTO tipoLegislacaoDTO;
    private List<LegislacaoDTO> legislacoesDTO;

    public TipoLegislacaoLegislacaoDTO() {
    }

    public TipoLegislacaoDTO getTipoLegislacaoDTO() {
        return tipoLegislacaoDTO;
    }

    public void setTipoLegislacaoDTO(TipoLegislacaoDTO tipoLegislacaoDTO) {
        this.tipoLegislacaoDTO = tipoLegislacaoDTO;
    }

    public List<LegislacaoDTO> getLegislacoesDTO() {
        return legislacoesDTO;
    }

    public void setLegislacoesDTO(List<LegislacaoDTO> legislacoesDTO) {
        this.legislacoesDTO = legislacoesDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        br.com.webpublico.domain.dto.TipoLegislacaoLegislacaoDTO that = (br.com.webpublico.domain.dto.TipoLegislacaoLegislacaoDTO) o;

        return tipoLegislacaoDTO != null ? tipoLegislacaoDTO.equals(that.tipoLegislacaoDTO) : that.tipoLegislacaoDTO == null;
    }

    @Override
    public int hashCode() {
        return tipoLegislacaoDTO != null ? tipoLegislacaoDTO.hashCode() : 0;
    }
}
