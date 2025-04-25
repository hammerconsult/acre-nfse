package br.com.webpublico.domain.events;

import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;

public class NotaFiscalCreatedEvent {

    private final NotaFiscalNfseDTO dto;

    public NotaFiscalCreatedEvent(NotaFiscalNfseDTO dto) {
        this.dto = dto;
    }

    public NotaFiscalNfseDTO getDto() {
        return dto;
    }
}
