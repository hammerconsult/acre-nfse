package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.OpcaoPagamentoNfseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class OpcaoPagamentoJDBCRepository implements Serializable {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OpcaoPagamentoNfseDTO findByDivida(Long idDivida) {
        MapSqlParameterSource parametros = new MapSqlParameterSource();
        parametros.addValue("id_divida", idDivida);
        String sql = " select op.id, pfp.diavencimento " +
                "   from opcaopagamento op " +
                "  inner join opcaopagamentodivida opd on opd.opcaopagamento_id = op.id" +
                "  inner join parcelafixaperiodica pfp on pfp.id = (select max(s.id) from parcela s where s.opcaopagamento_id = op.id) " +
                " where opd.divida_id = :id_divida " +
                " and current_date between opd.iniciovigencia and coalesce(opd.finalvigencia, current_date) ";
        List<OpcaoPagamentoNfseDTO> query = namedParameterJdbcTemplate.query(sql, parametros,
                new OpcaoPagamentoNfseDTO());
        if (query != null && query.stream().findFirst().isPresent())
            return query.stream().findFirst().get();
        return null;
    }
}
