package br.com.webpublico.domain.dto.search;

import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class NotaFiscalSearchDTO extends AbstractEntity implements RowMapper<NotaFiscalSearchDTO> {

    private Long idPrestador;
    private Long idTomador;
    private Long idRps;
    private String codigo;
    private String codigoRps;
    private Long numero;
    private Date emissao;
    private String nomeTomador;
    private String nomePrestador;
    private String cpfCnpjTomador;
    private String cpfCnpjPrestador;
    private String situacao;
    private String modalidade;
    private Boolean issRetido;
    private BigDecimal totalServicos;
    private BigDecimal desconto;
    private BigDecimal deducoes;
    private BigDecimal aliquota;
    private BigDecimal valorLiquido;
    private BigDecimal baseCalculo;
    private BigDecimal iss;
    private BigDecimal issCalculado;
    private Boolean autorizarCancelamento;
    private String tipoDocumentoNfse;
    private String naturezaOperacao;
    private String discriminacaoServico;
    private String serieRps;
    private String tipoRps;

    public NotaFiscalSearchDTO() {
        super();
    }

    public NotaFiscalSearchDTO(NotaFiscalNfseDTO notaFiscal) {
        this();
        this.id = notaFiscal.getId();
        this.setId(notaFiscal.getId());
        this.setCodigo(notaFiscal.getCodigoVerificacao());
        this.setNumero(notaFiscal.getNumero());
        this.setEmissao(notaFiscal.getEmissao());
        this.setNomeTomador(notaFiscal.getTomador().getDadosPessoais().getNomeRazaoSocial());
        this.setNomePrestador(notaFiscal.getPrestador().getPessoa().getDadosPessoais().getNomeRazaoSocial());
        this.setCpfCnpjTomador(notaFiscal.getTomador().getDadosPessoais().getCpfCnpj());
        this.setIdPrestador(notaFiscal.getPrestador().getId());
        this.setSituacao(notaFiscal.getSituacao().name());
        this.setModalidade(notaFiscal.getModalidade().name());
        this.setIssCalculado(notaFiscal.getIssCalculado());
        this.setDiscriminacaoServico(notaFiscal.getDescriminacaoServico());
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    public String getNomeTomador() {
        return nomeTomador;
    }

    public void setNomeTomador(String nomeTomador) {
        this.nomeTomador = nomeTomador;
    }

    public String getCpfCnpjTomador() {
        return cpfCnpjTomador;
    }

    public void setCpfCnpjTomador(String cpfCnpjTomador) {
        this.cpfCnpjTomador = cpfCnpjTomador;
    }

    public Long getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(Long idPrestador) {
        this.idPrestador = idPrestador;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getNomePrestador() {
        return nomePrestador;
    }

    public void setNomePrestador(String nomePrestador) {
        this.nomePrestador = nomePrestador;
    }

    public String getCpfCnpjPrestador() {
        return cpfCnpjPrestador;
    }

    public void setCpfCnpjPrestador(String cpfCnpjPrestador) {
        this.cpfCnpjPrestador = cpfCnpjPrestador;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getIss() {
        return iss;
    }

    public void setIss(BigDecimal iss) {
        this.iss = iss;
    }

    public BigDecimal getIssCalculado() {
        return issCalculado;
    }

    public void setIssCalculado(BigDecimal issCalculado) {
        this.issCalculado = issCalculado;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public Boolean getIssRetido() {
        return issRetido;
    }

    public void setIssRetido(Boolean issRetido) {
        this.issRetido = issRetido;
    }

    public BigDecimal getDeducoes() {
        return deducoes;
    }

    public void setDeducoes(BigDecimal deducoes) {
        this.deducoes = deducoes;
    }

    public Long getIdTomador() {
        return idTomador;
    }

    public void setIdTomador(Long idTomador) {
        this.idTomador = idTomador;
    }

    public Long getIdRps() {
        return idRps;
    }

    public void setIdRps(Long idRps) {
        this.idRps = idRps;
    }

    public String getCodigoRps() {
        return codigoRps;
    }

    public void setCodigoRps(String codigoRps) {
        this.codigoRps = codigoRps;
    }

    public Boolean getAutorizarCancelamento() {
        return autorizarCancelamento;
    }

    public void setAutorizarCancelamento(Boolean autorizarCancelamento) {
        this.autorizarCancelamento = autorizarCancelamento;
    }

    public String getTipoDocumentoNfse() {
        return tipoDocumentoNfse;
    }

    public void setTipoDocumentoNfse(String tipoDocumentoNfse) {
        this.tipoDocumentoNfse = tipoDocumentoNfse;
    }

    public String getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(String naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    public String getDiscriminacaoServico() {
        return discriminacaoServico;
    }

    public void setDiscriminacaoServico(String discriminacaoServico) {
        this.discriminacaoServico = discriminacaoServico;
    }

    public String getSerieRps() {
        return serieRps;
    }

    public void setSerieRps(String serieRps) {
        this.serieRps = serieRps;
    }

    public String getTipoRps() {
        return tipoRps;
    }

    public void setTipoRps(String tipoRps) {
        this.tipoRps = tipoRps;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotaFiscalSearchDTO that = (NotaFiscalSearchDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(idPrestador, that.idPrestador) &&
                Objects.equals(idTomador, that.idTomador) &&
                Objects.equals(idRps, that.idRps) &&
                Objects.equals(codigo, that.codigo) &&
                Objects.equals(codigoRps, that.codigoRps) &&
                Objects.equals(numero, that.numero) &&
                Objects.equals(emissao, that.emissao) &&
                Objects.equals(nomeTomador, that.nomeTomador) &&
                Objects.equals(nomePrestador, that.nomePrestador) &&
                Objects.equals(cpfCnpjTomador, that.cpfCnpjTomador) &&
                Objects.equals(cpfCnpjPrestador, that.cpfCnpjPrestador) &&
                Objects.equals(situacao, that.situacao) &&
                Objects.equals(modalidade, that.modalidade) &&
                Objects.equals(issRetido, that.issRetido) &&
                Objects.equals(totalServicos, that.totalServicos) &&
                Objects.equals(desconto, that.desconto) &&
                Objects.equals(deducoes, that.deducoes) &&
                Objects.equals(aliquota, that.aliquota) &&
                Objects.equals(valorLiquido, that.valorLiquido) &&
                Objects.equals(baseCalculo, that.baseCalculo) &&
                Objects.equals(iss, that.iss) &&
                Objects.equals(issCalculado, that.issCalculado) &&
                Objects.equals(autorizarCancelamento, that.autorizarCancelamento) &&
                Objects.equals(tipoDocumentoNfse, that.tipoDocumentoNfse) &&
                Objects.equals(naturezaOperacao, that.naturezaOperacao) &&
                Objects.equals(discriminacaoServico, that.discriminacaoServico) &&
                Objects.equals(serieRps, that.serieRps) &&
                Objects.equals(tipoRps, that.tipoRps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idPrestador, idTomador, idRps, codigo, codigoRps, numero, emissao, nomeTomador, nomePrestador, cpfCnpjTomador, cpfCnpjPrestador, situacao, modalidade, issRetido, totalServicos, desconto, deducoes, aliquota, valorLiquido, baseCalculo, iss, issCalculado, autorizarCancelamento, tipoDocumentoNfse, naturezaOperacao, discriminacaoServico, serieRps, tipoRps);
    }

    @Override
    public String toString() {
        return "NotaFiscalSearchDTO{" +
                "id=" + id +
                ", idPrestador=" + idPrestador +
                ", codigo='" + codigo + '\'' +
                ", numero=" + numero +
                ", emissao=" + emissao +
                ", nomeTomador='" + nomeTomador + '\'' +
                ", nomePrestador='" + nomePrestador + '\'' +
                ", cpfCnpjTomador='" + cpfCnpjTomador + '\'' +
                ", situacao='" + situacao + '\'' +
                '}';
    }

    @Override
    public NotaFiscalSearchDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        NotaFiscalSearchDTO dto = new NotaFiscalSearchDTO();
        dto.setId(resultSet.getLong("id_nota"));
        dto.setTipoDocumentoNfse(resultSet.getString("tipo_documento"));
        dto.setIdPrestador(resultSet.getLong("id_prestador"));
        dto.setIdTomador(resultSet.getLong("id_tomador"));
        dto.setIdRps(resultSet.getLong("id_rps"));
        dto.setCodigoRps(resultSet.getString("numero_rps"));
        dto.setNumero(resultSet.getLong("numero_nota"));
        dto.setEmissao(resultSet.getDate("emissao_nota"));
        dto.setNomeTomador(resultSet.getString("nome_tomador"));
        dto.setNomePrestador(resultSet.getString("nome_prestador"));
        dto.setCpfCnpjTomador(resultSet.getString("cpfcnpj_tomador"));
        dto.setCpfCnpjPrestador(resultSet.getString("cpfcnpj_prestador"));
        dto.setSituacao(resultSet.getString("situacao"));
        dto.setModalidade(resultSet.getString("modalidade"));
        dto.setIssRetido(resultSet.getBoolean("issretido"));
        dto.setTotalServicos(resultSet.getBigDecimal("totalservicos"));
        dto.setDeducoes(resultSet.getBigDecimal("deducoeslegais"));
        dto.setBaseCalculo(resultSet.getBigDecimal("basecalculo"));
        dto.setIss(resultSet.getBigDecimal("isspagar"));
        dto.setIssCalculado(resultSet.getBigDecimal("isscalculado"));
        dto.setNaturezaOperacao(resultSet.getString("naturezaoperacao"));
        dto.setDiscriminacaoServico(resultSet.getString("descriminacaoservico"));
        dto.setSerieRps(resultSet.getString("serie_rps"));
        dto.setTipoRps(resultSet.getString("tipo_rps"));
        return dto;
    }
}
