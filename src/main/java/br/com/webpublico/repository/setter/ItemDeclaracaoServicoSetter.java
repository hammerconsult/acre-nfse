package br.com.webpublico.repository.setter;

import br.com.webpublico.domain.dto.DeclaracaoPrestacaoServicoNfseDTO;
import br.com.webpublico.domain.dto.ItemDeclaracaoServicoNfseDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ItemDeclaracaoServicoSetter implements BatchPreparedStatementSetter {

    private DeclaracaoPrestacaoServicoNfseDTO declaracaoDto;
    private ItemDeclaracaoServicoNfseDTO dto;

    public ItemDeclaracaoServicoSetter(DeclaracaoPrestacaoServicoNfseDTO declaracaoDto,
                                       ItemDeclaracaoServicoNfseDTO dto) {
        this.declaracaoDto = declaracaoDto;
        this.dto = dto;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, dto.getId());
        ps.setBigDecimal(2, dto.getIss());
        ps.setBigDecimal(3, dto.getBaseCalculo());
        ps.setBigDecimal(4, dto.getDeducoes());
        ps.setBigDecimal(5, dto.getDescontosIncondicionados());
        ps.setBigDecimal(6, dto.getDescontosCondicionados());
        ps.setBigDecimal(7, dto.getQuantidade());
        ps.setBigDecimal(8, dto.getValorServico());
        ps.setString(9, dto.getDescricao());
        ps.setString(10, dto.getNomeServico());
        ps.setBigDecimal(11, dto.getAliquotaServico());
        ps.setBoolean(12, dto.getPrestadoNoPais());
        ps.setString(13, dto.getObservacoes());
        ps.setLong(14, dto.getServico().getId());
        if (dto.getMunicipio() != null && dto.getMunicipio().getId() != null) {
            ps.setLong(15, dto.getMunicipio().getId());
        } else {
            ps.setNull(15, Types.NULL);
        }
        ps.setLong(16, declaracaoDto.getId());
        if (dto.getPais() != null && dto.getPais().getId() != null) {
            ps.setLong(17, dto.getPais().getId());
        } else {
            ps.setNull(17, Types.NULL);
        }
        if (dto.getCnae() != null && dto.getCnae().getId() != null) {
            ps.setLong(18, dto.getCnae().getId());
        } else {
            ps.setNull(18, Types.NULL);
        }
        ps.setBoolean(19, dto.getDeducao());
        if (dto.getTipoDeducao() != null) {
            ps.setString(20, dto.getTipoDeducao().name());
        } else {
            ps.setNull(20, Types.NULL);
        }
        if (dto.getTipoOperacao() != null) {
            ps.setString(21, dto.getTipoOperacao().name());
        } else {
            ps.setNull(21, Types.NULL);
        }
        ps.setString(22, dto.getNumeroDocumentoFiscal());
        ps.setString(23, dto.getCpfCnpjDeducao());
        ps.setBigDecimal(24, dto.getValorDocumentoFiscal());
        if (dto.getConta() != null) {
            ps.setLong(25, dto.getConta().getId());
        } else {
            ps.setNull(25, Types.NULL);
        }
        ps.setBigDecimal(26, dto.getSaldoAnterior());
        ps.setBigDecimal(27, dto.getCredito());
        ps.setBigDecimal(28, dto.getDebito());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
