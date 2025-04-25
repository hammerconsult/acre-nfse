package br.com.webpublico.service;

import br.com.webpublico.domain.dto.importacaodesif.TipoInstituicaoFinanceiraNfseDTO;
import br.com.webpublico.repository.TipoInstituicaoFinanceiraJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional
@Service
public class TipoInstituicaoFinanceiraService implements Serializable {

    @Autowired
    TipoInstituicaoFinanceiraJDBCRepository repository;

    public TipoInstituicaoFinanceiraNfseDTO findByCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    public TipoInstituicaoFinanceiraNfseDTO findById(Long id) {
        return repository.findById(id);
    }
}
