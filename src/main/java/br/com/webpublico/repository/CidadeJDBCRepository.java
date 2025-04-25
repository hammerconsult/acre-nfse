package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CidadeJDBCRepository extends AbstractJDBCRepository<MunicipioNfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id as id," +
                " obj.nome as nome, " +
                " obj.codigoibge as codigoibge, " +
                " uf.uf as estado ";
    }

    @Override
    public String getFrom() {
        return "   from cidade obj " +
                " inner join uf on uf.id = obj.uf_id ";
    }

    @Override
    public RowMapper<MunicipioNfseDTO> newRowMapper() {
        return new MunicipioNfseDTO();
    }

    @Override
    public MunicipioNfseDTO insert(MunicipioNfseDTO dto) {
        return dto;
    }

    @Override
    public MunicipioNfseDTO update(MunicipioNfseDTO dto) {
        return dto;
    }
    @Override
    public void remove(MunicipioNfseDTO dto) {
    }

    public MunicipioNfseDTO buscarPorNomeAndEstado(String nome, String estado) {
        if (StringUtils.isEmpty(nome) || StringUtils.isEmpty(estado)) {
            return null;
        }
        List<MunicipioNfseDTO> query = jdbcTemplate.query(getSelect() + getFrom() +
                        " where lower(obj.nome) like ? and lower(uf.uf) like ? ",
                new Object[]{"%" + nome.toLowerCase().trim() + "%", "%" + estado.toLowerCase().trim() + "%"},
                new MunicipioNfseDTO());
        if (query != null && !query.isEmpty()) {
            return query.stream().findFirst().get();
        }
        return null;
    }
}
