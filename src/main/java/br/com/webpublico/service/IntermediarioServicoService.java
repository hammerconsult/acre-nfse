package br.com.webpublico.service;

import br.com.webpublico.domain.dto.IntermediarioServicoNfseDTO;
import br.com.webpublico.repository.IntermediarioServicoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class IntermediarioServicoService implements Serializable {

    @Autowired
    private IntermediarioServicoJDBCRepository intermediarioServicoJDBCRepository;

    public IntermediarioServicoNfseDTO findById(Long id) {
        return intermediarioServicoJDBCRepository.findById(id);
    }

    public void inserirIntermediarioServico(IntermediarioServicoNfseDTO intermediario) {
        intermediarioServicoJDBCRepository.inserirIntermediarioServico(intermediario);
    }
}
