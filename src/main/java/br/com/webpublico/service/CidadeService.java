package br.com.webpublico.service;

import br.com.webpublico.domain.dto.MunicipioNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.CidadeJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CidadeService extends AbstractService<MunicipioNfseDTO> {

    @Autowired
    private CidadeJDBCRepository repository;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    public MunicipioNfseDTO findByCodigoIBGE(String codigoIBGE) {
        return repository.findByAtributo("obj.codigoibge", codigoIBGE);
    }

    public MunicipioNfseDTO buscarPorNomeAndEstado(String nome, String estado) {
        return repository.buscarPorNomeAndEstado(nome, estado);
    }
}
