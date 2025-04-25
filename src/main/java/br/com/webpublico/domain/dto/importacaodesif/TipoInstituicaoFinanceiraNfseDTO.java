package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.service.TipoInstituicaoFinanceiraService;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TipoInstituicaoFinanceiraNfseDTO implements Serializable, RowMapper<TipoInstituicaoFinanceiraNfseDTO> {

    private Long id;
    private String codigo;
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public TipoInstituicaoFinanceiraNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TipoInstituicaoFinanceiraNfseDTO dto = new TipoInstituicaoFinanceiraNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setCodigo(resultSet.getString("codigo"));
        dto.setDescricao(resultSet.getString("descricao"));
        return dto;
    }
}
