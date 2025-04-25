package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ManualDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.ManualJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManualService extends AbstractService<ManualDTO> {

    @Autowired
    private ManualJDBCRepository repository;
    @Autowired
    private ArquivoService arquivoService;

    @Override
    public AbstractJDBCRepository<ManualDTO> getRepository() {
        return repository;
    }

    public List<ManualDTO> buscarManuaisParaExibicao() {
        return repository.buscarManuaisParaExibicao();
    }

    public List<ManualDTO> buscarManuaisPorTag(String tag) {
        return repository.buscarManuaisPorTag(tag);
    }

    public List<ManualDTO> buscarManuaisPorTipo(Long tipo) {
        return repository.buscarManuaisPorTipo(tipo);

    }
}
