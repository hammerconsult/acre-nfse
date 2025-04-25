package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CosifNfseDTO extends AbstractEntity implements RowMapper<CosifNfseDTO>, BatchPreparedStatementSetter {

    private String conta;
    private String descricao;
    private String funcao;

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @Override
    public CosifNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        CosifNfseDTO dto = new CosifNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setConta(resultSet.getString("CONTA"));
        dto.setDescricao(resultSet.getString("DESCRICAO"));
        dto.setFuncao(resultSet.getString("FUNCAO"));
        return dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        if (!update) {
            ps.setLong(1, getId());
            ps.setString(2, getConta());
            ps.setString(3, getDescricao());
            ps.setString(4, getFuncao());
        } else {
            ps.setString(1, getConta());
            ps.setString(2, getDescricao());
            ps.setString(3, getFuncao());
            ps.setLong(4, getId());
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
