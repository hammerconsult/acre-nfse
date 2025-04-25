package br.com.webpublico.service;

import br.com.webpublico.domain.dto.importacaodesif.EventoContabilDesifNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.EventoContabilDesifJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EventoContabilDesifService extends AbstractService<EventoContabilDesifNfseDTO> {

    @Autowired
    private EventoContabilDesifJDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(EventoContabilDesifNfseDTO registro) {
    }

    public EventoContabilDesifNfseDTO findByCodigo(Integer codigo) {
        return repository.findByAtributo("obj.codigo", codigo);
    }
}
