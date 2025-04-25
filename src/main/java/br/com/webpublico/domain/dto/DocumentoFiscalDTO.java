package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DocumentoFiscalDTO extends AbstractEntity implements RowMapper<DocumentoFiscalDTO> {

    private Long id;
    private TipoDocumentoNfseDTO tipoDocumento;
    private Long idDeclaracao;
    private Long numero;
    private Date emissao;
    private String prestadorCpfCnpj;
    private String prestadorNomeRazaoSocial;
    private String tomadorCpfCnpj;
    private String tomadorNomeRazaoSocial;
    private SituacaoDeclaracaoNfseDTO situacao;
    private ExigibilidadeNfseDTO naturezaOperacao;
    private Boolean issRetido;
    private BigDecimal totalServicos;
    private BigDecimal descontos;
    private BigDecimal deducoes;
    private BigDecimal valorLiquido;
    private BigDecimal baseCalculo;
    private BigDecimal aliquota;
    private BigDecimal issCalculado;
    private BigDecimal issPagar;

    public DocumentoFiscalDTO() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumentoNfseDTO getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoNfseDTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getIdDeclaracao() {
        return idDeclaracao;
    }

    public void setIdDeclaracao(Long idDeclaracao) {
        this.idDeclaracao = idDeclaracao;
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

    public String getPrestadorCpfCnpj() {
        return prestadorCpfCnpj;
    }

    public void setPrestadorCpfCnpj(String prestadorCpfCnpj) {
        this.prestadorCpfCnpj = prestadorCpfCnpj;
    }

    public String getPrestadorNomeRazaoSocial() {
        return prestadorNomeRazaoSocial;
    }

    public void setPrestadorNomeRazaoSocial(String prestadorNomeRazaoSocial) {
        this.prestadorNomeRazaoSocial = prestadorNomeRazaoSocial;
    }

    public String getTomadorCpfCnpj() {
        return tomadorCpfCnpj;
    }

    public void setTomadorCpfCnpj(String tomadorCpfCnpj) {
        this.tomadorCpfCnpj = tomadorCpfCnpj;
    }

    public String getTomadorNomeRazaoSocial() {
        return tomadorNomeRazaoSocial;
    }

    public void setTomadorNomeRazaoSocial(String tomadorNomeRazaoSocial) {
        this.tomadorNomeRazaoSocial = tomadorNomeRazaoSocial;
    }

    public SituacaoDeclaracaoNfseDTO getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoDeclaracaoNfseDTO situacao) {
        this.situacao = situacao;
    }

    public ExigibilidadeNfseDTO getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(ExigibilidadeNfseDTO naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    public Boolean getIssRetido() {
        return issRetido;
    }

    public void setIssRetido(Boolean issRetido) {
        this.issRetido = issRetido;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getDescontos() {
        return descontos;
    }

    public void setDescontos(BigDecimal descontos) {
        this.descontos = descontos;
    }

    public BigDecimal getDeducoes() {
        return deducoes;
    }

    public void setDeducoes(BigDecimal deducoes) {
        this.deducoes = deducoes;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
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

    public BigDecimal getIssCalculado() {
        return issCalculado;
    }

    public void setIssCalculado(BigDecimal issCalculado) {
        this.issCalculado = issCalculado;
    }

    public BigDecimal getIssPagar() {
        return issPagar;
    }

    public void setIssPagar(BigDecimal issPagar) {
        this.issPagar = issPagar;
    }

    @Override
    public DocumentoFiscalDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        DocumentoFiscalDTO dto = new DocumentoFiscalDTO();
        dto.setId(resultSet.getLong("ID"));
        if (!Strings.isNullOrEmpty(resultSet.getString("TIPO_DOCUMENTO"))) {
            dto.setTipoDocumento(TipoDocumentoNfseDTO.valueOf(resultSet.getString("TIPO_DOCUMENTO")));
        }
        dto.setIdDeclaracao(resultSet.getLong("ID_DECLARACAO"));
        dto.setNumero(resultSet.getLong("NUMERO"));
        dto.setEmissao(resultSet.getDate("EMISSAO"));
        dto.setPrestadorCpfCnpj(resultSet.getString("PRESTADOR_CPFCNPJ"));
        dto.setPrestadorNomeRazaoSocial(resultSet.getString("PRESTADOR_NOMERAZAOSOCIAL"));
        dto.setTomadorCpfCnpj(resultSet.getString("TOMADOR_CPFCNPJ"));
        dto.setTomadorNomeRazaoSocial(resultSet.getString("TOMADOR_NOMERAZAOSOCIAL"));
        if (!Strings.isNullOrEmpty(resultSet.getString("NATUREZA_OPERACAO"))) {
            dto.setNaturezaOperacao(ExigibilidadeNfseDTO.valueOf(resultSet.getString("NATUREZA_OPERACAO")));
        }
        if (!Strings.isNullOrEmpty(resultSet.getString("SITUACAO"))) {
            dto.setSituacao(SituacaoDeclaracaoNfseDTO.valueOf(resultSet.getString("SITUACAO")));
        }
        dto.setIssRetido(resultSet.getBoolean("ISS_RETIDO"));
        dto.setTotalServicos(resultSet.getBigDecimal("TOTAL_SERVICOS"));
        dto.setDescontos(resultSet.getBigDecimal("DESCONTOS"));
        dto.setDeducoes(resultSet.getBigDecimal("DEDUCOES"));
        dto.setValorLiquido(resultSet.getBigDecimal("VALOR_LIQUIDO"));
        dto.setBaseCalculo(resultSet.getBigDecimal("BASE_CALCULO"));
        dto.setAliquota(resultSet.getBigDecimal("ALIQUOTA"));
        dto.setIssCalculado(resultSet.getBigDecimal("ISS_CALCULADO"));
        dto.setIssPagar(resultSet.getBigDecimal("ISS_PAGAR"));
        return dto;
    }
}
