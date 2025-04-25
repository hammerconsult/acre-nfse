package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.TipoDependenciaDesifNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TipoDependenciaDesifJDBCRepository extends AbstractJDBCRepository<TipoDependenciaDesifNfseDTO> {
    @Override
    public String getSelect() {
        return " select obj.id, obj.codigo, obj.descricao ";
    }

    @Override
    public String getFrom() {
        return " from tipodependenciadesif obj ";
    }

    @Override
    public RowMapper<TipoDependenciaDesifNfseDTO> newRowMapper() {
        return new TipoDependenciaDesifNfseDTO();
    }

    @Override
    public TipoDependenciaDesifNfseDTO insert(TipoDependenciaDesifNfseDTO dto) {
        return dto;
    }

    @Override
    public TipoDependenciaDesifNfseDTO update(TipoDependenciaDesifNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(TipoDependenciaDesifNfseDTO dto) {

    }
}
