package br.com.webpublico.service;

import br.com.webpublico.domain.dto.OpcaoPagamentoNfseDTO;
import br.com.webpublico.repository.OpcaoPagamentoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class OpcaoPagamentoService implements Serializable {

    @Autowired
    OpcaoPagamentoJDBCRepository repository;

    public OpcaoPagamentoNfseDTO findByDivida(Long idDivida) {
        return repository.findByDivida(idDivida);
    }
}
