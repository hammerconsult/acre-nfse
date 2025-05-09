//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.24 at 03:55:13 PM BRT 
//


package br.com.webpublico.domain.dto.nfse203;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tcDadosConstrucaoCivil complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcDadosConstrucaoCivil">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoObra" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsCodigoObra" minOccurs="0"/>
 *         &lt;element name="NumeroAlvaraConstrucao" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsNumeroAlvara" minOccurs="0"/>
 *         &lt;element name="Art" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsArt"/>
 *         &lt;element name="Incorporacao" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsSimNao" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcDadosConstrucaoCivil", propOrder = {
    "codigoObra",
    "numeroAlvaraConstrucao",
    "art",
    "incorporacao"
})
public class TcDadosConstrucaoCivil {

    @XmlElement(name = "CodigoObra")
    protected String codigoObra;
    @XmlElement(name = "NumeroAlvaraConstrucao")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numeroAlvaraConstrucao;
    @XmlElement(name = "Art", required = true)
    protected String art;
    @XmlElement(name = "Incorporacao")
    protected Byte incorporacao;

    /**
     * Gets the value of the codigoObra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoObra() {
        return codigoObra;
    }

    /**
     * Sets the value of the codigoObra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoObra(String value) {
        this.codigoObra = value;
    }

    /**
     * Gets the value of the numeroAlvaraConstrucao property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroAlvaraConstrucao() {
        return numeroAlvaraConstrucao;
    }

    /**
     * Sets the value of the numeroAlvaraConstrucao property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroAlvaraConstrucao(BigInteger value) {
        this.numeroAlvaraConstrucao = value;
    }

    /**
     * Gets the value of the art property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArt() {
        return art;
    }

    /**
     * Sets the value of the art property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArt(String value) {
        this.art = value;
    }

    /**
     * Gets the value of the incorporacao property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getIncorporacao() {
        return incorporacao;
    }

    /**
     * Sets the value of the incorporacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setIncorporacao(Byte value) {
        this.incorporacao = value;
    }

}
