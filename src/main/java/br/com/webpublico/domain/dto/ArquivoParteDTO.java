package br.com.webpublico.domain.dto;

import java.io.Serializable;

/**
 * Created by wellington on 04/09/17.
 */
public class ArquivoParteDTO implements Serializable {

    public static final int TAMANHO_MAXIMO = 4096;
    private Long id;
    private byte dados[];
    private br.com.webpublico.domain.dto.ArquivoDTO arquivoDTO;

    public ArquivoParteDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDados() {
        return dados;
    }

    public void setDados(byte[] dados) {
        this.dados = dados;
    }

    public br.com.webpublico.domain.dto.ArquivoDTO getArquivoDTO() {
        return arquivoDTO;
    }

    public void setArquivoDTO(ArquivoDTO arquivoDTO) {
        this.arquivoDTO = arquivoDTO;
    }
}
