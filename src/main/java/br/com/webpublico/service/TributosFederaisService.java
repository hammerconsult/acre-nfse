package br.com.webpublico.service;

import br.com.webpublico.domain.dto.TributosFederaisNfseDTO;
import br.com.webpublico.repository.TributosFederaisJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class TributosFederaisService implements Serializable {

    @Autowired
    private TributosFederaisJDBCRepository tributosFederaisJDBCRepository;

    public TributosFederaisNfseDTO findById(Long id) {
        return tributosFederaisJDBCRepository.findById(id);
    }

    public TributosFederaisNfseDTO findByPrestadorId(Long prestadorId) {
        return tributosFederaisJDBCRepository.findByPrestadorId(prestadorId);
    }

    public void inserir(TributosFederaisNfseDTO tributosFederaisNfseDTO) {
        tributosFederaisJDBCRepository.inserir(tributosFederaisNfseDTO);
    }

    public void atualizar(TributosFederaisNfseDTO tributosFederaisNfseDTO) {
        tributosFederaisJDBCRepository.atualizar(tributosFederaisNfseDTO);
    }
}
