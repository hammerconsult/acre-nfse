package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.ServicoDeclaradoSearchNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicoDeclaracaoSearchMapper implements RowMapper<ServicoDeclaradoSearchNfseDTO> {

    @Override
    public ServicoDeclaradoSearchNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ServicoDeclaradoSearchNfseDTO item = new ServicoDeclaradoSearchNfseDTO();
        item.setId(resultSet.getLong("id"));
        item.setNumero(resultSet.getLong("numero"));
        item.setEmissao(resultSet.getDate("emissao"));
        item.setTipoServicoDeclarado(resultSet.getString("tipo_servico"));
        item.setTipoDocumentoCodigo(resultSet.getLong("tipo_documento_codigo"));
        item.setTipoDocumentoDescricao(resultSet.getString("tipo_documento_descricao"));
        item.setTomadorCpfCnpj(resultSet.getString("tomador_cpf_cnpj"));
        item.setTomadorNomeRazaoSocial(resultSet.getString("tomador_nome_razao"));
        item.setPrestadorCpfCnpj(resultSet.getString("prestador_cpf_cnpj"));
        item.setPrestadorNomeRazaoSocial(resultSet.getString("prestador_nome_razao"));
        item.setIssRetido(resultSet.getBoolean("iss_retido"));
        item.setTotalServicos(resultSet.getBigDecimal("total_servico"));
        item.setBaseCalculo(resultSet.getBigDecimal("base_calculo"));
        item.setIssCalculado(resultSet.getBigDecimal("iss_calculado"));
        item.setSituacao(resultSet.getString("situacao"));
        item.setNaturezaOperacao(resultSet.getString("naturezaoperacao"));
        return item;
    }

}
