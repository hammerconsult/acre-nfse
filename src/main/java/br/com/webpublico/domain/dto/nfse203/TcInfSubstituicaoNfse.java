//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.24 at 03:55:13 PM BRT 
//


package br.com.webpublico.domain.dto.nfse203;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;


/**
 * <p>Java class for tcInfSubstituicaoNfse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcInfSubstituicaoNfse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NfseSubstituidora" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsNumeroNfse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcInfSubstituicaoNfse", propOrder = {
    "nfseSubstituidora"
})
public class TcInfSubstituicaoNfse {

    @XmlElement(name = "NfseSubstituidora", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger nfseSubstituidora;

    /**
     * Gets the value of the nfseSubstituidora property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNfseSubstituidora() {
        return nfseSubstituidora;
    }

    /**
     * Sets the value of the nfseSubstituidora property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNfseSubstituidora(BigInteger value) {
        this.nfseSubstituidora = value;
    }

}
