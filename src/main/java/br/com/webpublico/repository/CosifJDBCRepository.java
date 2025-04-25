package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.CosifNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CosifJDBCRepository extends AbstractJDBCRepository<CosifNfseDTO> {

    @Override
    public String getSelect() {
        return " select" +
                " obj.id, " +
                " obj.conta, " +
                " obj.descricao, " +
                " obj.funcao ";
    }

    @Override
    public String getFrom() {
        return " from cosif obj ";
    }

    @Override
    public RowMapper<CosifNfseDTO> newRowMapper() {
        return new CosifNfseDTO();
    }

    @Override
    public CosifNfseDTO insert(CosifNfseDTO dto) {
        return dto;
    }

    @Override
    public CosifNfseDTO update(CosifNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(CosifNfseDTO dto) {

    }

    public List<CosifNfseDTO> findByGrupo(String grupo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("grupo", grupo.trim() + "%");
        return namedParameterJdbcTemplate.query(getSelect() + getFrom() +
                " where obj.conta like :grupo ", parameterSource, new CosifNfseDTO());
    }
}
