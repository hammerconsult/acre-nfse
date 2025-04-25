package br.com.webpublico.domain.dto;

import br.com.webpublico.domain.dto.enums.TipoMensagemDfeAdnDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MensagemDfeAdnDTO extends AbstractEntity implements RowMapper<MensagemDfeAdnDTO> {

    private Long id;
    private TipoMensagemDfeAdnDTO tipoMensagem;
    private String codigo;
    private String descricao;
    private String complemento;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public TipoMensagemDfeAdnDTO getTipoMensagem() {
        return tipoMensagem;
    }

    public void setTipoMensagem(TipoMensagemDfeAdnDTO tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public MensagemDfeAdnDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        MensagemDfeAdnDTO dto = new MensagemDfeAdnDTO();
        dto.setTipoMensagem(TipoMensagemDfeAdnDTO.valueOf(resultSet.getString("tipomensagem")));
        dto.setCodigo(resultSet.getString("codigo"));
        dto.setDescricao(resultSet.getString("descricao"));
        dto.setComplemento(resultSet.getString("complemento"));
        return dto;
    }
}
