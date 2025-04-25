package br.com.webpublico.domain.dto;

import java.io.Serializable;
import java.util.List;

public class AssistenteDAM implements Serializable {

    private Long idParcela;
    private List<Long> idsDam;

    public Long getIdParcela() {
        return idParcela;
    }

    public void setIdParcela(Long idParcela) {
        this.idParcela = idParcela;
    }

    public List<Long> getIdsDam() {
        return idsDam;
    }

    public void setIdsDam(List<Long> idsDam) {
        this.idsDam = idsDam;
    }
}
