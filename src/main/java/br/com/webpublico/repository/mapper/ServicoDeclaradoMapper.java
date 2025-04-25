package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.ExigibilidadeNfseDTO;
import br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO;
import br.com.webpublico.domain.dto.enums.TipoServicoDeclaradoNfseDTO;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicoDeclaradoMapper implements RowMapper<ServicoDeclaradoNfseDTO> {
    @Override
    public ServicoDeclaradoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ServicoDeclaradoNfseDTO dto = new ServicoDeclaradoNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setCadastroEconomico(new PrestadorServicoNfseDTO());
        dto.getCadastroEconomico().setId(resultSet.getLong("cadastroeconomico_id"));
        dto.setEmissao(resultSet.getDate("emissao"));
        dto.setNumero(resultSet.getLong("numero"));
        dto.setDeclaracaoPrestacaoServico(new DeclaracaoPrestacaoServicoNfseDTO());
        dto.getDeclaracaoPrestacaoServico().setId(resultSet.getLong("declaracaoprestacaoservico_id"));
        if (!Strings.isNullOrEmpty(resultSet.getString("tiposervicodeclarado")))
            dto.setTipoServicoDeclarado(TipoServicoDeclaradoNfseDTO.valueOf(resultSet.getString("tiposervicodeclarado")));
        dto.setTipoDocumentoServicoDeclarado(new TipoDocumentoServicoDeclaradoNfseDTO());
        dto.getTipoDocumentoServicoDeclarado().setId(resultSet.getLong("tipodocservicodeclarado_id"));
        return dto;
    }
}
