package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.SimpleValidationSuccessNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.service.CadastroEconomicoService;
import br.com.webpublico.service.UserService;
import br.com.webpublico.web.rest.dto.TrocarSenhaDTO;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);
    @Inject
    private UserService userService;
    @Inject
    private CadastroEconomicoService cadastroEconomicoService;


    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserNfseDTO> registerAccount(@Valid @RequestBody UserNfseDTO user) {
        return ResponseEntity.ok(userService.insert(user));

    }


    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> activateAccount(@RequestParam(value = "key") String key) {
        userService.activateRegistration(key);
        return ResponseEntity.ok(null);
    }

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserNfseDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthoritiesAndEmpresas())
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody UserNfseDTO userDTO) {
        return userService.updateUserInformation(userDTO);
    }

    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody TrocarSenhaDTO trocarSenhaDTO) {
        userService.changePassword(trocarSenhaDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/reset_password/init",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SimpleValidationSuccessNfseDTO> requestPasswordReset(@RequestBody String cpf) {
        return new ResponseEntity<>(userService.requestPasswordReset(cpf), HttpStatus.OK);
    }


    @RequestMapping(value = "/account/reset_password/finish",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestParam(value = "key") String key, @RequestParam(value = "newPassword") String newPassword) {
        userService.completePasswordReset(newPassword, key);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) && password.length() >= UserNfseDTO.PASSWORD_MIN_LENGTH && password.length() <= UserNfseDTO.PASSWORD_MAX_LENGTH);
    }

}
