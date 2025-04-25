package br.com.webpublico.web.rest.dto;

import br.com.webpublico.tributario.consultadebitos.enums.SituacaoParcelaDTO;

import java.util.Date;
import java.util.List;

public class FiltroParcelaDTO {
    private List<SituacaoParcelaDTO> situacoes;
    private Date vencimentoInicio;
    private Date vencimentoFim;

    public FiltroParcelaDTO() {
    }

    public List<SituacaoParcelaDTO> getSituacoes() {
        return situacoes;
    }

    public void setSituacoes(List<SituacaoParcelaDTO> situacoes) {
        this.situacoes = situacoes;
    }

    public Date getVencimentoInicio() {
        return vencimentoInicio;
    }

    public void setVencimentoInicio(Date vencimentoInicio) {
        this.vencimentoInicio = vencimentoInicio;
    }

    public Date getVencimentoFim() {
        return vencimentoFim;
    }

    public void setVencimentoFim(Date vencimentoFim) {
        this.vencimentoFim = vencimentoFim;
    }
}
