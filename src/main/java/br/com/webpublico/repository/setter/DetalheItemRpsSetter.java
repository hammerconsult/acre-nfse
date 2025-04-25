package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetalheItemRpsSetter implements BatchPreparedStatementSetter {

    private ItemDeclaracaoServicoNfseDTO item;
    private DetalheItemDeclaracaoServicoNfseDTO detalhe;

    public DetalheItemRpsSetter(ItemDeclaracaoServicoNfseDTO item, DetalheItemDeclaracaoServicoNfseDTO detalhe) {
        this.item = item;
        this.detalhe = detalhe;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, detalhe.getId());
        ps.setLong(2, item.getId());
        ps.setBigDecimal(3, detalhe.getQuantidade());
        ps.setBigDecimal(4, detalhe.getValorServico());
        ps.setString(5, detalhe.getDescricao());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
