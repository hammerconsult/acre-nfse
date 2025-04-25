package br.com.webpublico.service;

import br.com.webpublico.domain.dto.importacaodesif.TarifaBancariaNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.TarifaBancariaJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TarifaBancariaService extends AbstractService<TarifaBancariaNfseDTO> {

    @Autowired
    TarifaBancariaJDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public TarifaBancariaNfseDTO findByCodigo(String codigo) {
        return repository.findByAtributo(" obj.codigo ", codigo);
    }
}
