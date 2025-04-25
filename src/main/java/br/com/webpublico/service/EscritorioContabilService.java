package br.com.webpublico.service;

import br.com.webpublico.domain.dto.EscritorioContabilNfseDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.EscritorioContabilJDBCRepository;
import br.com.webpublico.web.rest.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Transactional
public class EscritorioContabilService extends AbstractService<EscritorioContabilNfseDTO> {

    @Autowired
    private EscritorioContabilJDBCRepository repository;
    @Autowired
    private PessoaService pessoaService;

    @Override
    public AbstractJDBCRepository getRepository() {
        return repository;
    }

    @Override
    public void preencher(EscritorioContabilNfseDTO registro) {
        if (registro.getDadosPessoais() != null) {
            registro.setDadosPessoais(pessoaService.findById(registro.getDadosPessoais().getId()).getDadosPessoais());
        }
        if (registro.getResponsavel() != null) {
            registro.setResponsavel(pessoaService.findById(registro.getResponsavel().getId()).getDadosPessoais());
        }
    }
}
