package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.PerguntasRespostasDTO;
import br.com.webpublico.service.PerguntasRespostasService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by wellington on 17/10/17.
 */
@RestController
@RequestMapping("/api/externo")
public class PerguntasRespostasResource {

    private final Logger log = LoggerFactory.getLogger(PerguntasRespostasResource.class);

    @Inject
    private PerguntasRespostasService perguntasRespostasService;


    @GetMapping("/perguntas-repostas-para-exibicao")
    @Timed
    public ResponseEntity<List<PerguntasRespostasDTO>> buscarPerguntasRespostasParaExibicao() {
        return perguntasRespostasService.buscarPerguntasRespostasParaExibicao();
    }
}
