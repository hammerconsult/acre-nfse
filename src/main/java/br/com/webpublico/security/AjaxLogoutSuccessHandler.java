package br.com.webpublico.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler {

    public static final String BEARER_AUTHENTICATION = "Bearer ";

    @Inject
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        // Request the token
        String token = request.getHeader("authorization");
        if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
            final OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(StringUtils.substringAfter(token, BEARER_AUTHENTICATION));
            if (oAuth2AccessToken != null) {
                tokenStore.removeAccessToken(oAuth2AccessToken);
                HttpSession session = request.getSession(false);
                if (session != null) {
                    logger.debug("Invalidating session: " + session.getId());
                    session.invalidate();
                }
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(null);
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
