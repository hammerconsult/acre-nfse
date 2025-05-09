//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.24 at 03:55:13 PM BRT 
//


package br.com.webpublico.domain.dto.nfse203;

import br.com.webpublico.domain.dto.nfse203.TcCpfCnpj;
import br.com.webpublico.domain.dto.nfse203.TcDeclaracaoPrestacaoServico;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for tcLoteRps complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcLoteRps">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroLote" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsNumeroLote"/>
 *         &lt;element name="CpfCnpj" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tcCpfCnpj"/>
 *         &lt;element name="InscricaoMunicipal" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsInscricaoMunicipal" minOccurs="0"/>
 *         &lt;element name="QuantidadeRps" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsQuantidadeRps"/>
 *         &lt;element name="ListaRps">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Rps" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tcDeclaracaoPrestacaoServico" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="versao" use="required" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsVersao" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcLoteRps", propOrder = {
    "numeroLote",
    "cpfCnpj",
    "inscricaoMunicipal",
    "quantidadeRps",
    "listaRps"
})
public class TcLoteRps {

    @XmlElement(name = "NumeroLote", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numeroLote;
    @XmlElement(name = "CpfCnpj", required = true)
    protected TcCpfCnpj cpfCnpj;
    @XmlElement(name = "InscricaoMunicipal")
    protected String inscricaoMunicipal;
    @XmlElement(name = "QuantidadeRps")
    protected int quantidadeRps;
    @XmlElement(name = "ListaRps", required = true)
    protected ListaRps listaRps;
    @XmlAttribute(name = "versao", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String versao;

    /**
     * Gets the value of the numeroLote property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroLote() {
        return numeroLote;
    }

    /**
     * Sets the value of the numeroLote property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroLote(BigInteger value) {
        this.numeroLote = value;
    }

    /**
     * Gets the value of the cpfCnpj property.
     *
     * @return possible object is
     * {@link TcCpfCnpj }
     */
    public TcCpfCnpj getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * Sets the value of the cpfCnpj property.
     *
     * @param value allowed object is
     *              {@link TcCpfCnpj }
     */
    public void setCpfCnpj(TcCpfCnpj value) {
        this.cpfCnpj = value;
    }

    /**
     * Gets the value of the inscricaoMunicipal property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    /**
     * Sets the value of the inscricaoMunicipal property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInscricaoMunicipal(String value) {
        this.inscricaoMunicipal = value;
    }

    /**
     * Gets the value of the quantidadeRps property.
     *
     */
    public int getQuantidadeRps() {
        return quantidadeRps;
    }

    /**
     * Sets the value of the quantidadeRps property.
     * 
     */
    public void setQuantidadeRps(int value) {
        this.quantidadeRps = value;
    }

    /**
     * Gets the value of the listaRps property.
     * 
     * @return
     *     possible object is
     *     {@link ListaRps }
     *
     */
    public ListaRps getListaRps() {
        return listaRps;
    }

    /**
     * Sets the value of the listaRps property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaRps }
     *     
     */
    public void setListaRps(ListaRps value) {
        this.listaRps = value;
    }

    /**
     * Gets the value of the versao property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVersao() {
        return versao;
    }

    /**
     * Sets the value of the versao property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersao(String value) {
        this.versao = value;
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
     *         &lt;element name="Rps" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tcDeclaracaoPrestacaoServico" maxOccurs="unbounded"/>
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
        "rps"
    })
    public static class ListaRps {

        @XmlElement(name = "Rps", required = true)
        protected List<TcDeclaracaoPrestacaoServico> rps;

        /**
         * Gets the value of the rps property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the rps property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRps().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TcDeclaracaoPrestacaoServico }
         *
         *
         */
        public List<TcDeclaracaoPrestacaoServico> getRps() {
            if (rps == null) {
                rps = new ArrayList<TcDeclaracaoPrestacaoServico>();
            }
            return this.rps;
        }

    }

}
