package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import br.com.webpublico.domain.dto.RpsNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ItemRpsSetter implements BatchPreparedStatementSetter {

    private RpsNfseDTO rps;
    private ItemDeclaracaoServicoNfseDTO item;

    public ItemRpsSetter(RpsNfseDTO rps, ItemDeclaracaoServicoNfseDTO item) {
        this.rps = rps;
        this.item = item;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, item.getId());
        ps.setBigDecimal(2, item.getIss());
        ps.setBigDecimal(3, item.getBaseCalculo());
        ps.setBigDecimal(4, item.getDeducoes());
        ps.setBigDecimal(5, item.getDescontosIncondicionados());
        ps.setBigDecimal(6, item.getDescontosCondicionados());
        ps.setBigDecimal(7, item.getQuantidade());
        ps.setBigDecimal(8, item.getValorServico());
        ps.setString(9, item.getDescricao());
        ps.setString(10, item.getNomeServico());
        ps.setBigDecimal(11, item.getAliquotaServico());
        ps.setBoolean(12, item.getPrestadoNoPais());
        ps.setString(13, item.getObservacoes());
        ps.setLong(14, item.getServico().getId());
        if (item.getMunicipio() != null && item.getMunicipio().getId() != null) {
            ps.setLong(15, item.getMunicipio().getId());
        } else {
            ps.setNull(15, Types.NULL);
        }
        if (item.getPais() != null && item.getPais().getId() != null) {
            ps.setLong(16, item.getPais().getId());
        } else {
            ps.setNull(16, Types.NULL);
        }
        if (item.getCnae() != null && item.getCnae().getId() != null) {
            ps.setLong(17, item.getCnae().getId());
        } else {
            ps.setNull(17, Types.NULL);
        }
        ps.setLong(18, rps.getId());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
