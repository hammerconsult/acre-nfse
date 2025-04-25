package br.com.webpublico.service;

import br.com.webpublico.domain.dto.LegislacaoDTO;
import br.com.webpublico.repository.AbstractJDBCRepository;
import br.com.webpublico.repository.LegislacaoJDBCRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Service
public class LegislacaoService extends AbstractService<LegislacaoDTO> {

    @Autowired
    private LegislacaoJDBCRepository repository;

    @Override
    public LegislacaoJDBCRepository getRepository() {
        return repository;
    }

    public List<LegislacaoDTO> buscarLegislacoesParaExibicao() {
        return repository.buscarLegilacoesParaExibicao();
    }
}
