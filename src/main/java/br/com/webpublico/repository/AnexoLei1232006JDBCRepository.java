package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.AnexoLei1232006FaixaNfseDTO;
import br.com.webpublico.domain.dto.AnexoLei1232006NfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnexoLei1232006JDBCRepository extends AbstractJDBCRepository<AnexoLei1232006NfseDTO> {
    @Override
    public String getSelect() {
        return " select obj.* ";
    }

    @Override
    public String getFrom() {
        return " from anexolei1232006 obj ";
    }

    @Override
    public RowMapper<AnexoLei1232006NfseDTO> newRowMapper() {
        return new AnexoLei1232006NfseDTO();
    }

    @Override
    public AnexoLei1232006NfseDTO insert(AnexoLei1232006NfseDTO dto) {
        return null;
    }

    @Override
    public AnexoLei1232006NfseDTO update(AnexoLei1232006NfseDTO dto) {
        return null;
    }

    @Override
    public void remove(AnexoLei1232006NfseDTO dto) {
    }

    public List<AnexoLei1232006NfseDTO> buscarAnexos() {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("exibir", Boolean.TRUE);
        return namedParameterJdbcTemplate.query(getSelect() + getFrom() +
                        " where obj.exibiralteracaoanexo = :exibir ", parameterSource,
                newRowMapper());
    }

    public List<AnexoLei1232006FaixaNfseDTO> buscarFaixasDoAnexo(AnexoLei1232006NfseDTO anexo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("anexoId", anexo.getId());
        return namedParameterJdbcTemplate.query("select * from anexolei1232006faixa faixa " +
                " where faixa.anexolei1232006_id = :anexoId ", parameterSource,
                new AnexoLei1232006FaixaNfseDTO());
    }
}
