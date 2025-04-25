package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.NoticiaNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticiaJDBCRepository extends AbstractJDBCRepository<NoticiaNfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                "   obj.id, " +
                "   obj.datanoticia, " +
                "   obj.titulo, " +
                "   obj.conteudo, " +
                "   obj.detentorarquivocomposicao_id ";
    }

    @Override
    public String getFrom() {
        return " from noticianfse obj ";
    }

    @Override
    public RowMapper<NoticiaNfseDTO> newRowMapper() {
        return new NoticiaNfseDTO();
    }

    @Override
    public NoticiaNfseDTO insert(NoticiaNfseDTO dto) {
        return dto;
    }

    @Override
    public NoticiaNfseDTO update(NoticiaNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(NoticiaNfseDTO dto) {
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.datanoticia desc ";
    }

    public List<NoticiaNfseDTO> buscarUltimasNoticias() {
        return jdbcTemplate.query(getSelect() + getFrom() +
                getOrderByDefault() +
                " fetch first 4 rows only  ", new NoticiaNfseDTO());
    }
}
