package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArquivoDesifRegistro0300NfseDTO extends AbstractEntity implements RowMapper<ArquivoDesifRegistro0300NfseDTO>, BatchPreparedStatementSetter {

    private Long linha;
    private Long idArquivo;
    private ProdutoServicoBancarioNfseDTO produtoServicoBancario;
    private String descricaoComplementar;
    private String conta;
    private String desdobramento;

    public Long getLinha() {
        return linha;
    }

    public void setLinha(Long linha) {
        this.linha = linha;
    }

    public Long getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Long idArquivo) {
        this.idArquivo = idArquivo;
    }

    public ProdutoServicoBancarioNfseDTO getProdutoServicoBancario() {
        return produtoServicoBancario;
    }

    public void setProdutoServicoBancario(ProdutoServicoBancarioNfseDTO produtoServicoBancario) {
        this.produtoServicoBancario = produtoServicoBancario;
    }

    public String getDescricaoComplementar() {
        return descricaoComplementar;
    }

    public void setDescricaoComplementar(String descricaoComplementar) {
        this.descricaoComplementar = descricaoComplementar;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getDesdobramento() {
        return desdobramento;
    }

    public void setDesdobramento(String desdobramento) {
        this.desdobramento = desdobramento;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setLong(2, linha);
        preparedStatement.setLong(3, idArquivo);
        preparedStatement.setLong(4, produtoServicoBancario.getId());
        preparedStatement.setString(5, descricaoComplementar);
        preparedStatement.setString(6, conta);
        preparedStatement.setString(7, desdobramento);
    }

    @Override
    public int getBatchSize() {
        return 1;
    }

    @Override
    public ArquivoDesifRegistro0300NfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoDesifRegistro0300NfseDTO dto = new ArquivoDesifRegistro0300NfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setIdArquivo(resultSet.getLong("arquivodesif_id"));
        dto.setProdutoServicoBancario(new ProdutoServicoBancarioNfseDTO());
        dto.getProdutoServicoBancario().setId(resultSet.getLong("produtoservicobancario_id"));
        dto.setDescricaoComplementar(resultSet.getString("descricaocomplementar"));
        dto.setConta(resultSet.getString("conta"));
        dto.setDesdobramento(resultSet.getString("desdobramento"));
        return dto;
    }
}
