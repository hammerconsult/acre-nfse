//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.31 at 12:43:16 PM BRT 
//


package br.com.webpublico.domain.dto.nfse12;

import br.com.webpublico.domain.dto.nfse12.ListaMensagemAlertaRetorno;
import br.com.webpublico.domain.dto.nfse12.ListaMensagemRetorno;
import br.com.webpublico.domain.dto.nfse12.ListaMensagemRetornoLote;
import br.com.webpublico.domain.dto.nfse12.TcCompNfse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="Situacao" type="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}tsSituacaoLoteRps"/>
 *         &lt;choice>
 *           &lt;element name="ListaNfse">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element ref="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}CompNfse" maxOccurs="50"/>
 *                     &lt;element ref="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}ListaMensagemAlertaRetorno" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element ref="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}ListaMensagemRetorno"/>
 *           &lt;element ref="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}ListaMensagemRetornoLote"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "situacao",
    "listaNfse",
    "listaMensagemRetorno",
    "listaMensagemRetornoLote"
})
@XmlRootElement(name = "ConsultarLoteRpsResposta")
public class ConsultarLoteRpsResposta {

    @XmlElement(name = "Situacao")
    protected byte situacao;
    @XmlElement(name = "ListaNfse")
    protected br.com.webpublico.domain.dto.nfse12.ConsultarLoteRpsResposta.ListaNfse listaNfse;
    @XmlElement(name = "ListaMensagemRetorno")
    protected ListaMensagemRetorno listaMensagemRetorno;
    @XmlElement(name = "ListaMensagemRetornoLote")
    protected ListaMensagemRetornoLote listaMensagemRetornoLote;

    /**
     * Gets the value of the situacao property.
     *
     */
    public byte getSituacao() {
        return situacao;
    }

    /**
     * Sets the value of the situacao property.
     *
     */
    public void setSituacao(byte value) {
        this.situacao = value;
    }

    /**
     * Gets the value of the listaNfse property.
     *
     * @return
     *     possible object is
     *     {@link br.com.webpublico.domain.dto.nfse12.ConsultarLoteRpsResposta.ListaNfse }
     *
     */
    public br.com.webpublico.domain.dto.nfse12.ConsultarLoteRpsResposta.ListaNfse getListaNfse() {
        return listaNfse;
    }

    /**
     * Sets the value of the listaNfse property.
     *
     * @param value
     *     allowed object is
     *     {@link br.com.webpublico.domain.dto.nfse12.ConsultarLoteRpsResposta.ListaNfse }
     *
     */
    public void setListaNfse(br.com.webpublico.domain.dto.nfse12.ConsultarLoteRpsResposta.ListaNfse value) {
        this.listaNfse = value;
    }

    /**
     * Gets the value of the listaMensagemRetorno property.
     * 
     * @return
     *     possible object is
     *     {@link ListaMensagemRetorno }
     *     
     */
    public ListaMensagemRetorno getListaMensagemRetorno() {
        return listaMensagemRetorno;
    }

    /**
     * Sets the value of the listaMensagemRetorno property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaMensagemRetorno }
     *     
     */
    public void setListaMensagemRetorno(ListaMensagemRetorno value) {
        this.listaMensagemRetorno = value;
    }

    /**
     * Gets the value of the listaMensagemRetornoLote property.
     * 
     * @return
     *     possible object is
     *     {@link ListaMensagemRetornoLote }
     *     
     */
    public ListaMensagemRetornoLote getListaMensagemRetornoLote() {
        return listaMensagemRetornoLote;
    }

    /**
     * Sets the value of the listaMensagemRetornoLote property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaMensagemRetornoLote }
     *     
     */
    public void setListaMensagemRetornoLote(ListaMensagemRetornoLote value) {
        this.listaMensagemRetornoLote = value;
    }


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
     *         &lt;element ref="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}CompNfse" maxOccurs="50"/>
     *         &lt;element ref="{http://nfse.webpublico.com.br/iss/nfse_v1_2.xsd}ListaMensagemAlertaRetorno" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "compNfse",
        "listaMensagemAlertaRetorno"
    })
    public static class ListaNfse {

        @XmlElement(name = "CompNfse", required = true)
        protected List<TcCompNfse> compNfse;
        @XmlElement(name = "ListaMensagemAlertaRetorno")
        protected ListaMensagemAlertaRetorno listaMensagemAlertaRetorno;

        /**
         * Gets the value of the compNfse property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the compNfse property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCompNfse().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TcCompNfse }
         * 
         * 
         */
        public List<TcCompNfse> getCompNfse() {
            if (compNfse == null) {
                compNfse = new ArrayList<TcCompNfse>();
            }
            return this.compNfse;
        }

        /**
         * Gets the value of the listaMensagemAlertaRetorno property.
         * 
         * @return
         *     possible object is
         *     {@link ListaMensagemAlertaRetorno }
         *     
         */
        public ListaMensagemAlertaRetorno getListaMensagemAlertaRetorno() {
            return listaMensagemAlertaRetorno;
        }

        /**
         * Sets the value of the listaMensagemAlertaRetorno property.
         * 
         * @param value
         *     allowed object is
         *     {@link ListaMensagemAlertaRetorno }
         *     
         */
        public void setListaMensagemAlertaRetorno(ListaMensagemAlertaRetorno value) {
            this.listaMensagemAlertaRetorno = value;
        }

    }

}
