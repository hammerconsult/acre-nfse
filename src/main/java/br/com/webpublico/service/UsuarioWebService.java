package br.com.webpublico.service;

import br.com.webpublico.domain.dto.PrestadorUsuarioNfseDTO;
import br.com.webpublico.domain.dto.UserNfseDTO;
import br.com.webpublico.repository.UsuarioWebJDBCRepository;
import br.com.webpublico.repository.mongo.UsuarioWebMongoRepository;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioWebService {

    private final Logger log = LoggerFactory.getLogger(UsuarioWebService.class);

    private final ConfiguracaoService configuracaoService;
    private final UsuarioWebMongoRepository usuarioWebMongoRepository;
    private final UsuarioWebJDBCRepository usuarioWebJDBCRepository;
    private final UserCadastroEconomicoService userCadastroEconomicoService;

    @Autowired
    public UsuarioWebService(ConfiguracaoService configuracaoService,
                             UsuarioWebMongoRepository usuarioWebMongoRepository,
                             UsuarioWebJDBCRepository usuarioWebJDBCRepository,
                             UserCadastroEconomicoService userCadastroEconomicoService) {
        this.configuracaoService = configuracaoService;
        this.usuarioWebMongoRepository = usuarioWebMongoRepository;
        this.usuarioWebJDBCRepository = usuarioWebJDBCRepository;
        this.userCadastroEconomicoService = userCadastroEconomicoService;
    }

    public UserNfseDTO efetuarLogin(String login) {
        limparDadosDoUsuarioNoMongo(login);
        UserNfseDTO user = findByLogin(login);
        if (user != null) {
            tratarPermissoes(user);
            user = findByLogin(user.getLogin());
        }
        return user;
    }

    public UserNfseDTO findByLogin(String login) {
        UserNfseDTO dto;
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            dto = usuarioWebMongoRepository.findByLogin(login);
            if (dto != null && !Strings.isNullOrEmpty(dto.getEmail())) {
                return dto;
            }
        }
        dto = usuarioWebJDBCRepository.findByAtributo("OBJ.LOGIN", login);
        try {
            if (dto != null) {
                dto.setRoles(usuarioWebJDBCRepository.findRoles(dto.getId()));
                if (dto.getEmpresa() != null) {
                    dto.setEmpresa(userCadastroEconomicoService.findByUserAndPrestador(dto.getId(), dto.getEmpresa().getPrestador().getId()));
                } else {
                    PrestadorUsuarioNfseDTO firstUserCadastroEconomico = userCadastroEconomicoService.findFirstUserCadastroEconomico(dto.getId());
                    if (firstUserCadastroEconomico != null) {
                        dto.setEmpresa(firstUserCadastroEconomico);
                        usuarioWebJDBCRepository.update(dto);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erro ao recuperar cadastros do usuário", e);
        }
        saveOnMongo(dto);
        return dto;
    }

    public void saveOnMongo(UserNfseDTO dto) {
        if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            return;
        }
        if (dto != null && dto.getId() != null) {
            usuarioWebMongoRepository.save(dto);
        }
    }

    private void tratarPermissoes(UserNfseDTO user) {
        tratarPermissaoAdmin(user);
    }

    private void tratarPermissaoAdmin(UserNfseDTO user) {
        boolean fiscalTributario = usuarioWebJDBCRepository.isFiscalTributario(user.getLogin());
        if (fiscalTributario && !user.getRoles().stream().anyMatch(s -> s.equals("ROLE_ADMIN"))) {
            usuarioWebJDBCRepository.insertNfseUserAuthority(user, "ROLE_ADMIN");
        } else if (!fiscalTributario && user.getRoles().stream().anyMatch(s -> s.equals("ROLE_ADMIN"))) {
            usuarioWebJDBCRepository.removeNfseUserAuthority(user, "ROLE_ADMIN");
            usuarioWebJDBCRepository.removerPrestadoresUsuario(user);
        }
    }

    public void limparDadosDoUsuarioNoMongo(String login) {
        if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            return;
        }
        UserNfseDTO byLogin = usuarioWebMongoRepository.findByLogin(login);
        if (byLogin != null) {
            usuarioWebMongoRepository.deleteById(byLogin.getId());
            byLogin.setRoles(usuarioWebJDBCRepository.findRoles(byLogin.getId()));
        }
    }
}
