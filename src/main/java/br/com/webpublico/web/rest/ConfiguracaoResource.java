package br.com.webpublico.web.rest;

import br.com.webpublico.config.Constants;
import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.service.ConfiguracaoService;
import br.com.webpublico.web.rest.dto.PerfilAppDto;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * REST controller for managing Configuracao.
 */
@RestController
@RequestMapping("")
public class ConfiguracaoResource {

    private final Logger log = LoggerFactory.getLogger(ConfiguracaoResource.class);

    @Inject
    private ConfiguracaoService configuracaoService;

    @Inject
    private Environment env;


    /**
     * GET  /configuracaos/:id -> get the "id" configuracao.
     */
    @RequestMapping(value = "/publico/configuracao",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ConfiguracaoNfseDTO> get() {
        log.debug("REST request to get Configuracao : {}");
        ConfiguracaoNfseDTO retorno = configuracaoService.getConfiguracaoFromServer();
        return retorno == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(retorno, HttpStatus.OK);
    }


    @RequestMapping(value = "/publico/perfil-app",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PerfilAppDto> getPerfil() {
        log.debug("REST request to get Configuracao : {}");
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            return new ResponseEntity<>(new PerfilAppDto(Constants.SPRING_PROFILE_PRODUCTION), HttpStatus.OK);
        }
        return new ResponseEntity<>(new PerfilAppDto(Constants.SPRING_PROFILE_DEVELOPMENT), HttpStatus.OK);
    }


}
