package br.com.webpublico.config;

import br.com.webpublico.security.ApiKeyAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/bower_components/**")
                .antMatchers("/i18n/**")
                .antMatchers("/assets/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/api/register")
                .antMatchers("/api/prestador-servico/vincular-usuario")
                .antMatchers("/api/pessoa_por_cpfCnpj/**")
                .antMatchers("/api/prestadorServicos_por_cpfCnpj/**")
                .antMatchers("/api/convite-usuario/**")
                .antMatchers("/api/notaFiscals/autenticar**")
                .antMatchers("/api/publica/nota-fiscal/**")
                .antMatchers("/api/publico/**")
                .antMatchers("/publico/**")
                .antMatchers("/api/enumerado**")
                .antMatchers("/api/imagem/**")
                .antMatchers("/api/activate")
                .antMatchers("/api/account/reset_password/init")
                .antMatchers("/api/account/reset_password/finish")
                .antMatchers("/api/cep/**")
                .antMatchers("/api/municipios/**")
                .antMatchers("/api/_search/municipios/**")
                .antMatchers("/api/_search/servicos/**")
                .antMatchers("/api/_search/cnaes/**")
                .antMatchers("/api/municipio_por_descricao/**")
                .antMatchers("/api/arquivo/**")
                .antMatchers("/api/externo/**")
                .antMatchers("/api/users-acrescentar-tentativa-login/**")
                .antMatchers("/api/zerar-tentativa-login/**")
                .antMatchers("/api/cnae-por-servico/**")
                .antMatchers("/api/servico-por-cnae/**")
                .antMatchers("/api/exportar/**")
                .antMatchers("/iss/nfse_v2_03.xsd")
                .antMatchers("/test/**")
                .antMatchers("/api/prestador-servico/criar-usuario-empresa")
                .antMatchers("/api/user-from-login")
                .antMatchers("/api/user-from-reset-key")
                .antMatchers("/api/classificacoes")
                .antMatchers("/api/naturezas-juridicas")
                .antMatchers("/api/prestadorServicos_por_cpfCnpj")
                .antMatchers("/api/prestador-servico/salvar-novo")
                .antMatchers("/api/servicos")
                .antMatchers("/api/sistema/versao**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
