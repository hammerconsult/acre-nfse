package br.com.webpublico.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_TOKEN_HEADER_NAME = "API-KEY";

    @Value("${webpublico.api-key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Authentication authentication = getAuthentication((HttpServletRequest) request);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (BadCredentialsException e) {
            throw e;
        }
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKeyRequest = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKeyRequest != null && apiKeyRequest.equals(apiKey)) {
            return new ApiKeyAuthentication(apiKey, AuthorityUtils.createAuthorityList("ROLE_API"));
        }
        return null;
    }
}