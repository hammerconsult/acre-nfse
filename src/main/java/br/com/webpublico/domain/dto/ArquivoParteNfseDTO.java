package br.com.webpublico.domain.dto;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArquivoParteNfseDTO extends AbstractEntity implements RowMapper<ArquivoParteNfseDTO>, BatchPreparedStatementSetter {

    private Long idArquivo;
    private byte[] dados;

    public Long getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Long idArquivo) {
        this.idArquivo = idArquivo;
    }

    public byte[] getDados() {
        return dados;
    }

    public void setDados(byte[] dados) {
        this.dados = dados;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, idArquivo);
        preparedStatement.setBytes(3, dados);
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoParteNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoParteNfseDTO dto = new ArquivoParteNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setIdArquivo(resultSet.getLong("arquivo_id"));
        dto.setDados(resultSet.getBytes("dados"));
        return dto;
    }
}
