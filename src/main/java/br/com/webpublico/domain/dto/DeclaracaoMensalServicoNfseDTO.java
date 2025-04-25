package br.com.webpublico.domain.dto;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.enums.LancadoPorNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoMensalServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoDeclaracaoMensalServicoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoMovimentoMensalNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.DeclaracaoContaBancariaNfseDTO;
import br.com.webpublico.domain.dto.search.NotaFiscalSearchDTO;
import br.com.webpublico.domain.enumeration.Mes;
import com.beust.jcommander.internal.Lists;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

public class DeclaracaoMensalServicoNfseDTO implements NfseDTO, BatchPreparedStatementSetter, RowMapper<DeclaracaoMensalServicoNfseDTO> {

    private Long id;
    private Integer codigo;
    private Integer mes;
    private Integer exercicio;
    private Date competencia;
    private SituacaoDeclaracaoMensalServicoNfseDTO situacao;
    private TipoDeclaracaoMensalServicoNfseDTO tipo;
    private TipoMovimentoMensalNfseDTO tipoMovimento;
    private List<NotaFiscalSearchDTO> notas;
    private List<NotaFiscalSearchDTO> notasNaoDeclaradas;
    private PrestadorServicoNfseDTO prestador;
    private String situacaoDebito;
    private Integer qtdNotas;
    private BigDecimal totalServicos;
    private BigDecimal totalIss;
    private BigDecimal totalJuros;
    private BigDecimal totalMulta;
    private BigDecimal totalCorrecao;
    private BigDecimal totalHonorarios;
    private Date vencimento;
    private Date abertura;
    private Date encerramento;
    private String usuarioDeclaracao;
    private List<NotaDeclaradaNfseDTO> notasDeclaradas;
    private LancadoPorNfseDTO lancadoPor;
    private List<DeclaracaoContaBancariaNfseDTO> contasBancarias;
    private Date referencia;
    private Long idExercicio;
    private Long idCalculo;
    private Date dataCancelamento;
    private UserNfseDTO usuarioCancelamento;
    private Long idArquivoDesif;
    private boolean todasNotasDoMes;

    public DeclaracaoMensalServicoNfseDTO() {
        this.totalServicos = BigDecimal.ZERO;
        this.totalIss = BigDecimal.ZERO;
        this.totalJuros = BigDecimal.ZERO;
        this.totalMulta = BigDecimal.ZERO;
        this.totalCorrecao = BigDecimal.ZERO;
        this.totalHonorarios = BigDecimal.ZERO;
        this.contasBancarias = Lists.newArrayList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Date competencia) {
        this.competencia = competencia;
    }

    public TipoDeclaracaoMensalServicoNfseDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeclaracaoMensalServicoNfseDTO tipo) {
        this.tipo = tipo;
    }

    public List<NotaFiscalSearchDTO> getNotas() {
        return notas;
    }

    public void setNotas(List<NotaFiscalSearchDTO> notas) {
        this.notas = notas;
    }

    public List<NotaFiscalSearchDTO> getNotasNaoDeclaradas() {
        return notasNaoDeclaradas;
    }

    public void setNotasNaoDeclaradas(List<NotaFiscalSearchDTO> notasNaoDeclaradas) {
        this.notasNaoDeclaradas = notasNaoDeclaradas;
    }

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getExercicio() {
        return exercicio;
    }

    public void setExercicio(Integer exercicio) {
        this.exercicio = exercicio;
    }

    public SituacaoDeclaracaoMensalServicoNfseDTO getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoDeclaracaoMensalServicoNfseDTO situacao) {
        this.situacao = situacao;
    }

    public String getSituacaoDebito() {
        return situacaoDebito;
    }

    public void setSituacaoDebito(String situacaoDebito) {
        this.situacaoDebito = situacaoDebito;
    }

    public Integer getQtdNotas() {
        return qtdNotas;
    }

    public void setQtdNotas(Integer qtdNotas) {
        this.qtdNotas = qtdNotas;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos != null ? totalServicos : BigDecimal.ZERO;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getTotalIss() {
        return totalIss != null ? totalIss : BigDecimal.ZERO;
    }

    public void setTotalIss(BigDecimal totalIss) {
        this.totalIss = totalIss;
    }

    public BigDecimal getTotalJuros() {
        return totalJuros != null ? totalJuros : BigDecimal.ZERO;
    }

    public void setTotalJuros(BigDecimal totalJuros) {
        this.totalJuros = totalJuros;
    }

    public BigDecimal getTotalMulta() {
        return totalMulta != null ? totalMulta : BigDecimal.ZERO;
    }

    public void setTotalMulta(BigDecimal totalMulta) {
        this.totalMulta = totalMulta;
    }

    public BigDecimal getTotalCorrecao() {
        return totalCorrecao != null ? totalCorrecao : BigDecimal.ZERO;
    }

    public void setTotalCorrecao(BigDecimal totalCorrecao) {
        this.totalCorrecao = totalCorrecao;
    }

    public BigDecimal getTotalHonorarios() {
        return totalHonorarios;
    }

    public void setTotalHonorarios(BigDecimal totalHonorarios) {
        this.totalHonorarios = totalHonorarios;
    }

    public BigDecimal getTotalDebito() {
        return getTotalIss().add(getTotalJuros()).add(getTotalMulta()).add(getTotalCorrecao()).add(getTotalHonorarios());
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public Date getAbertura() {
        return abertura;
    }

    public void setAbertura(Date abertura) {
        this.abertura = abertura;
    }

    public Date getEncerramento() {
        return encerramento;
    }

    public void setEncerramento(Date encerramento) {
        this.encerramento = encerramento;
    }

    public TipoMovimentoMensalNfseDTO getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(TipoMovimentoMensalNfseDTO tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public String getUsuarioDeclaracao() {
        return usuarioDeclaracao;
    }

    public void setUsuarioDeclaracao(String usuarioDeclaracao) {
        this.usuarioDeclaracao = usuarioDeclaracao;
    }

    public List<NotaDeclaradaNfseDTO> getNotasDeclaradas() {
        return notasDeclaradas;
    }

    public void setNotasDeclaradas(List<NotaDeclaradaNfseDTO> notasDeclaradas) {
        this.notasDeclaradas = notasDeclaradas;
    }

    public LancadoPorNfseDTO getLancadoPor() {
        return lancadoPor;
    }

    public void setLancadoPor(LancadoPorNfseDTO lancadoPor) {
        this.lancadoPor = lancadoPor;
    }

    public List<DeclaracaoContaBancariaNfseDTO> getContasBancarias() {
        return contasBancarias;
    }

    public void setContasBancarias(List<DeclaracaoContaBancariaNfseDTO> contasBancarias) {
        this.contasBancarias = contasBancarias;
    }

    public boolean getAusenciaMovimento() {
        return (this.notas == null || this.notas.isEmpty()) &&
                (this.contasBancarias == null || this.contasBancarias.isEmpty());
    }

    public Date getReferencia() {
        return referencia;
    }

    public void setReferencia(Date referencia) {
        this.referencia = referencia;
    }

    public Long getIdExercicio() {
        return idExercicio;
    }

    public void setIdExercicio(Long idExercicio) {
        this.idExercicio = idExercicio;
    }

    public Long getIdCalculo() {
        return idCalculo;
    }

    public void setIdCalculo(Long idCalculo) {
        this.idCalculo = idCalculo;
    }

    public Date getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(Date dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public UserNfseDTO getUsuarioCancelamento() {
        return usuarioCancelamento;
    }

    public void setUsuarioCancelamento(UserNfseDTO usuarioCancelamento) {
        this.usuarioCancelamento = usuarioCancelamento;
    }

    public boolean isTodasNotasDoMes() {
        return todasNotasDoMes;
    }

    public void setTodasNotasDoMes(boolean todasNotasDoMes) {
        this.todasNotasDoMes = todasNotasDoMes;
    }

    public Long getIdArquivoDesif() {
        return idArquivoDesif;
    }

    public void setIdArquivoDesif(Long idArquivoDesif) {
        this.idArquivoDesif = idArquivoDesif;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, this.getId());
        ps.setInt(2, this.getCodigo());
        ps.setString(3, this.getTipo().name());
        ps.setLong(4, this.getPrestador().getId());
        ps.setString(5, Mes.getMesToInt(this.getMes()).name());
        ps.setLong(6, this.getIdExercicio());
        ps.setString(7, this.getSituacao().name());
        ps.setInt(8, this.getQtdNotas());
        ps.setBigDecimal(9, this.getTotalServicos());
        ps.setBigDecimal(10, this.getTotalIss());
        ps.setDate(11, DateUtils.toSQLDate(this.getAbertura()));
        ps.setDate(12, DateUtils.toSQLDate(this.getEncerramento()));
        ps.setString(13, this.getTipoMovimento().name());
        ps.setDate(14, DateUtils.toSQLDate(this.getReferencia()));
        ps.setString(15, this.getUsuarioDeclaracao());
        if (this.getIdArquivoDesif() != null) {
            ps.setLong(16, this.getIdArquivoDesif());
        } else {
            ps.setNull(16, Types.NULL);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public DeclaracaoMensalServicoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        DeclaracaoMensalServicoNfseDTO dto = new DeclaracaoMensalServicoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setPrestador(new PrestadorServicoNfseDTO());
        dto.getPrestador().setId(resultSet.getLong("PRESTADOR_ID"));
        dto.getPrestador().setInscricaoMunicipal(resultSet.getString("INSCRICAOCADASTRAL"));
        dto.getPrestador().setPessoa(new PessoaNfseDTO(null, new DadosPessoaisNfseDTO()));
        dto.getPrestador().getPessoa().getDadosPessoais().setNomeRazaoSocial(resultSet.getString("NOME"));
        dto.getPrestador().getPessoa().getDadosPessoais().setCpfCnpj(resultSet.getString("CPF_CNPJ"));
        dto.setCodigo(resultSet.getInt("CODIGO"));
        dto.setTipo(TipoDeclaracaoMensalServicoNfseDTO.valueOf(resultSet.getString("TIPO")));
        dto.setMes(Mes.valueOf(resultSet.getString("MES")).getNumeroMes());
        dto.setSituacao(SituacaoDeclaracaoMensalServicoNfseDTO.valueOf(resultSet.getString("SITUACAO")));
        dto.setQtdNotas(resultSet.getInt("QTDNOTAS"));
        dto.setTotalServicos(resultSet.getBigDecimal("TOTALSERVICOS"));
        dto.setTotalIss(resultSet.getBigDecimal("TOTALISS"));
        dto.setExercicio(resultSet.getInt("ANO"));
        dto.setAbertura(resultSet.getDate("ABERTURA"));
        dto.setEncerramento(resultSet.getDate("ENCERRAMENTO"));
        dto.setTipoMovimento(TipoMovimentoMensalNfseDTO.valueOf(resultSet.getString("TIPOMOVIMENTO")));
        dto.setVencimento(resultSet.getDate("VENCIMENTO"));
        dto.setIdCalculo(resultSet.getLong("CALCULO_ID"));
        dto.setDataCancelamento(resultSet.getDate("DATACANCELAMENTO"));
        if (resultSet.getLong("ID_USUARIOCANCELAMENTO") != 0) {
            dto.setUsuarioCancelamento(new UserNfseDTO());
            dto.getUsuarioCancelamento().setId(resultSet.getLong("ID_USUARIOCANCELAMENTO"));
            dto.getUsuarioCancelamento().setLogin(resultSet.getString("LOGIN_USUARIOCANCELAMENTO"));
        }
        if (resultSet.getLong("ARQUIVODESIF_ID") != 0) {
            dto.setIdArquivoDesif(resultSet.getLong("ARQUIVODESIF_ID"));
        }
        return dto;
    }
}
