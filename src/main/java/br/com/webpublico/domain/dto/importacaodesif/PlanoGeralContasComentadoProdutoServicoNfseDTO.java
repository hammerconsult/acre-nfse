package br.com.webpublico.domain.dto.importacaodesif;

import br.com.webpublico.domain.dto.AbstractEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlanoGeralContasComentadoProdutoServicoNfseDTO extends AbstractEntity implements BatchPreparedStatementSetter {

    private ProdutoServicoBancarioNfseDTO produtoServico;
    private String descricaoComplementar;

    public ProdutoServicoBancarioNfseDTO getProdutoServico() {
        return produtoServico;
    }

    public void setProdutoServico(ProdutoServicoBancarioNfseDTO produtoServico) {
        this.produtoServico = produtoServico;
    }

    public String getDescricaoComplementar() {
        return descricaoComplementar;
    }

    public void setDescricaoComplementar(String descricaoComplementar) {
        this.descricaoComplementar = descricaoComplementar;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        if (!update) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, produtoServico.getId());
            preparedStatement.setString(3, descricaoComplementar);
        } else {
            preparedStatement.setLong(1, produtoServico.getId());
            preparedStatement.setString(2, descricaoComplementar);
            preparedStatement.setLong(3, id);
        }
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
