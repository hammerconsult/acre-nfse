package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.AbstractEntity;
import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.domain.dto.PrestadorServicoNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import com.beust.jcommander.internal.Lists;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ArquivoDesifNfseDTO extends AbstractEntity implements Serializable, RowMapper<ArquivoDesifNfseDTO>, BatchPreparedStatementSetter {

    private UserNfseDTO enviadoPor;
    private Date enviadoEm;
    private SituacaoArquivoDesifNfseDTO situacao;
    private PrestadorServicoNfseDTO prestador;
    private TipoInstituicaoFinanceiraNfseDTO tipoInstituicaoFinanceira;
    private MunicipioNfseDTO municipio;
    private Date inicioCompetencia;
    private Date fimCompetencia;
    private ModuloDesifNfseDTO modulo;
    private TipoDesifNfseDTO tipo;
    private String protocolo;
    private TipoConsolidacaoDesifNfseDTO tipoConsolidacao;
    private String cnpjResponsavel;
    private String versao;
    private TipoArredondamentoDesifNfseDTO tipoArredondamento;

    //Modulo 1
    private List<ArquivoDesifRegistro0400NfseDTO> registros0400;
    private List<ArquivoDesifRegistro0410NfseDTO> registros0410;

    //Modulo 2
    private List<ArquivoDesifRegistro0430NfseDTO> registros0430;
    private List<ArquivoDesifRegistro0440NfseDTO> registros0440;

    //Módulo 3
    private List<ArquivoDesifRegistro0100NfseDTO> registros0100;
    private List<ArquivoDesifRegistro0200NfseDTO> registros0200;
    private List<ArquivoDesifRegistro0300NfseDTO> registros0300;

    //Modulo 4
    private List<ArquivoDesifRegistro1000NfseDTO> registros1000;

    private Long idDeclaracaoMensalServico;

    public ArquivoDesifNfseDTO() {
        super();
        registros0100 = Lists.newArrayList();
        registros0200 = Lists.newArrayList();
        registros0300 = Lists.newArrayList();
        registros0400 = Lists.newArrayList();
        registros0410 = Lists.newArrayList();
        registros0430 = Lists.newArrayList();
        registros0440 = Lists.newArrayList();
        registros1000 = Lists.newArrayList();
    }

    public UserNfseDTO getEnviadoPor() {
        return enviadoPor;
    }

    public void setEnviadoPor(UserNfseDTO enviadoPor) {
        this.enviadoPor = enviadoPor;
    }

    public Date getEnviadoEm() {
        return enviadoEm;
    }

    public void setEnviadoEm(Date enviadoEm) {
        this.enviadoEm = enviadoEm;
    }

    public SituacaoArquivoDesifNfseDTO getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoArquivoDesifNfseDTO situacao) {
        this.situacao = situacao;
    }

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public TipoInstituicaoFinanceiraNfseDTO getTipoInstituicaoFinanceira() {
        return tipoInstituicaoFinanceira;
    }

    public void setTipoInstituicaoFinanceira(TipoInstituicaoFinanceiraNfseDTO tipoInstituicaoFinanceira) {
        this.tipoInstituicaoFinanceira = tipoInstituicaoFinanceira;
    }

    public MunicipioNfseDTO getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioNfseDTO municipio) {
        this.municipio = municipio;
    }

    public Date getInicioCompetencia() {
        return inicioCompetencia;
    }

    public void setInicioCompetencia(Date inicioCompetencia) {
        this.inicioCompetencia = inicioCompetencia;
    }

    public Date getFimCompetencia() {
        return fimCompetencia;
    }

    public void setFimCompetencia(Date fimCompetencia) {
        this.fimCompetencia = fimCompetencia;
    }

    public ModuloDesifNfseDTO getModulo() {
        return modulo;
    }

    public void setModulo(ModuloDesifNfseDTO modulo) {
        this.modulo = modulo;
    }

    public TipoDesifNfseDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoDesifNfseDTO tipo) {
        this.tipo = tipo;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public TipoConsolidacaoDesifNfseDTO getTipoConsolidacao() {
        return tipoConsolidacao;
    }

    public void setTipoConsolidacao(TipoConsolidacaoDesifNfseDTO tipoConsolidacao) {
        this.tipoConsolidacao = tipoConsolidacao;
    }

    public String getCnpjResponsavel() {
        return cnpjResponsavel;
    }

    public void setCnpjResponsavel(String cnpjResponsavel) {
        this.cnpjResponsavel = cnpjResponsavel;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public TipoArredondamentoDesifNfseDTO getTipoArredondamento() {
        return tipoArredondamento;
    }

    public void setTipoArredondamento(TipoArredondamentoDesifNfseDTO tipoArredondamento) {
        this.tipoArredondamento = tipoArredondamento;
    }

    public List<ArquivoDesifRegistro0100NfseDTO> getRegistros0100() {
        return registros0100;
    }

    public void setRegistros0100(List<ArquivoDesifRegistro0100NfseDTO> registros0100) {
        this.registros0100 = registros0100;
    }

    public List<ArquivoDesifRegistro0200NfseDTO> getRegistros0200() {
        return registros0200;
    }

    public void setRegistros0200(List<ArquivoDesifRegistro0200NfseDTO> registros0200) {
        this.registros0200 = registros0200;
    }

    public List<ArquivoDesifRegistro0300NfseDTO> getRegistros0300() {
        return registros0300;
    }

    public void setRegistros0300(List<ArquivoDesifRegistro0300NfseDTO> registros0300) {
        this.registros0300 = registros0300;
    }

    public List<ArquivoDesifRegistro0400NfseDTO> getRegistros0400() {
        return registros0400;
    }

    public void setRegistros0400(List<ArquivoDesifRegistro0400NfseDTO> registros0400) {
        this.registros0400 = registros0400;
    }

    public List<ArquivoDesifRegistro0410NfseDTO> getRegistros0410() {
        return registros0410;
    }

    public void setRegistros0410(List<ArquivoDesifRegistro0410NfseDTO> registros0410) {
        this.registros0410 = registros0410;
    }

    public List<ArquivoDesifRegistro0430NfseDTO> getRegistros0430() {
        return registros0430;
    }

    public void setRegistros0430(List<ArquivoDesifRegistro0430NfseDTO> registros0430) {
        this.registros0430 = registros0430;
    }

    public List<ArquivoDesifRegistro0440NfseDTO> getRegistros0440() {
        return registros0440;
    }

    public void setRegistros0440(List<ArquivoDesifRegistro0440NfseDTO> registros0440) {
        this.registros0440 = registros0440;
    }

    public List<ArquivoDesifRegistro1000NfseDTO> getRegistros1000() {
        return registros1000;
    }

    public void setRegistros1000(List<ArquivoDesifRegistro1000NfseDTO> registros1000) {
        this.registros1000 = registros1000;
    }

    public Long getIdDeclaracaoMensalServico() {
        return idDeclaracaoMensalServico;
    }

    public void setIdDeclaracaoMensalServico(Long idDeclaracaoMensalServico) {
        this.idDeclaracaoMensalServico = idDeclaracaoMensalServico;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        if (!update) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, enviadoPor.getId());
            preparedStatement.setDate(3, DateUtils.toSQLDate(enviadoEm));
            preparedStatement.setString(4, situacao.name());
            preparedStatement.setLong(5, prestador.getId());
            preparedStatement.setLong(6, tipoInstituicaoFinanceira.getId());
            preparedStatement.setLong(7, municipio.getId());
            preparedStatement.setDate(8, DateUtils.toSQLDate(inicioCompetencia));
            preparedStatement.setDate(9, DateUtils.toSQLDate(fimCompetencia));
            preparedStatement.setString(10, modulo.name());
            preparedStatement.setString(11, tipo.name());
            preparedStatement.setString(12, protocolo);
            preparedStatement.setString(13, tipoConsolidacao.name());
            preparedStatement.setString(14, cnpjResponsavel);
            preparedStatement.setString(15, versao);
            preparedStatement.setString(16, tipoArredondamento.name());
        } else {
            preparedStatement.setString(1, situacao.name());
            preparedStatement.setString(2, protocolo);
            preparedStatement.setLong(3, id);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifNfseDTO dto = new ArquivoDesifNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setEnviadoPor(new UserNfseDTO());
        dto.getEnviadoPor().setId(resultSet.getLong("ENVIADOPOR_ID"));
        dto.setEnviadoEm(resultSet.getDate("enviadoem"));
        dto.setSituacao(SituacaoArquivoDesifNfseDTO.valueOf(resultSet.getString("situacao")));
        dto.setPrestador(new PrestadorServicoNfseDTO());
        dto.getPrestador().setId(resultSet.getLong("cadastroeconomico_id"));
        dto.setTipoInstituicaoFinanceira(new TipoInstituicaoFinanceiraNfseDTO());
        dto.getTipoInstituicaoFinanceira().setId(resultSet.getLong("tipoinstituicaofinanceira_id"));
        dto.setMunicipio(new MunicipioNfseDTO());
        dto.getMunicipio().setId(resultSet.getLong("cidade_id"));
        dto.setInicioCompetencia(resultSet.getDate("iniciocompetencia"));
        dto.setFimCompetencia(resultSet.getDate("fimcompetencia"));
        dto.setModulo(ModuloDesifNfseDTO.valueOf(resultSet.getString("modulo")));
        dto.setTipo(TipoDesifNfseDTO.valueOf(resultSet.getString("tipo")));
        dto.setProtocolo(resultSet.getString("protocolo"));
        dto.setTipoConsolidacao(TipoConsolidacaoDesifNfseDTO.valueOf(resultSet.getString("tipoconsolidacao")));
        dto.setCnpjResponsavel(resultSet.getString("cnpjresponsavel"));
        dto.setVersao(resultSet.getString("versao"));
        dto.setTipoArredondamento(TipoArredondamentoDesifNfseDTO.valueOf(resultSet.getString("tipoarredondamento")));
        if (resultSet.getLong("iddeclaracaomensalservico") != 0) {
            dto.setIdDeclaracaoMensalServico(resultSet.getLong("iddeclaracaomensalservico"));
        }
        return dto;
    }

    public String getPrimeiros8DigitosCpfCnpj() {
        if (prestador == null) {
            return "";
        }
        return prestador.getPessoa().getDadosPessoais().getPrimeiros8DigitosCpfCnpj();
    }
}
