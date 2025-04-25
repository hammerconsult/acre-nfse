package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ImpressaoServicoDeclaradoNfseDTO implements RowMapper<ImpressaoServicoDeclaradoNfseDTO> {

    private String tomadorCmc;
    private String tomadorRazaoSocial;
    private String tomadorCpfCnpj;
    private String prestadorCmc;
    private String prestadorRazaoSocial;
    private String prestadorCpfCnpj;
    private Long numero;
    private String modelo;
    private String serie;
    private Date emissao;
    private String tipoDocumento;
    private String municipio;
    private String item;
    private String servico;
    private String discriminacaoServico;
    private BigDecimal valorServico;
    private BigDecimal deducao;
    private BigDecimal baseCalculo;
    private BigDecimal aliquota;
    private BigDecimal imposto;
    private Boolean homologacao;
    private String situacao;

    public String getTomadorCmc() {
        return tomadorCmc;
    }

    public void setTomadorCmc(String tomadorCmc) {
        this.tomadorCmc = tomadorCmc;
    }

    public String getTomadorRazaoSocial() {
        return tomadorRazaoSocial;
    }

    public void setTomadorRazaoSocial(String tomadorRazaoSocial) {
        this.tomadorRazaoSocial = tomadorRazaoSocial;
    }

    public String getTomadorCpfCnpj() {
        return tomadorCpfCnpj;
    }

    public void setTomadorCpfCnpj(String tomadorCpfCnpj) {
        this.tomadorCpfCnpj = tomadorCpfCnpj;
    }

    public String getPrestadorCmc() {
        return prestadorCmc;
    }

    public void setPrestadorCmc(String prestadorCmc) {
        this.prestadorCmc = prestadorCmc;
    }

    public String getPrestadorRazaoSocial() {
        return prestadorRazaoSocial;
    }

    public void setPrestadorRazaoSocial(String prestadorRazaoSocial) {
        this.prestadorRazaoSocial = prestadorRazaoSocial;
    }

    public String getPrestadorCpfCnpj() {
        return prestadorCpfCnpj;
    }

    public void setPrestadorCpfCnpj(String prestadorCpfCnpj) {
        this.prestadorCpfCnpj = prestadorCpfCnpj;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getDiscriminacaoServico() {
        return discriminacaoServico;
    }

    public void setDiscriminacaoServico(String discriminacaoServico) {
        this.discriminacaoServico = discriminacaoServico;
    }

    public BigDecimal getValorServico() {
        return valorServico;
    }

    public void setValorServico(BigDecimal valorServico) {
        this.valorServico = valorServico;
    }

    public BigDecimal getDeducao() {
        return deducao;
    }

    public void setDeducao(BigDecimal deducao) {
        this.deducao = deducao;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public BigDecimal getImposto() {
        return imposto;
    }

    public void setImposto(BigDecimal imposto) {
        this.imposto = imposto;
    }

    public Boolean getHomologacao() {
        return homologacao;
    }

    public void setHomologacao(Boolean homologacao) {
        this.homologacao = homologacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Override
    public ImpressaoServicoDeclaradoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ImpressaoServicoDeclaradoNfseDTO dto = new ImpressaoServicoDeclaradoNfseDTO();
        dto.setTomadorCmc(resultSet.getString("tomadorCmc"));
        dto.setTomadorRazaoSocial(resultSet.getString("tomadorRazaoSocial"));
        dto.setTomadorCpfCnpj(resultSet.getString("tomadorCpfCnpj"));
        dto.setPrestadorCmc(resultSet.getString("prestadorCmc"));
        dto.setPrestadorRazaoSocial(resultSet.getString("prestadorRazaoSocial"));
        dto.setPrestadorCpfCnpj(resultSet.getString("prestadorCpfCnpj"));
        dto.setNumero(resultSet.getLong("numero"));
        dto.setModelo("");
        dto.setSerie("");
        dto.setEmissao(resultSet.getDate("emissao"));
        dto.setTipoDocumento(resultSet.getString("tipoDocumento"));
        dto.setMunicipio(resultSet.getString("municipio"));
        dto.setItem(resultSet.getString("item"));
        dto.setServico(resultSet.getString("servico"));
        dto.setDiscriminacaoServico(resultSet.getString("discriminacaoServico"));
        dto.setValorServico(resultSet.getBigDecimal("valorServico"));
        dto.setDeducao(resultSet.getBigDecimal("deducao"));
        dto.setBaseCalculo(resultSet.getBigDecimal("baseCalculo"));
        dto.setAliquota(resultSet.getBigDecimal("aliquota"));
        dto.setImposto(resultSet.getBigDecimal("imposto"));
        dto.setSituacao(resultSet.getString("situacao"));
        return dto;
    }
}
