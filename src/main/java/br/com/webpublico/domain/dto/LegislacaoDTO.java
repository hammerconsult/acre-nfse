package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.ArquivoDTO;
import br.com.webpublico.domain.dto.NfseDTO;
import br.com.webpublico.domain.dto.TipoLegislacaoDTO;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by wellington on 04/09/17.
 */
public class LegislacaoDTO extends AbstractEntity implements RowMapper<LegislacaoDTO> {

    private TipoLegislacaoDTO tipoLegislacaoDTO;
    private String nome;
    private String sumula;
    private Date dataPublicacao;
    private ArquivoDTO arquivoDTO;
    private Integer ordemExibicao;
    private Boolean habilitarExibicao;

    public TipoLegislacaoDTO getTipoLegislacaoDTO() {
        return tipoLegislacaoDTO;
    }

    public void setTipoLegislacaoDTO(TipoLegislacaoDTO tipoLegislacaoDTO) {
        this.tipoLegislacaoDTO = tipoLegislacaoDTO;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSumula() {
        return sumula;
    }

    public void setSumula(String sumula) {
        this.sumula = sumula;
    }

    public Date getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(Date dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public ArquivoDTO getArquivoDTO() {
        return arquivoDTO;
    }

    public void setArquivoDTO(ArquivoDTO arquivoDTO) {
        this.arquivoDTO = arquivoDTO;
    }

    public Integer getOrdemExibicao() {
        return ordemExibicao;
    }

    public void setOrdemExibicao(Integer ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }

    public Boolean getHabilitarExibicao() {
        return habilitarExibicao;
    }

    public void setHabilitarExibicao(Boolean habilitarExibicao) {
        this.habilitarExibicao = habilitarExibicao;
    }

    @Override
    public LegislacaoDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        LegislacaoDTO dto = new LegislacaoDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setTipoLegislacaoDTO(new TipoLegislacaoDTO());
        dto.getTipoLegislacaoDTO().setId(resultSet.getLong("tipo_id"));
        dto.getTipoLegislacaoDTO().setDescricao(resultSet.getString("tipo_descricao"));
        dto.getTipoLegislacaoDTO().setHabilitarExibicao(resultSet.getBoolean("tipo_habilitarexibicao"));
        dto.getTipoLegislacaoDTO().setOrdemExibicao(resultSet.getInt("tipo_ordemexibicao"));
        dto.setDataPublicacao(resultSet.getDate("datapublicacao"));
        dto.setNome(resultSet.getString("nome"));
        dto.setSumula(resultSet.getString("sumula"));
        dto.setHabilitarExibicao(resultSet.getBoolean("habilitarexibicao"));
        dto.setOrdemExibicao(resultSet.getInt("ordemexibicao"));
        dto.setArquivoDTO(new ArquivoDTO());
        dto.getArquivoDTO().setId(resultSet.getLong("arquivo_id"));
        dto.getArquivoDTO().setNome(resultSet.getString("arquivo_nome"));
        return dto;
    }
}
