package br.com.webpublico.service;

import br.com.webpublico.ConsumirGovBr;
import br.com.webpublico.domain.dto.ConfiguracaoGovBrDTO;
import br.com.webpublico.domain.dto.PrestadorUsuarioNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.repository.UsuarioWebJDBCRepository;
import br.com.webpublico.repository.mongo.UsuarioWebMongoRepository;
import com.google.common.base.Strings;
import org.apache.tools.ant.types.resources.Tokens;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GovBrService {

    private final Logger log = LoggerFactory.getLogger(GovBrService.class);

    private final UsuarioWebService usuarioWebService;
    private final ConfiguracaoGovBrService configuracaoGovBrService;
    private final TokenStore tokenStore;

    @Autowired
    public GovBrService(UsuarioWebService usuarioWebService,
                        ConfiguracaoGovBrService configuracaoGovBrService,
                        TokenStore tokenStore) {
        this.usuarioWebService = usuarioWebService;
        this.configuracaoGovBrService = configuracaoGovBrService;
        this.tokenStore = tokenStore;
    }

    public UserNfseDTO getUsuarioViaGovBr(String code) {
        try {
            ConfiguracaoGovBrDTO configuracaoGovBr = configuracaoGovBrService.getConfiguracaoGovBr();
            String idToken = ConsumirGovBr.extractIdToken(configuracaoGovBr, code);
            JwtClaims jwtClaims = ConsumirGovBr.getJwtClaims(configuracaoGovBr, idToken);
            String cpf = jwtClaims.getSubject();
            return usuarioWebService.efetuarLogin(cpf);
        } catch (Exception e) {
            log.error("Erro ao recuperar usuario via gov.br. Erro: {}", e.getMessage());
            log.debug("Detalhes do erro ao recuperar usuario via gov.br.", e);
        }
        return null;
    }

    public OAuth2Authentication createOAuth2Authentication(UserNfseDTO userNfseDTO) {
        // Simulando um cliente OAuth2
        String clientId = "nfseapp";
        String clientSecret = "mySecretOAuthSecret";

        // Definindo os parâmetros necessários
        String grantType = "password";  // Ou outro tipo de grant
        String redirectUri = "http://localhost/callback";
        String scope = "read write";  // Escopos solicitados

        // Definindo a lista de scopes
        HashSet<String> scopes = new HashSet<>();
        scopes.add("read");
        scopes.add("write");

        // Criando um OAuth2Request
        OAuth2Request oauth2Request = new OAuth2Request(
                Collections.singletonMap("client_id", clientId),  // client_id
                clientId,  // clientId
                Collections.emptyList(),  // authorities (sem autoridades específicas aqui)
                true,  // approvalGranted
                scopes,  // escopos
                new HashSet<>(),  // redirecionamentos
                redirectUri,  // redirecionamento
                new HashSet<>(),  // extensions (usado para parâmetros extras)
                null  // resourceIds
        );

        // Criando o OAuth2Authentication
        List<GrantedAuthority> grantedAuthorities = userNfseDTO.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userNfseDTO.getLogin(), null, grantedAuthorities);
        return new OAuth2Authentication(oauth2Request, authentication);
    }

    public OAuth2AccessToken generateToken(UserNfseDTO userNfseDTO) {
        OAuth2Authentication oAuth2Authentication = createOAuth2Authentication(userNfseDTO);
        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken("token-gov-br");

        // Configurações básicas do token
        accessToken.setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 hora de validade
        HashSet<String> scopes = new HashSet<>();
        scopes.add("read");
        scopes.add("write");
        accessToken.setScope(scopes);

        // Adicionar informações adicionais ao token
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("user_name", oAuth2Authentication.getName());
        accessToken.setAdditionalInformation(additionalInfo);

        // Armazena o token no TokenStore
        tokenStore.storeAccessToken(accessToken, oAuth2Authentication);
        return accessToken;
    }
}
