package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.LegislacaoDTO;
import br.com.webpublico.service.LegislacaoService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wellington on 17/10/17.
 */
@RestController
@RequestMapping("/api/externo")
public class LegislacaoResource {

    @Autowired
    private LegislacaoService service;

    @GetMapping("/legislacoes-para-exibicao")
    @Timed
    public ResponseEntity<List<LegislacaoDTO>> buscarLegislacoesParaExibicao() {
        return new ResponseEntity<>(service.buscarLegislacoesParaExibicao(), HttpStatus.OK);
    }
}
