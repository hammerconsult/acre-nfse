package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.security.SecurityUtils;
import br.com.webpublico.service.UserService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserService userService;


    /**
     * GET  /users -> get all users.
     */
    @RequestMapping(value = "/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserNfseDTO>> getAllUsers(@RequestParam(value = "page", required = false) Integer offset,
                                                         @RequestParam(value = "per_page", required = false) Integer limit)
            throws URISyntaxException {
        Page<UserNfseDTO> page = userService.findAllWherePessoaIsNotNull(PaginationUtil.generatePageRequest(offset, limit));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/users/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<UserNfseDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return new ResponseEntity<>(userService.findByLogin(login), HttpStatus.OK);
    }

    @RequestMapping(value = "/users-acrescentar-tentativa-login/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getUserTentativaLogin(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        userService.registrarTentativaLogin(login);
    }

    @RequestMapping(value = "/users-zerar-tentativa-login/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getUserZerarLogin(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        userService.zerarTentativaLogin(login);
    }

    @RequestMapping(value = "/_search/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserNfseDTO>> searchCpf(Pageable pageable, @RequestParam(value = "query", defaultValue = "") String query) throws Exception {
        Page<UserNfseDTO> registros = userService.buscarUsuariosPorFiltro(pageable, query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(registros, "",
                pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(registros.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/user-from-login",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<UserNfseDTO> getUserFromLogin(@RequestParam String login) {
        log.debug("REST request to get User : {}", login);
        return new ResponseEntity<>(userService.findByLogin(login), HttpStatus.OK);
    }

    @RequestMapping(value = "/user-from-reset-key",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<UserNfseDTO> getUserFromResetKey(@RequestParam String resetKey) {
        return new ResponseEntity<>(userService.findByResetKey(resetKey), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/inativar",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity inativar() {
        String login = SecurityUtils.getCurrentLogin();
        log.debug("REST request to get User : {}", login);
        userService.inativar(login);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/publico/user/remove-mongo/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void removerMongo(@PathVariable Long id) {
        userService.removeOnMongo(id);
    }
}
