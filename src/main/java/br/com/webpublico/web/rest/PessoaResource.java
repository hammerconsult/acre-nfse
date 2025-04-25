package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.PessoaNfseDTO;
import br.com.webpublico.exception.OperacaoNaoPermitidaException;
import br.com.webpublico.security.SecurityUtils;
import br.com.webpublico.service.PessoaService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pessoa.
 */
@RestController
@RequestMapping("/api")
public class PessoaResource {

    private final Logger log = LoggerFactory.getLogger(PessoaResource.class);

    @Inject
    private PessoaService pessoaService;

    /**
     * PUT  /pessoas -> Updates an existing pessoa.
     */
    @RequestMapping(value = "/pessoas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PessoaNfseDTO> update(@RequestBody PessoaNfseDTO pessoa) throws URISyntaxException {
        log.debug("REST request to update Pessoa : {}", pessoa);
        return ResponseEntity.ok(pessoaService.save(pessoa));
    }

    /**
     * GET  /pessoas -> get all the pessoas.
     */
    @RequestMapping(value = "/pessoas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PessoaNfseDTO>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                      @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        return pessoaService.findAll(PaginationUtil.generatePageRequest(offset, limit));

    }

    /**
     * GET  /pessoas/:id -> get the "id" pessoa.
     */
    @RequestMapping(value = "/pessoas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PessoaNfseDTO> get(@PathVariable Long id) throws InterruptedException {
        log.debug("REST request to get Pessoa : {}", id);
        return pessoaService.findOne(id);
    }

    /**
     * SEARCH  /_search/pessoas/:query -> search for the pessoa corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/pessoas/{query}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PessoaNfseDTO>> search(@PathVariable String query) {
        return pessoaService.findByQuery(query);
    }


    @RequestMapping(value = "/pessoa_por_cpfCnpj/{cpfCnpj}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PessoaNfseDTO> getPorCpfCnpj(@PathVariable String cpfCnpj) {
        log.debug("REST request to get Pessoa Repo: {}", cpfCnpj);
        try {
            return Optional.ofNullable(pessoaService.findByCpfCnpj(cpfCnpj))
                .map(tomador -> new ResponseEntity<>(
                    tomador,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            log.debug("Não foi encontrada nenhuma pessoa {} ", cpfCnpj);
        }
        return null;
    }


    @RequestMapping(value = "/pessoa_do_usuario",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PessoaNfseDTO> getPorUsuarioLogado() {
        log.debug("REST request to get Pessoa Do Usuário ");
        try {
            return Optional.ofNullable(pessoaService.findByLogin(SecurityUtils.getCurrentLogin()))
                .map(pessoa -> new ResponseEntity<>(
                    pessoa,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            log.debug("Não foi encontrada nenhuma pessoa");
        }
        return null;
    }

    @RequestMapping(value = "/pessoa_do_usuario/login/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PessoaNfseDTO> getPessoaPorUsuario(@PathVariable String login) {
        try {
            return Optional.ofNullable(pessoaService.findByLogin(login))
                .map(pessoa -> new ResponseEntity<>(
                    pessoa,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            throw new OperacaoNaoPermitidaException("Não foi possível encontrar nehuma pessoa para o login informado!");
        }
    }

}
