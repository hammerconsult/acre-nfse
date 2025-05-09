//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.24 at 03:55:13 PM BRT 
//


package br.com.webpublico.domain.dto.nfse203;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * <p>Java class for tcInfNfse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="tcInfNfse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Numero" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsNumeroNfse"/>
 *         &lt;element name="CodigoVerificacao" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsCodigoVerificacao"/>
 *         &lt;element name="DataEmissao" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsData"/>
 *         &lt;element name="NfseSubstituida" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsNumeroNfse" minOccurs="0"/>
 *         &lt;element name="OutrasInformacoes" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsOutrasInformacoes" minOccurs="0"/>
 *         &lt;element name="ValoresNfse" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tcValoresNfse"/>
 *         &lt;element name="ValorCredito" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsValor" minOccurs="0"/>
 *         &lt;element name="PrestadorServico" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tcDadosPrestador"/>
 *         &lt;element name="OrgaoGerador" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tcIdentificacaoOrgaoGerador"/>
 *         &lt;element name="DeclaracaoPrestacaoServico" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tcDeclaracaoPrestacaoServico"/>
 *         &lt;element name="ChaveAcesso" type="{http://nfse.webpublico.com.br/iss/nfse_v2_03.xsd}tsChaveAcesso"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcInfNfse", propOrder = {
        "numero",
        "codigoVerificacao",
        "dataEmissao",
        "nfseSubstituida",
        "outrasInformacoes",
        "valoresNfse",
        "valorCredito",
        "prestadorServico",
        "orgaoGerador",
        "declaracaoPrestacaoServico",
        "chaveAcesso",
        "linkAutenticacao"
})
public class TcInfNfse {

    @XmlElement(name = "Numero", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numero;
    @XmlElement(name = "CodigoVerificacao", required = true)
    protected String codigoVerificacao;
    @XmlElement(name = "DataEmissao", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataEmissao;
    @XmlElement(name = "NfseSubstituida")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger nfseSubstituida;
    @XmlElement(name = "OutrasInformacoes")
    protected String outrasInformacoes;
    @XmlElement(name = "ValoresNfse", required = true)
    protected TcValoresNfse valoresNfse;
    @XmlElement(name = "ValorCredito")
    protected BigDecimal valorCredito;
    @XmlElement(name = "PrestadorServico", required = true)
    protected TcDadosPrestador prestadorServico;
    @XmlElement(name = "OrgaoGerador", required = true)
    protected TcIdentificacaoOrgaoGerador orgaoGerador;
    @XmlElement(name = "DeclaracaoPrestacaoServico", required = true)
    protected TcDeclaracaoPrestacaoServico declaracaoPrestacaoServico;
    @XmlElement(name = "ChaveAcesso", required = true)
    protected String chaveAcesso;
    @XmlElement(name = "LinkAutenticacao", required = false)
    protected String linkAutenticacao;

    /**
     * Gets the value of the numero property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getNumero() {
        return numero;
    }

    /**
     * Sets the value of the numero property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setNumero(BigInteger value) {
        this.numero = value;
    }

    /**
     * Gets the value of the codigoVerificacao property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    /**
     * Sets the value of the codigoVerificacao property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodigoVerificacao(String value) {
        this.codigoVerificacao = value;
    }

    /**
     * Gets the value of the dataEmissao property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDataEmissao() {
        return dataEmissao;
    }

    /**
     * Sets the value of the dataEmissao property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setDataEmissao(XMLGregorianCalendar value) {
        this.dataEmissao = value;
    }

    /**
     * Gets the value of the nfseSubstituida property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getNfseSubstituida() {
        return nfseSubstituida;
    }

    /**
     * Sets the value of the nfseSubstituida property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setNfseSubstituida(BigInteger value) {
        this.nfseSubstituida = value;
    }

    /**
     * Gets the value of the outrasInformacoes property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOutrasInformacoes() {
        return outrasInformacoes;
    }

    /**
     * Sets the value of the outrasInformacoes property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOutrasInformacoes(String value) {
        this.outrasInformacoes = value;
    }

    /**
     * Gets the value of the valoresNfse property.
     *
     * @return possible object is
     * {@link TcValoresNfse }
     */
    public TcValoresNfse getValoresNfse() {
        return valoresNfse;
    }

    /**
     * Sets the value of the valoresNfse property.
     *
     * @param value allowed object is
     *              {@link TcValoresNfse }
     */
    public void setValoresNfse(TcValoresNfse value) {
        this.valoresNfse = value;
    }

    /**
     * Gets the value of the valorCredito property.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getValorCredito() {
        return valorCredito;
    }

    /**
     * Sets the value of the valorCredito property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setValorCredito(BigDecimal value) {
        this.valorCredito = value;
    }

    /**
     * Gets the value of the prestadorServico property.
     *
     * @return possible object is
     * {@link TcDadosPrestador }
     */
    public TcDadosPrestador getPrestadorServico() {
        return prestadorServico;
    }

    /**
     * Sets the value of the prestadorServico property.
     *
     * @param value allowed object is
     *              {@link TcDadosPrestador }
     */
    public void setPrestadorServico(TcDadosPrestador value) {
        this.prestadorServico = value;
    }

    /**
     * Gets the value of the orgaoGerador property.
     *
     * @return possible object is
     * {@link TcIdentificacaoOrgaoGerador }
     */
    public TcIdentificacaoOrgaoGerador getOrgaoGerador() {
        return orgaoGerador;
    }

    /**
     * Sets the value of the orgaoGerador property.
     *
     * @param value allowed object is
     *              {@link TcIdentificacaoOrgaoGerador }
     */
    public void setOrgaoGerador(TcIdentificacaoOrgaoGerador value) {
        this.orgaoGerador = value;
    }

    /**
     * Gets the value of the declaracaoPrestacaoServico property.
     *
     * @return possible object is
     * {@link TcDeclaracaoPrestacaoServico }
     */
    public TcDeclaracaoPrestacaoServico getDeclaracaoPrestacaoServico() {
        return declaracaoPrestacaoServico;
    }

    /**
     * Sets the value of the declaracaoPrestacaoServico property.
     *
     * @param value allowed object is
     *              {@link TcDeclaracaoPrestacaoServico }
     */
    public void setDeclaracaoPrestacaoServico(TcDeclaracaoPrestacaoServico value) {
        this.declaracaoPrestacaoServico = value;
    }

    /**
     * Gets the value of the chaveAcesso property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getChaveAcesso() {
        return chaveAcesso;
    }

    /**
     * Sets the value of the chaveAcesso property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setChaveAcesso(String value) {
        this.chaveAcesso = value;
    }

    public String getLinkAutenticacao() {
        return linkAutenticacao;
    }

    public void setLinkAutenticacao(String linkAutenticacao) {
        this.linkAutenticacao = linkAutenticacao;
    }
}
