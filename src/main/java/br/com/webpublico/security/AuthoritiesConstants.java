package br.com.webpublico.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String CONTRIBUINTE = "ROLE_CONTRIBUINTE";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
