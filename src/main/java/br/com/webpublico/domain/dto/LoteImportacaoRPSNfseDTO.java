package br.com.webpublico.domain.dto;

import java.io.Serializable;

public class LoteImportacaoRPSNfseDTO implements Serializable, NfseDTO {

    private String inscricaoCadastral;
    private String file;

    public LoteImportacaoRPSNfseDTO() {
    }

    public String getInscricaoCadastral() {
        return inscricaoCadastral;
    }

    public void setInscricaoCadastral(String inscricaoCadastral) {
        this.inscricaoCadastral = inscricaoCadastral;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
