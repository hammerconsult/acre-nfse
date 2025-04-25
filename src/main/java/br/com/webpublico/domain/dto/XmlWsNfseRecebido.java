package br.com.webpublico.domain.dto;

import java.util.Date;

public class XmlWsNfseRecebido {

    private Long id;
    private Date dataRegistro;
    private String inscricaoMunicipal;
    private String xml;

    public XmlWsNfseRecebido() {
    }

    public XmlWsNfseRecebido(Date dataRegistro, String inscricaoMunicipal, String xml) {
        this.dataRegistro = dataRegistro;
        this.inscricaoMunicipal = inscricaoMunicipal;
        this.xml = xml;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
