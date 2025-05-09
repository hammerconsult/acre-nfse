//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.22 at 01:03:44 PM BRST 
//


package br.com.webpublico.domain.dto.nfse10;

import br.com.webpublico.domain.dto.nfse10.SignatureType;
import br.com.webpublico.domain.dto.nfse10.TcLoteRps;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LoteRps" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tcLoteRps"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "loteRps",
        "signature"
})
@XmlRootElement(name = "EnviarLoteRpsEnvio")
public class EnviarLoteRpsEnvio {

    @XmlElement(name = "LoteRps", required = true)
    protected TcLoteRps loteRps;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;

    /**
     * Gets the value of the loteRps property.
     *
     * @return possible object is
     * {@link TcLoteRps }
     */
    public TcLoteRps getLoteRps() {
        return loteRps;
    }

    /**
     * Sets the value of the loteRps property.
     *
     * @param value allowed object is
     *              {@link TcLoteRps }
     */
    public void setLoteRps(TcLoteRps value) {
        this.loteRps = value;
    }

    /**
     * Gets the value of the signature property.
     *
     * @return possible object is
     * {@link SignatureType }
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     *
     * @param value allowed object is
     *              {@link SignatureType }
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

}
