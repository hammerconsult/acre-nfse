package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoDeducaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoOperacaoNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.PlanoGeralContasComentadoNfseDTO;
import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDeclaracaoServicoNfseDTO implements NfseDTO, RowMapper<ItemDeclaracaoServicoNfseDTO> {

    private Long id;
    private BigDecimal iss;
    private BigDecimal baseCalculo;
    private BigDecimal deducoes;
    private BigDecimal descontosIncondicionados;
    private BigDecimal descontosCondicionados;
    private BigDecimal quantidade;
    private BigDecimal valorServico;
    private String descricao;
    private String processoSuspensao;
    private String nomeServico;
    private BigDecimal aliquotaServico;
    private Boolean prestadoNoPais;
    private String observacoes;
    private CnaeNfseDTO cnae;
    private ServicoNfseDTO servico;
    private MunicipioNfseDTO municipio;
    private PaisNfseDTO pais;
    private Boolean deducao;
    private TipoDeducaoNfseDTO tipoDeducao;
    private TipoOperacaoNfseDTO tipoOperacao;
    private String numeroDocumentoFiscal;
    private String cpfCnpjDeducao;
    private BigDecimal valorDocumentoFiscal;
    private PlanoGeralContasComentadoNfseDTO conta;
    private BigDecimal saldoAnterior;
    private BigDecimal debito;
    private BigDecimal credito;
    private List<DetalheItemDeclaracaoServicoNfseDTO> detalhes;

    public ItemDeclaracaoServicoNfseDTO() {
        iss = BigDecimal.ZERO;
        baseCalculo = BigDecimal.ZERO;
        deducoes = BigDecimal.ZERO;
        descontosIncondicionados = BigDecimal.ZERO;
        descontosCondicionados = BigDecimal.ZERO;
        quantidade = BigDecimal.ZERO;
        valorServico = BigDecimal.ZERO;
        aliquotaServico = BigDecimal.ZERO;
        prestadoNoPais = Boolean.TRUE;
        deducao = Boolean.FALSE;
        valorDocumentoFiscal = BigDecimal.ZERO;
        saldoAnterior = BigDecimal.ZERO;
        debito = BigDecimal.ZERO;
        credito = BigDecimal.ZERO;
        detalhes = Lists.newArrayList();
    }

    @JsonIgnore
    public static br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO copy(br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO itemDeclaracaoServico, DeclaracaoPrestacaoServicoNfseDTO declaracaoPrestacaoServico) {
        br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO item = new br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO();
        item.aliquotaServico = itemDeclaracaoServico.getAliquotaServico();
        item.baseCalculo = itemDeclaracaoServico.getBaseCalculo();
        item.descricao = itemDeclaracaoServico.getDescricao();
        item.deducoes = itemDeclaracaoServico.getDeducoes();
        item.descontosCondicionados = itemDeclaracaoServico.getDescontosCondicionados();
        item.descontosIncondicionados = itemDeclaracaoServico.getDescontosIncondicionados();
        item.iss = itemDeclaracaoServico.getIss();
        item.municipio = itemDeclaracaoServico.getMunicipio();
        item.nomeServico = itemDeclaracaoServico.getNomeServico();
        item.observacoes = itemDeclaracaoServico.getObservacoes();
        item.quantidade = itemDeclaracaoServico.getQuantidade();
        item.pais = itemDeclaracaoServico.getPais();
        item.prestadoNoPais = itemDeclaracaoServico.getPrestadoNoPais();
        item.servico = itemDeclaracaoServico.getServico();
        item.processoSuspensao = itemDeclaracaoServico.getProcessoSuspensao();
        item.cnae = itemDeclaracaoServico.getCnae();
        item.deducao = itemDeclaracaoServico.getDeducao();
        item.tipoDeducao = itemDeclaracaoServico.getTipoDeducao();
        item.tipoOperacao = itemDeclaracaoServico.getTipoOperacao();
        item.numeroDocumentoFiscal = itemDeclaracaoServico.getNumeroDocumentoFiscal();
        item.cpfCnpjDeducao = itemDeclaracaoServico.getCpfCnpjDeducao();
        item.valorDocumentoFiscal = itemDeclaracaoServico.getValorDocumentoFiscal();
        item.setValorServico(itemDeclaracaoServico.getValorServico());
        item.setConta(itemDeclaracaoServico.getConta());
        item.setSaldoAnterior(itemDeclaracaoServico.getSaldoAnterior());
        item.setDebito(itemDeclaracaoServico.getDebito());
        item.setCredito(itemDeclaracaoServico.getCredito());
        item.setDetalhes(itemDeclaracaoServico.getDetalhes());
        return item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getIss() {
        return iss != null ? iss : BigDecimal.ZERO;
    }

    public void setIss(BigDecimal iss) {
        this.iss = iss;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo != null ? baseCalculo : BigDecimal.ZERO;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getDeducoes() {
        return deducoes != null ? deducoes : BigDecimal.ZERO;
    }

    public void setDeducoes(BigDecimal deducoes) {
        this.deducoes = deducoes;
    }

    public BigDecimal getDescontosIncondicionados() {
        return descontosIncondicionados != null ? descontosIncondicionados : BigDecimal.ZERO;
    }

    public void setDescontosIncondicionados(BigDecimal descontosIncondicionados) {
        this.descontosIncondicionados = descontosIncondicionados;
    }

    public BigDecimal getDescontosCondicionados() {
        return descontosCondicionados != null ? descontosCondicionados : BigDecimal.ZERO;
    }

    public void setDescontosCondicionados(BigDecimal descontosCondicionados) {
        this.descontosCondicionados = descontosCondicionados;
    }

    public BigDecimal getQuantidade() {
        return quantidade == null || quantidade.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorServico() {
        return valorServico != null ? valorServico : BigDecimal.ZERO;
    }

    public void setValorServico(BigDecimal valorServico) {
        this.valorServico = valorServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public BigDecimal getAliquotaServico() {
        return aliquotaServico != null ? aliquotaServico : BigDecimal.ZERO;
    }

    public void setAliquotaServico(BigDecimal aliquotaServico) {
        this.aliquotaServico = aliquotaServico;
    }

    public Boolean getPrestadoNoPais() {
        return prestadoNoPais;
    }

    public void setPrestadoNoPais(Boolean prestadoNoPais) {
        this.prestadoNoPais = prestadoNoPais;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public ServicoNfseDTO getServico() {
        return servico;
    }

    public void setServico(ServicoNfseDTO servico) {
        this.servico = servico;
    }

    public MunicipioNfseDTO getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioNfseDTO municipio) {
        this.municipio = municipio;
    }

    public PaisNfseDTO getPais() {
        return pais;
    }

    public void setPais(PaisNfseDTO pais) {
        this.pais = pais;
    }

    public String getProcessoSuspensao() {
        return processoSuspensao;
    }

    public void setProcessoSuspensao(String processoSuspensao) {
        this.processoSuspensao = processoSuspensao;
    }

    public CnaeNfseDTO getCnae() {
        return cnae;
    }

    public void setCnae(CnaeNfseDTO cnae) {
        this.cnae = cnae;
    }

    public Boolean getDeducao() {
        return deducao != null ? deducao : false;
    }

    public void setDeducao(Boolean deducao) {
        this.deducao = deducao;
    }

    public TipoDeducaoNfseDTO getTipoDeducao() {
        return tipoDeducao;
    }

    public void setTipoDeducao(TipoDeducaoNfseDTO tipoDeducao) {
        this.tipoDeducao = tipoDeducao;
    }

    public TipoOperacaoNfseDTO getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacaoNfseDTO tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public String getNumeroDocumentoFiscal() {
        return numeroDocumentoFiscal;
    }

    public void setNumeroDocumentoFiscal(String numeroDocumentoFiscal) {
        this.numeroDocumentoFiscal = numeroDocumentoFiscal;
    }

    public String getCpfCnpjDeducao() {
        return cpfCnpjDeducao;
    }

    public void setCpfCnpjDeducao(String cpfCnpjDeducao) {
        this.cpfCnpjDeducao = cpfCnpjDeducao;
    }

    public BigDecimal getValorDocumentoFiscal() {
        return valorDocumentoFiscal;
    }

    public void setValorDocumentoFiscal(BigDecimal valorDocumentoFiscal) {
        this.valorDocumentoFiscal = valorDocumentoFiscal;
    }

    public PlanoGeralContasComentadoNfseDTO getConta() {
        return conta;
    }

    public void setConta(PlanoGeralContasComentadoNfseDTO conta) {
        this.conta = conta;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public BigDecimal getDescontos() {
        BigDecimal descontos = BigDecimal.ZERO;
        if (descontosCondicionados != null) {
            descontos = descontos.add(descontosCondicionados);
        }
        if (descontosIncondicionados != null) {
            descontos = descontos.add(descontosIncondicionados);
        }
        return descontos;
    }

    public List<DetalheItemDeclaracaoServicoNfseDTO> getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(List<DetalheItemDeclaracaoServicoNfseDTO> detalhes) {
        this.detalhes = detalhes;
    }

    @Override
    public String toString() {
        return "ItemDeclaracaoServicoNfseDTO{" +
                "id=" + id +
                ", iss=" + iss +
                ", baseCalculo=" + baseCalculo +
                ", deducoes=" + deducoes +
                ", descontosIncondicionados=" + descontosIncondicionados +
                ", descontosCondicionados=" + descontosCondicionados +
                ", quantidade=" + quantidade +
                ", valorServico=" + valorServico +
                ", descricao='" + descricao + '\'' +
                ", nomeServico='" + nomeServico + '\'' +
                ", aliquotaServico=" + aliquotaServico +
                ", prestadoNoPais=" + prestadoNoPais +
                ", observacoes='" + observacoes + '\'' +
                ", servico=" + servico +
                ", municipio=" + municipio +
                ", pais=" + pais +
                ", detalhes=" + detalhes +
                '}';
    }

    @Override
    public ItemDeclaracaoServicoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ItemDeclaracaoServicoNfseDTO dto = new ItemDeclaracaoServicoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setIss(resultSet.getBigDecimal("ISS"));
        dto.setBaseCalculo(resultSet.getBigDecimal("BASECALCULO"));
        dto.setDescontosIncondicionados(resultSet.getBigDecimal("DESCONTOSINCONDICIONADOS"));
        dto.setDescontosCondicionados(resultSet.getBigDecimal("DESCONTOSCONDICIONADOS"));
        dto.setQuantidade(resultSet.getBigDecimal("QUANTIDADE"));
        dto.setValorServico(resultSet.getBigDecimal("VALORSERVICO"));
        dto.setDescricao(resultSet.getString("DESCRICAO"));
        dto.setNomeServico(resultSet.getString("NOMESERVICO"));
        dto.setAliquotaServico(resultSet.getBigDecimal("ALIQUOTASERVICO"));
        dto.setPrestadoNoPais(resultSet.getBoolean("PRESTADONOPAIS"));
        dto.setObservacoes(resultSet.getString("OBSERVACOES"));
        if (resultSet.getLong("SERVICO_ID") != 0) {
            dto.setServico(new ServicoNfseDTO());
            dto.getServico().setId(resultSet.getLong("SERVICO_ID"));
            dto.getServico().setPermiteRecolhimentoFora(resultSet.getBoolean("PERMITERECOLHIMENTOFORA"));
        }
        if (resultSet.getLong("MUNICIPIO_ID") != 0) {
            dto.setMunicipio(new MunicipioNfseDTO());
            dto.getMunicipio().setId(resultSet.getLong("MUNICIPIO_ID"));
        }
        if (resultSet.getLong("PAIS_ID") != 0) {
            dto.setPais(new PaisNfseDTO());
            dto.getPais().setId(resultSet.getLong("PAIS_ID"));
        }
        if (resultSet.getLong("CONTA_ID") != 0) {
            dto.setConta(new PlanoGeralContasComentadoNfseDTO());
            dto.getConta().setId(resultSet.getLong("CONTA_ID"));
        }
        dto.setDeducoes(resultSet.getBigDecimal("DEDUCOES"));
        dto.setDeducao(resultSet.getBoolean("DEDUCAO"));
        if (resultSet.getString("TIPODEDUCAO") != null &&
                !resultSet.getString("TIPODEDUCAO").isEmpty()) {
            dto.setTipoDeducao(TipoDeducaoNfseDTO.valueOf(resultSet.getString("TIPODEDUCAO")));
        }
        if (resultSet.getString("TIPOOPERACAO") != null &&
                !resultSet.getString("TIPOOPERACAO").isEmpty()) {
            dto.setTipoOperacao(TipoOperacaoNfseDTO.valueOf(resultSet.getString("TIPOOPERACAO")));
        }
        dto.setNumeroDocumentoFiscal(resultSet.getString("NUMERODOCUMENTOFISCAL"));
        dto.setCpfCnpjDeducao(resultSet.getString("CPFCNPJDEDUCAO"));
        dto.setValorDocumentoFiscal(resultSet.getBigDecimal("VALORDOCUMENTOFISCAL"));
        dto.setSaldoAnterior(resultSet.getBigDecimal("SALDOANTERIOR"));
        dto.setCredito(resultSet.getBigDecimal("CREDITO"));
        dto.setDebito(resultSet.getBigDecimal("DEBITO"));
        return dto;
    }
}
