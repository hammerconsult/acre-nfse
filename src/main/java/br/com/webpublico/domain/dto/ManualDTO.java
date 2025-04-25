package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by william on 05/09/17.
 */
public class ManualDTO extends AbstractEntity implements RowMapper<ManualDTO> {

    private TipoManualDTO tipoManualDTO;
    private String nome;
    private String resumo;
    private String link;
    private Integer ordem;
    private Boolean habilitarExibicao;
    private ArquivoNfseDTO arquivo;

    public TipoManualDTO getTipoManualDTO() {
        return tipoManualDTO;
    }

    public void setTipoManualDTO(TipoManualDTO tipoManualDTO) {
        this.tipoManualDTO = tipoManualDTO;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Boolean getHabilitarExibicao() {
        return habilitarExibicao;
    }

    public void setHabilitarExibicao(Boolean habilitarExibicao) {
        this.habilitarExibicao = habilitarExibicao;
    }

    public ArquivoNfseDTO getArquivo() {
        return arquivo;
    }

    public void setArquivo(ArquivoNfseDTO arquivo) {
        this.arquivo = arquivo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public ManualDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ManualDTO dto = new ManualDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setTipoManualDTO(new TipoManualDTO());
        dto.getTipoManualDTO().setId(resultSet.getLong("tipomanual_id"));
        dto.getTipoManualDTO().setDescricao(resultSet.getString("tipomanual_descricao"));
        dto.getTipoManualDTO().setOrdem(resultSet.getInt("tipomanual_ordem"));
        dto.setNome(resultSet.getString("nome"));
        dto.setResumo(resultSet.getString("resumo"));
        dto.setLink(resultSet.getString("link"));
        dto.setOrdem(resultSet.getInt("ordem"));
        dto.setHabilitarExibicao(resultSet.getBoolean("habilitarexibicao"));
        if (resultSet.getLong("arquivo_id") != 0) {
            dto.setArquivo(new ArquivoNfseDTO());
            dto.getArquivo().setId(resultSet.getLong("arquivo_id"));
            dto.getArquivo().setNome(resultSet.getString("arquivo_nome"));
        }
        return dto;
    }
}
