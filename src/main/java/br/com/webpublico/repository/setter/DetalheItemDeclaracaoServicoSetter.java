package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.DetalheItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetalheItemDeclaracaoServicoSetter implements BatchPreparedStatementSetter {

    private DetalheItemDeclaracaoServicoNfseDTO dto;
    private ItemDeclaracaoServicoNfseDTO itemDto;

    public DetalheItemDeclaracaoServicoSetter(ItemDeclaracaoServicoNfseDTO itemDto, DetalheItemDeclaracaoServicoNfseDTO dto) {
        this.itemDto = itemDto;
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        ps.setBigDecimal(2, dto.getQuantidade());
        ps.setBigDecimal(3, dto.getValorServico());
        ps.setString(4, dto.getDescricao());
        ps.setLong(5, itemDto.getId());

    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
