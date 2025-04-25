package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GraficoQuantitativoNotaNacionalDTO implements RowMapper<GraficoQuantitativoNotaNacionalDTO> {

    private String descricao;
    private Integer quantidade;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public GraficoQuantitativoNotaNacionalDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        GraficoQuantitativoNotaNacionalDTO dto = new GraficoQuantitativoNotaNacionalDTO();
        dto.setDescricao(resultSet.getString("descricao"));
        dto.setQuantidade(resultSet.getInt("quantidade"));
        return dto;
    }
}
