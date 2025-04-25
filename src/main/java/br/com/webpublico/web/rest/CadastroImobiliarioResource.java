package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.CadastroImobiliarioNfseDTO;
import br.com.webpublico.domain.dto.CadastroImobiliarioSearchNfseDTO;
import br.com.webpublico.service.CadastroImobiliarioService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cadastro-imobiliario")
public class CadastroImobiliarioResource {

    @Autowired
    private CadastroImobiliarioService service;

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CadastroImobiliarioNfseDTO> byId(@PathVariable Long id) {
        return service.findById(id);
    }


    @RequestMapping(value = "/search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CadastroImobiliarioSearchNfseDTO>> getAll(
        @RequestParam(value = "page", required = false) Integer offset,
        @RequestParam(value = "per_page", required = false) Integer limit,
        @RequestParam(value = "search", required = false) String search) {
        return service.search(PaginationUtil.generatePageRequest(offset, limit), search);
    }

    @GetMapping(value = "/by-inscricao")
    public ResponseEntity<CadastroImobiliarioNfseDTO> findByInscricao(@RequestParam(value = "inscricao") String inscricao) {
        return service.findByInscricao(inscricao);
    }
}
