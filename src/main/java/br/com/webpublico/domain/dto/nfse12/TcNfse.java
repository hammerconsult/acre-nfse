//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.31 at 12:43:16 PM BRT 
//


package br.com.webpublico.domain.dto.nfse12;

import br.com.webpublico.domain.dto.nfse12.TcInfNfse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tcNfse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcNfse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InfNfse" type="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}tcInfNfse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcNfse", propOrder = {
    "infNfse"
})
public class TcNfse {

    @XmlElement(name = "InfNfse", required = true)
    protected TcInfNfse infNfse;

    /**
     * Gets the value of the infNfse property.
     * 
     * @return
     *     possible object is
     *     {@link TcInfNfse }
     *     
     */
    public TcInfNfse getInfNfse() {
        return infNfse;
    }

    /**
     * Sets the value of the infNfse property.
     * 
     * @param value
     *     allowed object is
     *     {@link TcInfNfse }
     *     
     */
    public void setInfNfse(TcInfNfse value) {
        this.infNfse = value;
    }

}
