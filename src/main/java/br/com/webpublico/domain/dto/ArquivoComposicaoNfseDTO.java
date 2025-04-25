package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import br.com.webpublico.domain.dto.NfseDTO;

import java.util.Date;

public class ArquivoComposicaoNfseDTO implements NfseDTO {

    private Long id;
    private ArquivoNfseDTO arquivoNfseDTO;
    private Date dataUpload;

    public ArquivoNfseDTO getArquivoNfseDTO() {
        return arquivoNfseDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setArquivoNfseDTO(ArquivoNfseDTO arquivoNfseDTO) {
        this.arquivoNfseDTO = arquivoNfseDTO;
    }

    public Date getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }
}
