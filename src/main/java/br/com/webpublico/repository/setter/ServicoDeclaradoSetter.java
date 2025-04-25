package br.com.webpublico.repository.setter;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.ServicoDeclaradoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServicoDeclaradoSetter implements BatchPreparedStatementSetter {

    private ServicoDeclaradoNfseDTO dto;

    public ServicoDeclaradoSetter(ServicoDeclaradoNfseDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        ps.setLong(2, dto.getCadastroEconomico().getId());
        ps.setDate(3, DateUtils.toSQLDate(dto.getEmissao()));
        ps.setLong(4, dto.getNumero());
        ps.setLong(5, dto.getDeclaracaoPrestacaoServico().getId());
        ps.setString(6, dto.getTipoServicoDeclarado().name());
        ps.setLong(7, dto.getTipoDocumentoServicoDeclarado().getId());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
