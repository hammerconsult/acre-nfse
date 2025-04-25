package br.com.webpublico.security;

import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.service.UsuarioWebService;
import br.com.webpublico.tributario.consultadebitos.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    private final UsuarioWebService usuarioWebService;

    @Autowired
    public UserDetailsService(UsuarioWebService usuarioWebService) {
        this.usuarioWebService = usuarioWebService;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        String loginLowerCase = StringUtil.retornaApenasNumeros(login.toLowerCase());

        UserNfseDTO dto = usuarioWebService.efetuarLogin(loginLowerCase);

        Optional<UserNfseDTO> userFromDatabase = Optional.of(dto);

        return userFromDatabase.map(user -> {
            if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + loginLowerCase + " was not activated");
            }
            List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(loginLowerCase,
                    user.getPassword(),
                    grantedAuthorities);

        }).orElseThrow(() -> new UsernameNotFoundException("User " + loginLowerCase + " was not found in the " +
                "database"));
    }
}
