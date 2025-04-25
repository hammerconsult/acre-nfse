package br.com.webpublico.domain.dto;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.enums.SituacaoLoteRps;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LoteRpsNfseDTO implements RowMapper<LoteRpsNfseDTO> {

    private Long id;
    private PrestadorServicoNfseDTO prestador;
    private Integer numero;
    private UserNfseDTO userNfseDTO;
    private Date dataRecebimento;
    private String protocolo;
    private String log;
    private String xml;
    private boolean homologacao;
    private List<RpsNfseDTO> listaRps;
    private SituacaoLoteRps situacao;
    private Boolean reprocessar;
    private String versaoSistema;
    private String versaoAbrasf;

    public LoteRpsNfseDTO() {
        listaRps = Lists.newArrayList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrestadorServicoNfseDTO getPrestador() {
        return prestador;
    }

    public void setPrestador(PrestadorServicoNfseDTO prestador) {
        this.prestador = prestador;
    }

    public UserNfseDTO getUserNfseDTO() {
        return userNfseDTO;
    }

    public void setUserNfseDTO(UserNfseDTO userNfseDTO) {
        this.userNfseDTO = userNfseDTO;
    }

    public List<RpsNfseDTO> getListaRps() {
        return listaRps;
    }

    public void setListaRps(List<RpsNfseDTO> listaRps) {
        this.listaRps = listaRps;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Date dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public boolean isHomologacao() {
        return homologacao;
    }

    public void setHomologacao(boolean homologacao) {
        this.homologacao = homologacao;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public SituacaoLoteRps getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoLoteRps situacao) {
        this.situacao = situacao;
    }

    public Boolean getReprocessar() {
        return reprocessar == null ? Boolean.FALSE : reprocessar;
    }

    public void setReprocessar(Boolean reprocessar) {
        this.reprocessar = reprocessar;
    }

    public String getVersaoSistema() {
        return versaoSistema;
    }

    public void setVersaoSistema(String versaoSistema) {
        this.versaoSistema = versaoSistema;
    }

    public String getVersaoAbrasf() {
        return versaoAbrasf;
    }

    public void setVersaoAbrasf(String versaoAbrasf) {
        this.versaoAbrasf = versaoAbrasf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        br.com.webpublico.domain.dto.LoteRpsNfseDTO that = (br.com.webpublico.domain.dto.LoteRpsNfseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @JsonIgnore
    public void addLog(String s) {
        if (log == null) {
            log = "";
        }
        log += DateUtils.getDataHoraFormatada(new Date()) + " - " + s + "</br>";
    }

    @Override
    public LoteRpsNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        LoteRpsNfseDTO dto = new LoteRpsNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        if (!Strings.isNullOrEmpty(resultSet.getString("SITUACAO"))) {
            dto.setSituacao(SituacaoLoteRps.valueOf(resultSet.getString("SITUACAO")));
        }
        dto.setDataRecebimento(resultSet.getDate("DATARECEBIMENTO"));
        dto.setNumero(resultSet.getInt("NUMERO"));
        dto.setProtocolo(resultSet.getString("PROTOCOLO"));
        dto.setPrestador(new PrestadorServicoNfseDTO());
        dto.getPrestador().setId(resultSet.getLong("PRESTADOR_ID"));
        dto.setReprocessar(resultSet.getBoolean("REPROCESSAR"));
        dto.setVersaoSistema(resultSet.getString("VERSAOSISTEMA"));
        dto.setVersaoAbrasf(resultSet.getString("VERSAOABRASF"));
        dto.setHomologacao(resultSet.getBoolean("HOMOLOGACAO"));
        return dto;
    }
}
