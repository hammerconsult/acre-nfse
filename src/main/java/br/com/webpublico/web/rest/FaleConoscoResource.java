package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.FaleConoscoNfseDTO;
import br.com.webpublico.service.FaleConoscoService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api/externo")
public class FaleConoscoResource {

    @Inject
    FaleConoscoService faleConoscoService;


    @RequestMapping(value = "/fale-conosco",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FaleConoscoNfseDTO> createFaleconosco(@RequestBody FaleConoscoNfseDTO dto) throws URISyntaxException {
        FaleConoscoNfseDTO result = faleConoscoService.inserir(dto);
        return ResponseEntity.created(new URI("/api/fale-conosco/")).body(result);
    }


}
