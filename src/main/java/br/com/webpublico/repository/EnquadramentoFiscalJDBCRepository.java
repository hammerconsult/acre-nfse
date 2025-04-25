package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.EnquadramentoFiscalNfseDTO;
import br.com.webpublico.repository.mapper.EnquadramentoFiscalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class EnquadramentoFiscalJDBCRepository implements Serializable {


    @Autowired
    JdbcTemplate jdbcTemplate;

    public EnquadramentoFiscalNfseDTO findById(Long id) {
        List<EnquadramentoFiscalNfseDTO> query = jdbcTemplate.query(" SELECT EF.ID AS ID, " +
                " EF.PORTE AS PORTE, " +
                " EF.TIPOCONTRIBUINTE AS TIPOCONTRIBUINTE, " +
                " EC.CLASSIFICACAOATIVIDADE AS CLASSIFICACAOATIVIDADE, " +
                " EF.TIPONOTAFISCALSERVICO AS TIPONOTAFISCALSERVICO, " +
                " EF.SUBSTITUTOTRIBUTARIO AS SUBSTITUTOTRIBUTARIO, " +
                " EF.REGIMETRIBUTARIO AS  REGIMETRIBUTARIO, " +
                " EF.TIPOISSQN AS TIPOISSQN," +
                " EF.REGIMEESPECIALTRIBUTACAO AS REGIMEESPECIALTRIBUTACAO," +
                " EF.BANCO_ID," +
                " EF.VERSAODESIF," +
                " EF.ESCRITORIOCONTABIL_ID " +
                " FROM ENQUADRAMENTOFISCAL EF " +
                " INNER JOIN CADASTROECONOMICO EC ON EC.ID = EF.CADASTROECONOMICO_ID" +
                " LEFT JOIN ANEXOLEI1232006 AL ON AL.ID = EF.ANEXOLEI1232006_ID" +
                " LEFT JOIN ANEXOLEI1232006FAIXA ALF ON ALF.ID = EF.ANEXOLEI1232006FAIXA_ID" +
                " WHERE EF.ID = ? ", new Object[]{id}, new EnquadramentoFiscalMapper());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }

    public void update(EnquadramentoFiscalNfseDTO enquadramento) {
        if (enquadramento == null || enquadramento.getId() == null) {
            return;
        }
        jdbcTemplate.update(" UPDATE ENQUADRAMENTOFISCAL SET ESCRITORIOCONTABIL_ID = ? " +
                " WHERE ID = ? ", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                if (enquadramento.getEscritorioContabil() != null) {
                    preparedStatement.setLong(1, enquadramento.getEscritorioContabil().getId());
                } else {
                    preparedStatement.setNull(1, Types.NULL);
                }
                preparedStatement.setLong(2, enquadramento.getId());
            }
        });
    }
}
