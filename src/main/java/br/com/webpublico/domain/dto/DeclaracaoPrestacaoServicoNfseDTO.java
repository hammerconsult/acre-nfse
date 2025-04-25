package br.com.webpublico.domain.dto;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.OrigemEmissaoNfseDTO;
import br.com.webpublico.domain.dto.enums.ResponsavelRetencaoNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclaracaoPrestacaoServicoNfseDTO implements NfseDTO, BatchPreparedStatementSetter {

    private Long id;
    private ExigibilidadeNfseDTO naturezaOperacao;
    private Boolean optanteSimplesNacional;
    private Date competencia;
    private IntermediarioServicoNfseDTO intermediario;
    private ConstrucaoCivilNfseDTO construcaoCivil;
    private PagamentoNfseDTO condicaoPagamento;
    private List<ItemDeclaracaoServicoNfseDTO> itens;
    private TributosFederaisNfseDTO tributosFederais;
    private Boolean issRetido;
    private ResponsavelRetencaoNfseDTO responsavelRetencao;
    private CancelamentoNfseDTO cancelamento;
    private SituacaoDeclaracaoNfseDTO situacao;
    private OrigemEmissaoNfseDTO origemEmissao;
    private DadosPessoaisNfseDTO dadosPessoaisPrestador;
    private DadosPessoaisNfseDTO dadosPessoaisTomador;
    private BigDecimal totalServicos;
    private BigDecimal deducoesLegais;
    private BigDecimal baseCalculo;
    private BigDecimal issCalculado;
    private BigDecimal issPagar;
    private NotaFiscalNfseDTO.ModalidadeEmissao modalidade;
    private TipoDocumentoNfseDTO tipoDocumento;
    private BigDecimal descontosIncondicionais;
    private BigDecimal descontosCondicionais;
    private BigDecimal valorLiquido;
    private BigDecimal retencoesFederais;
    private BigDecimal aliquotaServico;

    public DeclaracaoPrestacaoServicoNfseDTO() {
        itens = Lists.newArrayList();
    }

    public DeclaracaoPrestacaoServicoNfseDTO(NotaFiscalNfseDTO notaFiscal) {
        this.issRetido = notaFiscal.getDeclaracaoPrestacaoServico().getIssRetido();
        this.responsavelRetencao = notaFiscal.getDeclaracaoPrestacaoServico().getResponsavelRetencao();
        this.competencia = new Date();
        this.naturezaOperacao = ExigibilidadeNfseDTO.TRIBUTACAO_MUNICIPAL;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIssRetido() {
        return issRetido != null ? issRetido : false;
    }

    public void setIssRetido(Boolean issRetido) {
        this.issRetido = issRetido;
    }

    public ResponsavelRetencaoNfseDTO getResponsavelRetencao() {
        return responsavelRetencao;
    }

    public void setResponsavelRetencao(ResponsavelRetencaoNfseDTO responsavelRetencao) {
        this.responsavelRetencao = responsavelRetencao;
    }

    public ExigibilidadeNfseDTO getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(ExigibilidadeNfseDTO naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    public Boolean getOptanteSimplesNacional() {
        return optanteSimplesNacional != null ? optanteSimplesNacional : Boolean.FALSE;
    }

    public void setOptanteSimplesNacional(Boolean optanteSimplesNacional) {
        this.optanteSimplesNacional = optanteSimplesNacional;
    }

    public Date getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Date competencia) {
        this.competencia = competencia;
    }

    public IntermediarioServicoNfseDTO getIntermediario() {
        return intermediario;
    }

    public void setIntermediario(IntermediarioServicoNfseDTO intermediario) {
        this.intermediario = intermediario;
    }

    public ConstrucaoCivilNfseDTO getConstrucaoCivil() {
        return construcaoCivil;
    }

    public void setConstrucaoCivil(ConstrucaoCivilNfseDTO construcaoCivil) {
        this.construcaoCivil = construcaoCivil;
    }

    public PagamentoNfseDTO getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(PagamentoNfseDTO pagamento) {
        this.condicaoPagamento = pagamento;
    }

    public List<ItemDeclaracaoServicoNfseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemDeclaracaoServicoNfseDTO> itens) {
        this.itens = itens;
    }

    public TributosFederaisNfseDTO getTributosFederais() {
        return tributosFederais;
    }

    public void setTributosFederais(TributosFederaisNfseDTO tributosFederais) {
        this.tributosFederais = tributosFederais;
    }

    public CancelamentoNfseDTO getCancelamento() {
        return cancelamento;
    }

    public void setCancelamento(CancelamentoNfseDTO cancelamento) {
        this.cancelamento = cancelamento;
    }

    public SituacaoDeclaracaoNfseDTO getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoDeclaracaoNfseDTO situacao) {
        this.situacao = situacao;
    }

    public DadosPessoaisNfseDTO getDadosPessoaisPrestador() {
        return dadosPessoaisPrestador;
    }

    public void setDadosPessoaisPrestador(DadosPessoaisNfseDTO dadosPessoaisPrestador) {
        this.dadosPessoaisPrestador = dadosPessoaisPrestador;
    }

    public DadosPessoaisNfseDTO getDadosPessoaisTomador() {
        return dadosPessoaisTomador;
    }

    public void setDadosPessoaisTomador(DadosPessoaisNfseDTO dadosPessoaisTomador) {
        this.dadosPessoaisTomador = dadosPessoaisTomador;
    }

    public OrigemEmissaoNfseDTO getOrigemEmissao() {
        return origemEmissao;
    }

    public void setOrigemEmissao(OrigemEmissaoNfseDTO origemEmissao) {
        this.origemEmissao = origemEmissao;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getDeducoesLegais() {
        return deducoesLegais;
    }

    public void setDeducoesLegais(BigDecimal deducoesLegais) {
        this.deducoesLegais = deducoesLegais;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
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

    public NotaFiscalNfseDTO.ModalidadeEmissao getModalidade() {
        return modalidade;
    }

    public void setModalidade(NotaFiscalNfseDTO.ModalidadeEmissao modalidade) {
        this.modalidade = modalidade;
    }

    public TipoDocumentoNfseDTO getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoNfseDTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public BigDecimal getDescontosIncondicionais() {
        return descontosIncondicionais != null ? descontosIncondicionais : BigDecimal.ZERO;
    }

    public void setDescontosIncondicionais(BigDecimal descontosIncondicionais) {
        this.descontosIncondicionais = descontosIncondicionais;
    }

    public BigDecimal getDescontosCondicionais() {
        return descontosCondicionais != null ? descontosCondicionais : BigDecimal.ZERO;
    }

    public void setDescontosCondicionais(BigDecimal descontosCondicionais) {
        this.descontosCondicionais = descontosCondicionais;
    }

    public BigDecimal getValorLiquido() {
        return valorLiquido != null ? valorLiquido : BigDecimal.ZERO;
    }

    public void setValorLiquido(BigDecimal valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public BigDecimal getRetencoesFederais() {
        return retencoesFederais != null ? retencoesFederais : BigDecimal.ZERO;
    }

    public void setRetencoesFederais(BigDecimal retencoesFederais) {
        this.retencoesFederais = retencoesFederais;
    }

    public BigDecimal getAliquotaServico() {
        return aliquotaServico != null ? aliquotaServico : BigDecimal.ZERO;
    }

    public void setAliquotaServico(BigDecimal aliquotaServico) {
        this.aliquotaServico = aliquotaServico;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, this.getId());
        ps.setString(2, this.getNaturezaOperacao().name());
        ps.setBoolean(3, this.getOptanteSimplesNacional());
        ps.setDate(4, DateUtils.toSQLDate(this.getCompetencia()));
        if (this.getIntermediario() != null && this.getIntermediario().getId() != null) {
            ps.setLong(5, this.getIntermediario().getId());
        } else {
            ps.setNull(5, Types.NULL);
        }
        if (this.getConstrucaoCivil() != null && this.getConstrucaoCivil().getId() != null) {
            ps.setLong(6, this.getConstrucaoCivil().getId());
        } else {
            ps.setNull(6, Types.NULL);
        }
        if (this.getCondicaoPagamento() != null && this.getCondicaoPagamento().getId() != null) {
            ps.setLong(7, this.getCondicaoPagamento().getId());
        } else {
            ps.setNull(7, Types.NULL);
        }
        if (this.getTributosFederais() != null && this.getTributosFederais().getId() != null) {
            ps.setLong(8, this.getTributosFederais().getId());
        } else {
            ps.setNull(8, Types.NULL);
        }
        ps.setBoolean(9, this.getIssRetido());
        if (this.getResponsavelRetencao() != null) {
            ps.setString(10, this.getResponsavelRetencao().name());
        } else {
            ps.setNull(10, Types.NULL);
        }
        ps.setLong(11, this.getDadosPessoaisPrestador().getId());
        if (this.getDadosPessoaisTomador() != null) {
            ps.setLong(12, this.getDadosPessoaisTomador().getId());
        } else {
            ps.setNull(12, Types.NULL);
        }
        ps.setString(13, this.getTipoDocumento().name());
        ps.setBigDecimal(14, this.getDeducoesLegais());
        ps.setBigDecimal(15, this.getDescontosIncondicionais());
        ps.setBigDecimal(16, this.getDescontosCondicionais());
        ps.setBigDecimal(17, this.getValorLiquido());
        ps.setBigDecimal(18, this.getRetencoesFederais());
        ps.setBigDecimal(19, this.getTotalServicos());
        ps.setBigDecimal(20, this.getBaseCalculo());
        ps.setString(21, this.getModalidade().name());
        ps.setString(22, this.getSituacao().name());
        if (this.getCancelamento() != null &&
                this.getCancelamento().getId() != null) {
            ps.setLong(23, this.getCancelamento().getId());
        } else {
            ps.setNull(23, Types.NULL);
        }
        ps.setNull(24, Types.NULL);
        ps.setBigDecimal(25, this.getIssCalculado());
        ps.setBigDecimal(26, this.getIssPagar());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
