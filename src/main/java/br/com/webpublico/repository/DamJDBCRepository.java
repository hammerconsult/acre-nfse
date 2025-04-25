package br.com.webpublico.repository;

import br.com.webpublico.tributario.consultadebitos.dtos.DamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DamJDBCRepository implements Serializable {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DamDTO buscarUltimoDamParcela(Long idParcela) {
        String sql = " SELECT D.ID AS ID, " +
                "       D.NUMERODAM AS NUMERODAM " +
                "   FROM DAM D " +
                "  INNER JOIN EXERCICIO E ON E.ID = D.EXERCICIO_ID " +
                "WHERE D.ID = (SELECT MAX(ID.DAM_ID) " +
                "                 FROM ITEMDAM ID " +
                "              WHERE ID.PARCELA_ID = :ID_PARCELA) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ID_PARCELA", idParcela);
        List<DamDTO> query = namedParameterJdbcTemplate.query(sql, parameters, (resultSet, i) -> {
            DamDTO damDTO = new DamDTO();
            damDTO.setId(resultSet.getLong("ID"));
            damDTO.setNumeroDAM(resultSet.getString("NUMERODAM"));
            return damDTO;
        });
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }
}
