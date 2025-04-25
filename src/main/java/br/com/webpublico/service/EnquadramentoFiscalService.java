package br.com.webpublico.service;

import br.com.webpublico.domain.dto.EnquadramentoFiscalNfseDTO;
import br.com.webpublico.repository.EnquadramentoFiscalJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class EnquadramentoFiscalService implements Serializable {

    @Autowired
    private EnquadramentoFiscalJDBCRepository enquadramentoFiscalJDBCRepository;
    @Autowired
    private EscritorioContabilService escritorioContabilService;

    public EnquadramentoFiscalNfseDTO findById(Long id) {
        EnquadramentoFiscalNfseDTO enquadramento = enquadramentoFiscalJDBCRepository.findById(id);
        preencher(enquadramento);
        return enquadramento;
    }

    private void preencher(EnquadramentoFiscalNfseDTO enquadramento) {
        if (enquadramento.getEscritorioContabil() != null) {
            enquadramento.setEscritorioContabil(escritorioContabilService
                    .findById(enquadramento.getEscritorioContabil().getId()));
        }

    }

    public void update(EnquadramentoFiscalNfseDTO enquadramentoFiscal) {
        enquadramentoFiscalJDBCRepository.update(enquadramentoFiscal);
    }
}
