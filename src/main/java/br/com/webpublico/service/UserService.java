package br.com.webpublico.service;

import br.com.webpublico.StringUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.consultanfse.Operador;
import br.com.webpublico.domain.dto.consultanfse.ParametroQuery;
import br.com.webpublico.domain.dto.consultanfse.ParametroQueryCampo;
import br.com.webpublico.domain.dto.enums.TipoTemplateNfse;
import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.domain.dto.template.TrocaTagEmailAtivacaoUsuario;
import br.com.webpublico.domain.dto.template.TrocaTagEmailSolicitacaoAlteracaoSenha;
import br.com.webpublico.exception.OperacaoNaoPermitidaException;
import br.com.webpublico.repository.TemplateJDBCRepository;
import br.com.webpublico.repository.UsuarioWebJDBCRepository;
import br.com.webpublico.repository.mongo.UsuarioWebMongoRepository;
import br.com.webpublico.security.SecurityUtils;
import br.com.webpublico.service.util.RandomUtil;
import br.com.webpublico.web.rest.dto.TrocarSenhaDTO;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService extends AbstractWPService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UsuarioWebJDBCRepository usuarioWebJDBCRepository;
    private final UserCadastroEconomicoService userCadastroEconomicoService;
    private final UsuarioWebMongoRepository usuarioWebMongoRepository;
    private final ImagemService imagemService;
    private final ConfiguracaoService configuracaoService;
    private final TemplateJDBCRepository templateJDBCRepository;
    private final EmailService emailService;
    private final PessoaService pessoaService;
    private final DadosPessoaisService dadosPessoaisService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public UserService(UsuarioWebJDBCRepository usuarioWebJDBCRepository,
                       UserCadastroEconomicoService userCadastroEconomicoService,
                       UsuarioWebMongoRepository usuarioWebMongoRepository, ImagemService imagemService,
                       ConfiguracaoService configuracaoService, TemplateJDBCRepository templateJDBCRepository,
                       EmailService emailService, PessoaService pessoaService,
                       DadosPessoaisService dadosPessoaisService, PasswordEncoder passwordEncoder,
                       RestTemplate restTemplate) {
        this.usuarioWebJDBCRepository = usuarioWebJDBCRepository;
        this.userCadastroEconomicoService = userCadastroEconomicoService;
        this.usuarioWebMongoRepository = usuarioWebMongoRepository;
        this.imagemService = imagemService;
        this.configuracaoService = configuracaoService;
        this.templateJDBCRepository = templateJDBCRepository;
        this.emailService = emailService;
        this.pessoaService = pessoaService;
        this.dadosPessoaisService = dadosPessoaisService;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    public UserCadastroEconomicoService getUserCadastroEconomicoService() {
        return userCadastroEconomicoService;
    }

    public UserNfseDTO insert(UserNfseDTO dto) {
        validarNovoUsuario(dto);
        registrarDadosPessoais(dto);
        gerarChaveAtivacao(dto);
        adicionarPermissaoInicial(dto);
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuarioWebJDBCRepository.insert(dto);
        enviarEmailAtivacaoUsuario(dto);
        return dto;
    }

    private void registrarDadosPessoais(UserNfseDTO dto) {
        PessoaNfseDTO pessoa = pessoaService.findByCpfCnpj(dto.getLogin());
        if (pessoa != null) {
            dto.setPessoaId(pessoa.getId());
        }
        dadosPessoaisService.salvar(dto.getDadosPessoais());
    }

    private void validarNovoUsuario(UserNfseDTO dto) {
        if (usuarioWebJDBCRepository.findByAtributo("OBJ.LOGIN", dto.getLogin()) != null) {
            throw new NfseOperacaoNaoPermitidaException("O CPF informado já está em uso. Verifique se o CPF informado está correto. Caso o CPF informado esteja correto, entre em contato com o suporte ou siga os passos do esqueci minha senha na página inicial.");
        }
        dto.validarDadosPessoaisUsuario();
    }

    private void adicionarPermissaoInicial(UserNfseDTO dto) {
        dto.setRoles(new ArrayList<>());
        dto.getRoles().add("ROLE_USER");
        dto.getRoles().add("ROLE_CONTRIBUINTE");
    }

    private void gerarChaveAtivacao(UserNfseDTO dto) {
        dto.setActivated(false);
        dto.setActivationKey(RandomUtil.generateActivationKey());
    }

    @Async
    public void enviarEmailAtivacaoUsuario(UserNfseDTO dto) {
        TemplateNfseDTO templateEmail = templateJDBCRepository.findByTipo(TipoTemplateNfse.ATIVACAO_CADASTRO);

        String conteudo = new TrocaTagEmailAtivacaoUsuario(dto, configuracaoService.getConfiguracaoFromServer())
                .trocarTags(templateEmail.getConteudo());

        emailService.enviarEmail(dto.getEmail(), "Ativação de Cadastro para acesso ao sistema WP ISS", conteudo,
                Boolean.TRUE);
    }

    public void activateRegistration(String key) {
        UserNfseDTO dto = usuarioWebJDBCRepository.findByAtributo("obj.activation_key", key);
        dto.setActivated(true);
        dto.setActivationKey(null);
        salvar(dto);
    }

    public ResponseEntity<String> updateUserInformation(UserNfseDTO user) {
        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/usuario").toUriString();
        return restTemplate.postForEntity(url, user, String.class);
    }

    public void completePasswordReset(String newPassword, String key) {
        UserNfseDTO user = findByResetKey(key);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetKey(null);
        salvar(user);
    }

    private void salvar(UserNfseDTO user) {
        usuarioWebJDBCRepository.update(user);
        saveOnMongo(user);
    }

    public SimpleValidationSuccessNfseDTO requestPasswordReset(String cpf) {
        UserNfseDTO user = usuarioWebJDBCRepository.findByAtributo("OBJ.LOGIN", StringUtils.removerMascaraCpfCnpj(cpf));
        if (user == null) {
            throw new NfseOperacaoNaoPermitidaException("Verifique o cpf ou cnpj informado, o mesmo não se encontra cadastrado");
        }
        user.setResetKey(RandomUtil.generateResetKey());
        salvar(user);
        enviarEmailResetarSenha(user);
        return new SimpleValidationSuccessNfseDTO("Operação Realizada",
                "Verifique a caixa de entrada do seu e-mail (" + user.getEmail() + "), você receberá os detalhes para gerar uma nova senha.");
    }

    public void enviarEmailResetarSenha(UserNfseDTO user) {
        TemplateNfseDTO template = templateJDBCRepository.findByTipo(TipoTemplateNfse.SOLICITACAO_ALTERACAO_SENHA);
        String conteudoEmail = new TrocaTagEmailSolicitacaoAlteracaoSenha(user, configuracaoService.getConfiguracaoFromServer())
                .trocarTags(template.getConteudo());
        emailService.enviarEmail(user.getEmail(), "Alteração de senha para acesso ao sistema WP ISS", conteudoEmail,
                Boolean.TRUE);
    }

    public void changePassword(TrocarSenhaDTO trocarSenhaDTO) {
        UserNfseDTO user = findByLogin(SecurityUtils.getCurrentLogin());
        if (!passwordEncoder.matches(trocarSenhaDTO.getOldPassword(), user.getPassword())) {
            throw new OperacaoNaoPermitidaException("Senha Atual incorreta.");
        }
        user.setPassword(passwordEncoder.encode(trocarSenhaDTO.getNewPassword()));
        usuarioWebJDBCRepository.changePassword(user);
        limparDadosDoUsuarioNoMongo(user.getLogin());
    }

    public UserNfseDTO getUserWithAuthorities() {
        try {
            return findByLogin(SecurityUtils.getCurrentLogin());
        } catch (Exception e) {
            return null;
        }
    }

    public UserNfseDTO getUserWithAuthoritiesAndEmpresas() {
        return getUserWithAuthorities();
    }


    public void registrarTentativaLogin(String login) {
        usuarioWebJDBCRepository.registrarTentativaLogin(login);
    }

    public void zerarTentativaLogin(String login) {
        usuarioWebJDBCRepository.zerarTentativaLogin(login);
    }

    public Page<UserNfseDTO> buscarUsuariosPorFiltro(Pageable pageable, String filtro) throws Exception {
        ParametroQuery parametroQuery = new ParametroQuery();
        parametroQuery.setJuncao(" or ");
        parametroQuery.setParametroQueryCampos(new ArrayList<>());
        parametroQuery.getParametroQueryCampos().add(new ParametroQueryCampo("replace(replace(replace(pf.cpf,'.',''),'-',''),'/','')",
                Operador.LIKE, "%" + StringUtils.removerMascaraCpfCnpj(filtro) + "%"));
        parametroQuery.getParametroQueryCampos().add(new ParametroQueryCampo("lower(pf.nome)",
                Operador.LIKE, "%" + filtro.toLowerCase() + "%"));
        parametroQuery.getParametroQueryCampos().add(new ParametroQueryCampo("lower(obj.email)",
                Operador.LIKE, "%" + filtro.toLowerCase() + "%"));
        return usuarioWebJDBCRepository.consultarPaginado(pageable, Lists.newArrayList(parametroQuery), "");
    }

    public Page<UserNfseDTO> findAllWherePessoaIsNotNull(Pageable pageable) {
        return (Page<UserNfseDTO>) findAll(pageable);
    }

    @Override
    public String getTableName() {
        return "Usuario";
    }

    @Override
    public String getDefaltSearchFields() {
        return "login, email";
    }

    @Override
    public ParameterizedTypeReference<List<UserNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<UserNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<UserNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<UserNfseDTO>() {
        };
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

    public UserNfseDTO efetuarLogin(String login) {
        limparDadosDoUsuarioNoMongo(login);
        UserNfseDTO user = findByLogin(login);
        if (user != null) {
            tratarPermissoes(user);
            user = findByLogin(user.getLogin());
        }
        return user;
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

    public UserNfseDTO findByResetKey(String resetKey) {
        return usuarioWebJDBCRepository.findByAtributo(" obj.reset_key ", resetKey);
    }

    public UserNfseDTO findByLogin(String login) {
        UserNfseDTO dto;
        if (configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            dto = usuarioWebMongoRepository.findByLogin(login);
            if (dto != null && !Strings.isNullOrEmpty(dto.getEmail())) {
                return dto;
            }
        }
        dto = usuarioWebJDBCRepository.findByAtributo("obj.login", login);
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

    public List<PrestadorUsuarioNfseDTO> findUserCadastroEconomico(Long idCadastroEconomico, String situacao) {
        return usuarioWebJDBCRepository.findUserCadastroEconomico(idCadastroEconomico, situacao);
    }

    public void removeOnMongo(Long id) {
        try {
            if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
                return;
            }
            if (id != null) {
                UserNfseDTO byPessoaId = usuarioWebMongoRepository.findByPessoaId(id);
                if (byPessoaId != null) {
                    usuarioWebMongoRepository.deleteById(byPessoaId.getId());
                    imagemService.deleteOnMongo(byPessoaId.getPessoaId());
                }
                UserNfseDTO dto = usuarioWebMongoRepository.findById(id).orElse(null);
                usuarioWebMongoRepository.deleteById(id);
                if (dto != null)
                    imagemService.deleteOnMongo(dto.getPessoaId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizarPrestadorUsuario(UserNfseDTO user) {
        usuarioWebJDBCRepository.atualizarPrestadorUsuario(user);
    }

    public void inserirPrestadorUsuario(UserNfseDTO user, PrestadorUsuarioNfseDTO prestadorUsuario) {
        usuarioWebJDBCRepository.inserirPrestadorUsuario(user, prestadorUsuario);
    }

    public Long buscarQuantidadeUsuariosAtivos() {
        return usuarioWebJDBCRepository.buscarQuantidadeUsuariosAtivos();
    }

    public void atualizarPrestadorUsuario(PrestadorUsuarioNfseDTO prestadorUsuario) {
        usuarioWebJDBCRepository.atualizarPermissaoPrestadorUsuario(prestadorUsuario);
    }

    public void inativar(String login) {
        UserNfseDTO byLogin = findByLogin(login);
        byLogin.setActivated(false);
        usuarioWebJDBCRepository.update(byLogin);
        usuarioWebMongoRepository.deleteById(byLogin.getId());
    }

    public void removerUserNfseCadastroEconomico(Long id) {
        usuarioWebJDBCRepository.removerUserNfseCadastroEconomico(id);
    }
}
