package br.com.webpublico.repository.mapper;

import br.com.webpublico.domain.dto.LivroFiscalCompetenciaNfseDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LivroFiscalCompetenciaMapper implements RowMapper<LivroFiscalCompetenciaNfseDTO> {


    @Override
    public LivroFiscalCompetenciaNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        LivroFiscalCompetenciaNfseDTO dto = new LivroFiscalCompetenciaNfseDTO();
        dto.setExercicio(resultSet.getInt("EXERCICIO"));
        dto.setMes(resultSet.getInt("MES"));
        dto.setQuantidade(resultSet.getInt("QUANTIDADE"));
        dto.setIssqnPago(resultSet.getBigDecimal("ISSQN_PAGO"));
        dto.setIssqnProprio(resultSet.getBigDecimal("ISSQN_PROPRIO"));
        dto.setIssqnRetido(resultSet.getBigDecimal("ISSQN_RETIDO"));
        dto.setValorServico(resultSet.getBigDecimal("VALOR_SERVICO"));
        dto.setQuantidadeNaoDeclarado(resultSet.getInt("QUANTIDADE_NAO_DECLARADA"));
        dto.setIssqnPagoNaoDeclarado(resultSet.getBigDecimal("ISSQN_PAGO_NAO_DECLARADA"));
        dto.setIssqnProprioNaoDeclarado(resultSet.getBigDecimal("ISSQN_PROPRIO_NAO_DECLARADA"));
        dto.setIssqnRetidoNaoDeclarado(resultSet.getBigDecimal("ISSQN_RETIDO_NAO_DECLARADA"));
        dto.setValorServicoNaoDeclarado(resultSet.getBigDecimal("VALOR_SERVICO_NAO_DECLARADA"));
        return dto;
    }


}
