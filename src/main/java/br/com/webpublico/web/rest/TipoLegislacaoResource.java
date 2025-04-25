package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.TipoLegislacaoLegislacaoDTO;
import br.com.webpublico.service.TipoLegislacaoService;
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
@RequestMapping("/api")
public class TipoLegislacaoResource {

    private final Logger log = LoggerFactory.getLogger(TipoLegislacaoResource.class);

    @Inject
    private TipoLegislacaoService tipoLegislacaoService;


    @GetMapping("/tipo-legislacao-com-legislacoes")
    @Timed
    public ResponseEntity<List<TipoLegislacaoLegislacaoDTO>> buscarTiposLegislacaoComLegislacoes() {
        return tipoLegislacaoService.buscarTiposLegislacaoComLegislacoes();
    }
}
