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
 * <p>Java class for tcContato complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="tcContato">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Telefone" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsTelefone" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://nfse.webpublico.com.br/iss/nfse_v1}tsEmail" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcContato", propOrder = {
        "telefone",
        "email"
})
public class TcContato {

    @XmlElement(name = "Telefone")
    protected String telefone;
    @XmlElement(name = "Email")
    protected String email;

    /**
     * Gets the value of the telefone property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Sets the value of the telefone property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelefone(String value) {
        this.telefone = value;
    }

    /**
     * Gets the value of the email property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmail(String value) {
        this.email = value;
    }

}
