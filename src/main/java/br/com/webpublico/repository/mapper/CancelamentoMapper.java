package br.com.webpublico.repository.mapper;

import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.CancelamentoNfseDTO;
import br.com.webpublico.domain.dto.NotaFiscalNfseDTO;
import br.com.webpublico.domain.dto.enums.MotivoCancelamentoNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeferimentoNfseDTO;
import br.com.webpublico.util.Util;
import com.google.common.base.Strings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CancelamentoMapper implements RowMapper<CancelamentoNfseDTO> {


    @Override
    public CancelamentoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        CancelamentoNfseDTO dto = new CancelamentoNfseDTO();
        dto.setId(resultSet.getLong("ID"));
        dto.setDataCancelamento(resultSet.getDate("DATACANCELAMENTO"));
        dto.setSolicitante(resultSet.getString("SOLICITANTE"));
        if (!Strings.isNullOrEmpty(resultSet.getString("MOTIVOCANCELAMENTO")))
            dto.setMotivoCancelamento(MotivoCancelamentoNfseDTO.valueOf(resultSet.getString("MOTIVOCANCELAMENTO")));
        dto.setObservacoesCancelamento(resultSet.getString("OBSERVACOESCANCELAMENTO"));
        if (!Strings.isNullOrEmpty(resultSet.getString("SITUACAOFISCAL")))
            dto.setSituacaoFiscal(SituacaoDeferimentoNfseDTO.valueOf(resultSet.getString("SITUACAOFISCAL")));
        if (!Strings.isNullOrEmpty(resultSet.getString("SITUACAOTOMADOR")))
            dto.setSituacaoTomador(SituacaoDeferimentoNfseDTO.valueOf(resultSet.getString("SITUACAOTOMADOR")));
        if (!Strings.isNullOrEmpty(resultSet.getString("OBSERVACOESFISCAL")))
            dto.setObservacoesFiscal(resultSet.getString("OBSERVACOESFISCAL"));
        dto.setObservacoesFiscal(resultSet.getString("OBSERVACOESFISCAL"));
        if (Util.isColumnThere(resultSet, "nota_id")) {
            NotaFiscalNfseDTO nota = new NotaFiscalNfseDTO();
            nota.setId(resultSet.getLong("nota_id"));
            nota.setNumero(resultSet.getLong("nota_numero"));
            nota.setEmissao(resultSet.getDate("nota_emissao"));
            dto.setNotaFiscal(nota);
        }
        return dto;
    }


}
