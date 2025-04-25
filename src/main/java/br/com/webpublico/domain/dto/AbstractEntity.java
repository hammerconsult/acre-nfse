package br.com.webpublico.domain.dto;

import br.com.webpublico.exception.OperacaoNaoPermitidaException;

public abstract class AbstractEntity {

    protected Long id;
    protected Boolean update;

    public AbstractEntity() {
        this.update = Boolean.FALSE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public void realizarValidacoes() throws OperacaoNaoPermitidaException {
    }
}
