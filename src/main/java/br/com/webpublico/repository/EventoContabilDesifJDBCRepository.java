package br.com.webpublico.repository;

import br.com.webpublico.domain.dto.importacaodesif.EventoContabilDesifNfseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EventoContabilDesifJDBCRepository extends AbstractJDBCRepository<EventoContabilDesifNfseDTO> {

    @Override
    public String getSelect() {
        return " select " +
                " obj.id, " +
                " obj.codigo, " +
                " obj.descricao, " +
                " obj.credito, " +
                " obj.debito ";
    }

    @Override
    public String getFrom() {
        return " from eventocontabildesif obj ";
    }

    @Override
    public String getOrderByDefault() {
        return " order by obj.id desc ";
    }

    @Override
    public RowMapper<EventoContabilDesifNfseDTO> newRowMapper() {
        return new EventoContabilDesifNfseDTO();
    }

    @Override
    public EventoContabilDesifNfseDTO insert(EventoContabilDesifNfseDTO dto) {
        return dto;
    }

    @Override
    public EventoContabilDesifNfseDTO update(EventoContabilDesifNfseDTO dto) {
        return dto;
    }

    @Override
    public void remove(EventoContabilDesifNfseDTO dto) {
        
    }

}
