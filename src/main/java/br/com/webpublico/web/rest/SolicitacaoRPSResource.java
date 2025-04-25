package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.SolicitacaoRPSNfseDTO;
import br.com.webpublico.security.AuthoritiesConstants;
import br.com.webpublico.service.SolicitacaoRPSService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SolicitacaoRPSResource {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoRPSResource.class);

    @Inject
    private SolicitacaoRPSService solicitacaoRPSService;

    @RequestMapping(value = "/solicitacao-rps/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SolicitacaoRPSNfseDTO> getSolicitacaoRPS(@PathVariable Long id, @RequestHeader Long prestador) {
        return solicitacaoRPSService.buscarSolicitacaoRPSPorIdAndPrestador(id, prestador);
    }

    @RequestMapping(value = "/solicitacao-rps/por-prestador",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SolicitacaoRPSNfseDTO>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                              @RequestParam(value = "per_page", required = false) Integer limit,
                                                              @RequestParam(value = "filtro", required = false) String filtro,
                                                              @RequestHeader Long prestador) throws URISyntaxException {
        return solicitacaoRPSService.buscarSolicitacaoRPSPorPrestadorServico(prestador, filtro, PaginationUtil.generatePageRequest(offset, limit));
    }

    @RequestMapping(value = "/solicitacao-rps",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.CONTRIBUINTE)
    public ResponseEntity<SolicitacaoRPSNfseDTO> createSolicitacaoRPS(@RequestBody @Valid SolicitacaoRPSNfseDTO dto) throws URISyntaxException {
        if (dto.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new user cannot already have an ID").body(null);
        }
        SolicitacaoRPSNfseDTO result = solicitacaoRPSService.salvarSolicitacaoRPS(dto);
        return ResponseEntity.created(new URI("/api/solicitacao-rps/" + dto.getId())).body(result);
    }


}
