package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ClassificacaoAtividadeNfseDTO;
import br.com.webpublico.service.ClassificacaoAtividadeService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ClassificacaoAtividadeResource {

    private final Logger log = LoggerFactory.getLogger(ClassificacaoAtividadeResource.class);

    @Inject
    private ClassificacaoAtividadeService classificacaoAtividadeService;


    @RequestMapping(value = "/classificacoes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClassificacaoAtividadeNfseDTO>> getall(){
        return classificacaoAtividadeService.buscarClassificacoes();
    }

}
