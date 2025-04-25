package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.ArquivoParteNfseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ArquivoParteJDBCRepository extends AbstractJDBCRepository<ArquivoParteNfseDTO> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public String getSelect() {
        return " select id, arquivo_id, dados ";
    }

    @Override
    public String getFrom() {
        return " from arquivoparte obj ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.id ";
    }

    @Override
    public RowMapper<ArquivoParteNfseDTO> newRowMapper() {
        return new ArquivoParteNfseDTO();
    }

    @Override
    public ArquivoParteNfseDTO insert(ArquivoParteNfseDTO dto) {
        dto.setId(idJDBCRepository.getId());
        jdbcTemplate.batchUpdate(" insert into arquivoparte (id, arquivo_id, dados) " +
                " values (?, ?, ?) ", dto);
        return dto;
    }

    @Override
    public ArquivoParteNfseDTO update(ArquivoParteNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(ArquivoParteNfseDTO dto) {

    }
}
