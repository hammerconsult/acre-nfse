package br.com.webpublico.service;

import br.com.webpublico.domain.dto.importacaodesif.ProdutoServicoBancarioNfseDTO;
import br.com.webpublico.repository.ProdutoServicoBancarioJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoServicoBancarioService extends AbstractService<ProdutoServicoBancarioNfseDTO> {

    @Autowired
    ProdutoServicoBancarioJDBCRepository repository;

    @Override
    public ProdutoServicoBancarioJDBCRepository getRepository() {
        return repository;
    }

    public ProdutoServicoBancarioNfseDTO findByCodigo(String codigo) {
        return repository.findByAtributo(" obj.codigo ", codigo);
    }
}
