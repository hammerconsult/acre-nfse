package br.com.webpublico.service;

import br.com.webpublico.domain.dto.importacaodesif.CodigoTributacaoNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.CodigoTributacaoJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CodigoTributacaoService extends AbstractService<CodigoTributacaoNfseDTO> {

    @Autowired
    CodigoTributacaoJDBCRepository repository;
    @Autowired
    ServicoService servicoService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(CodigoTributacaoNfseDTO codigoTributacao) {
        if (codigoTributacao != null) {
            if (codigoTributacao.getServico() != null) {
                codigoTributacao.setServico(servicoService.findById(codigoTributacao.getServico().getId()));
            }
        }
    }

    public CodigoTributacaoNfseDTO findByCodigo(String codigo) {
        CodigoTributacaoNfseDTO codigoTributacao = repository.findByAtributo("obj.codigo", codigo);
        preencher(codigoTributacao);
        return codigoTributacao;
    }
}
