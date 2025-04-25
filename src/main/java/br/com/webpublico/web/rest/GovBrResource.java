package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.service.GovBrService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/externo")
public class GovBrResource {

    private final GovBrService govBrService;

    public GovBrResource(GovBrService govBrService) {
        this.govBrService = govBrService;
    }

    @RequestMapping(value = "/gov-br/autenticar/{code}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OAuth2AccessToken> autenticar(@PathVariable String code) {
        UserNfseDTO userNfseDTO = govBrService.getUsuarioViaGovBr(code);
        if (userNfseDTO != null) {
            return ResponseEntity.ok(govBrService.generateToken(userNfseDTO));
        }
        return ResponseEntity.ok(null);
    }

}
