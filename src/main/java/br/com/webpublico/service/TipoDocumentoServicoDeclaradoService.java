package br.com.webpublico.service;

import br.com.webpublico.domain.dto.TipoDocumentoServicoDeclaradoNfseDTO;
import br.com.webpublico.repository.TipoDocumentoServicoDeclaradoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class TipoDocumentoServicoDeclaradoService implements Serializable {

    @Autowired
    private TipoDocumentoServicoDeclaradoJDBCRepository repository;

    public TipoDocumentoServicoDeclaradoNfseDTO findById(Long id) {
        return repository.findById(id);
    }

    public List<TipoDocumentoServicoDeclaradoNfseDTO> findAll() {
        return repository.findAll();
    }
}
