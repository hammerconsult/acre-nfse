//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.31 at 12:43:16 PM BRT 
//


package br.com.webpublico.domain.dto.nfse12;

import br.com.webpublico.domain.dto.nfse12.TcConfirmacaoCancelamento;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tcCancelamentoNfse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcCancelamentoNfse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConfirmacaoCancelamento" type="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}tcConfirmacaoCancelamento"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcCancelamentoNfse", propOrder = {
    "confirmacaoCancelamento"
})
public class TcCancelamentoNfse {

    @XmlElement(name = "ConfirmacaoCancelamento", required = true)
    protected TcConfirmacaoCancelamento confirmacaoCancelamento;

    /**
     * Gets the value of the confirmacaoCancelamento property.
     * 
     * @return
     *     possible object is
     *     {@link TcConfirmacaoCancelamento }
     *     
     */
    public TcConfirmacaoCancelamento getConfirmacaoCancelamento() {
        return confirmacaoCancelamento;
    }

    /**
     * Sets the value of the confirmacaoCancelamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link TcConfirmacaoCancelamento }
     *     
     */
    public void setConfirmacaoCancelamento(TcConfirmacaoCancelamento value) {
        this.confirmacaoCancelamento = value;
    }

}
