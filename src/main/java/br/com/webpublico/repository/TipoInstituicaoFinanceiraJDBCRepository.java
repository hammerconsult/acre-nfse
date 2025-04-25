package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.TipoInstituicaoFinanceiraNfseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TipoInstituicaoFinanceiraJDBCRepository {

    private static final String SELECT = " select " +
            " ti.id," +
            " ti.codigo, " +
            " ti.descricao ";
    private static final String FROM = " from tipoinstituicaofinanceira ti ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TipoInstituicaoFinanceiraNfseDTO findByCodigo(String codigo) {
        String sql = SELECT + FROM +
                " where ti.codigo = ? ";
        return jdbcTemplate.queryForObject(sql, new Object[]{codigo},
                new TipoInstituicaoFinanceiraNfseDTO());
    }

    public TipoInstituicaoFinanceiraNfseDTO findById(Long id) {
        String sql = SELECT + FROM +
                " where ti.id = ? ";
        return jdbcTemplate.queryForObject(sql, new Object[]{id},
                new TipoInstituicaoFinanceiraNfseDTO());
    }
}
