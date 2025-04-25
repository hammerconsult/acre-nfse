package br.com.webpublico.domain.dto;

import java.util.List;

public class RetornoImportacaoLoteRpsDTO {

    private Long id;
    private List<String> mensagens;

    public RetornoImportacaoLoteRpsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<String> mensagens) {
        this.mensagens = mensagens;
    }
}
