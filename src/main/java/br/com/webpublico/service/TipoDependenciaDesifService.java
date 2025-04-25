package br.com.webpublico.service;

import br.com.webpublico.domain.dto.importacaodesif.TipoDependenciaDesifNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.TipoDependenciaDesifJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TipoDependenciaDesifService extends AbstractService<TipoDependenciaDesifNfseDTO> {

    @Autowired
    TipoDependenciaDesifJDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public TipoDependenciaDesifNfseDTO findByCodigo(Integer codigo) {
        return repository.findByAtributo("obj.codigo", codigo);
    }
}
