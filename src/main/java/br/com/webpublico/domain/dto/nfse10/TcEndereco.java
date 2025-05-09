//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.22 at 01:03:44 PM BRST 
//


package br.com.webpublico.domain.dto.nfse10;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tcEndereco complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="tcEndereco">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Endereco" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsEndereco" minOccurs="0"/>
 *         &lt;element name="Numero" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsNumeroEndereco" minOccurs="0"/>
 *         &lt;element name="Complemento" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsComplementoEndereco" minOccurs="0"/>
 *         &lt;element name="Bairro" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsBairro" minOccurs="0"/>
 *         &lt;element name="CodigoMunicipio" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsCodigoMunicipioIbge" minOccurs="0"/>
 *         &lt;element name="Uf" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsUf" minOccurs="0"/>
 *         &lt;element name="Cep" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsCep" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcEndereco", propOrder = {
        "endereco",
        "numero",
        "complemento",
        "bairro",
        "codigoMunicipio",
        "uf",
        "codigoPais",
        "cep"
})
public class TcEndereco {

    @XmlElement(name = "Endereco")
    protected String endereco;
    @XmlElement(name = "Numero")
    protected String numero;
    @XmlElement(name = "Complemento")
    protected String complemento;
    @XmlElement(name = "Bairro")
    protected String bairro;
    @XmlElement(name = "CodigoMunicipio")
    protected Integer codigoMunicipio;
    @XmlElement(name = "Uf")
    protected String uf;
    @XmlElement(name = "CodigoPais")
    protected String codigoPais;
    @XmlElement(name = "Cep")
    protected Integer cep;

    /**
     * Gets the value of the endereco property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Sets the value of the endereco property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEndereco(String value) {
        this.endereco = value;
    }

    /**
     * Gets the value of the numero property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Sets the value of the numero property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Gets the value of the complemento property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * Sets the value of the complemento property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComplemento(String value) {
        this.complemento = value;
    }

    /**
     * Gets the value of the bairro property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * Sets the value of the bairro property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBairro(String value) {
        this.bairro = value;
    }

    /**
     * Gets the value of the codigoMunicipio property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getCodigoMunicipio() {
        return codigoMunicipio;
    }

    /**
     * Sets the value of the codigoMunicipio property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setCodigoMunicipio(Integer value) {
        this.codigoMunicipio = value;
    }

    /**
     * Gets the value of the uf property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUf() {
        return uf;
    }

    /**
     * Sets the value of the uf property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUf(String value) {
        this.uf = value;
    }

    /**
     * Gets the value of the cep property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getCep() {
        return cep;
    }

    /**
     * Sets the value of the cep property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setCep(Integer value) {
        this.cep = value;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }
}
