package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ConfiguracaoGovBrDTO;
import br.com.webpublico.service.ConfiguracaoGovBrService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/externo")
public class ConfiguracaoGovBrResource {

    private final ConfiguracaoGovBrService service;

    @Autowired
    public ConfiguracaoGovBrResource(ConfiguracaoGovBrService service) {
        this.service = service;
    }

    @GetMapping("/configuracao-gov-br")
    @Timed
    public ResponseEntity<ConfiguracaoGovBrDTO> getConfiguracaoGovBr() {
        return new ResponseEntity<>(service.getConfiguracaoGovBr(), HttpStatus.OK);
    }

}
