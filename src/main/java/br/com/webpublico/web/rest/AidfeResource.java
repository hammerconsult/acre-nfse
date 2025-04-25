package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.AidfeNfseDTO;
import br.com.webpublico.security.AuthoritiesConstants;
import br.com.webpublico.service.AidfeService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class AidfeResource {

    private final Logger log = LoggerFactory.getLogger(AidfeResource.class);

    @Inject
    private AidfeService aidfeService;

    @RequestMapping(value = "/aidfes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AidfeNfseDTO> getAidfe(@PathVariable Long id, @RequestHeader Long prestador) {
        return aidfeService.buscarAIDFePorIdAndPrestador(id, prestador);
    }

    @RequestMapping(value = "/aidfes/por-prestador",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AidfeNfseDTO>> getall(@RequestParam(value = "page", required = false) Integer offset,
                                                     @RequestParam(value = "per_page", required = false) Integer limit,
                                                     @RequestParam(value = "filtro", required = false) String filtro,
                                                     @RequestHeader Long prestador) throws URISyntaxException {
        return aidfeService.buscarAidfePorPrestadorServico(prestador, filtro, PaginationUtil.generatePageRequest(offset, limit));
    }

    @RequestMapping(value = "/aidfes/numero-inicial",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AidfeNfseDTO> getAidfeNumeroInicial(@RequestHeader Long prestador) {
        Long value = aidfeService.buscarNumeroInicialNFSE(prestador);
        AidfeNfseDTO aidfe = new AidfeNfseDTO();
        aidfe.setNumeroInicial(value);
        return new ResponseEntity<>(aidfe, HttpStatus.OK);
    }

    @RequestMapping(value = "/aidfes/aidfe-aguardando",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AidfeNfseDTO> getAidfeAguarandoPorPrestador(@RequestHeader Long prestador) {
        AidfeNfseDTO value = aidfeService.buscarAidfeWithStatusAguardando(prestador);
        if (value != null) {
            return new ResponseEntity<>(value, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/aidfes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.CONTRIBUINTE)
    public ResponseEntity<AidfeNfseDTO> createAidfe(@RequestBody @Valid AidfeNfseDTO aidfe) throws URISyntaxException {
        if (aidfe.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new user cannot already have an ID").body(null);
        }
        AidfeNfseDTO result = aidfeService.salvarAidfe(aidfe);
        return ResponseEntity.created(new URI("/api/aidfes/" + aidfe.getId())).body(result);
    }


}
