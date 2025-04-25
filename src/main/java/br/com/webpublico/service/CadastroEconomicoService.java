package br.com.webpublico.service;

import br.com.webpublico.DateUtils;
import br.com.webpublico.domain.dto.*;
import br.com.webpublico.domain.dto.enums.PermissaoUsuarioEmpresaNfse;
import br.com.webpublico.domain.dto.enums.TipoNotaFiscalServicoNfseDTO;
import br.com.webpublico.repository.AnexoLei1232006JDBCRepository;
import br.com.webpublico.repository.CadastroEconomicoJDBCRepository;
import br.com.webpublico.repository.mongo.CadastroEconomicoMongoRepository;
import br.com.webpublico.tributario.consultadebitos.ConsultaParcela;
import br.com.webpublico.tributario.consultadebitos.ResultadoParcela;
import br.com.webpublico.tributario.consultadebitos.dtos.DamDTO;
import br.com.webpublico.tributario.consultadebitos.dtos.ParcelaDTO;
import br.com.webpublico.tributario.consultadebitos.services.ConsultaDebitosService;
import br.com.webpublico.web.rest.dto.AgrupadorParcelas;
import br.com.webpublico.web.rest.dto.FiltroParcelaDTO;
import br.com.webpublico.web.rest.dto.SubstitutoTributarioDTO;
import br.com.webpublico.web.rest.util.PaginationUtil;
import com.google.common.collect.Lists;
import com.mifmif.common.regex.Generex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CadastroEconomicoService extends AbstractWPService<PrestadorServicoNfseDTO> {

    private final Logger log = LoggerFactory.getLogger(CadastroEconomicoService.class);

    private final UserService userService;
    private final CadastroEconomicoJDBCRepository cadastroEconomicoJDBCRepository;
    private final CadastroEconomicoMongoRepository cadastroEconomicoMongoRepository;
    private final ImagemService imagemService;
    private final PessoaService pessoaService;
    private final EscritorioContabilService escritorioContabilService;
    private final NaturezaJuridicaService naturezaJuridicaService;
    private final EnquadramentoFiscalService enquadramentoFiscalService;
    private final SocioService socioService;
    private final ServicoService servicoService;
    private final ConfiguracaoService configuracaoService;
    private final TributosFederaisService tributosFederaisService;
    private final AnexoLei1232006Service anexoLei1232006Service;
    private ConsultaDebitosService consultaDebitosService;

    public CadastroEconomicoService(UserService userService,
                                    CadastroEconomicoJDBCRepository cadastroEconomicoJDBCRepository,
                                    CadastroEconomicoMongoRepository cadastroEconomicoMongoRepository,
                                    ImagemService imagemService, PessoaService pessoaService,
                                    EscritorioContabilService escritorioContabilService,
                                    NaturezaJuridicaService naturezaJuridicaService,
                                    EnquadramentoFiscalService enquadramentoFiscalService,
                                    SocioService socioService, ServicoService servicoService,
                                    ConfiguracaoService configuracaoService,
                                    TributosFederaisService tributosFederaisService,
                                    AnexoLei1232006Service anexoLei1232006Service) {
        this.userService = userService;
        this.cadastroEconomicoJDBCRepository = cadastroEconomicoJDBCRepository;
        this.cadastroEconomicoMongoRepository = cadastroEconomicoMongoRepository;
        this.imagemService = imagemService;
        this.pessoaService = pessoaService;
        this.escritorioContabilService = escritorioContabilService;
        this.naturezaJuridicaService = naturezaJuridicaService;
        this.enquadramentoFiscalService = enquadramentoFiscalService;
        this.socioService = socioService;
        this.servicoService = servicoService;
        this.configuracaoService = configuracaoService;
        this.tributosFederaisService = tributosFederaisService;
        this.anexoLei1232006Service = anexoLei1232006Service;
    }


    @PostConstruct
    public void init() {
        if (consultaDebitosService == null) {
            consultaDebitosService = ConsultaDebitosService.buildFromJdbcTemplate(cadastroEconomicoJDBCRepository.getJdbcTemplate());
        }
    }

    public ResponseEntity<List<PrestadorServicoNfseDTO>> findByQuery(String query, Pageable pageable) {
        if (pageable == null) {
            pageable = PaginationUtil.generatePageRequest(0, 100);
        }
        String url = PaginationUtil.addParamsToUrl(urlsProperties.getWebpublicoPathNfse() + "/cadastro-economico/listar",
                pageable, query, getTableName(), getDefaltSearchFields());
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseTypeList());
    }

    private PrestadorServicoNfseDTO findOnMongo(Long id) {
        if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            return null;
        }
        try {
            PrestadorServicoNfseDTO one = cadastroEconomicoMongoRepository.findById(id).orElse(null);
            if (one != null) {
                return one;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveOnMongo(PrestadorServicoNfseDTO dto) {
        if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            return;
        }
        try {
            if (dto != null) {
                cadastroEconomicoMongoRepository.save(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeOnMongo(Long id) {
        if (!configuracaoService.getConfiguracaoFromServer().getUtilizaBancoCache()) {
            return;
        }
        try {
            if (id != null) {
                PrestadorServicoNfseDTO dto = findOnMongo(id);
                cadastroEconomicoMongoRepository.deleteById(id);
                imagemService.deleteOnMongo(id);
                if (dto != null && dto.getPessoa() != null)
                    imagemService.deleteOnMongo(dto.getPessoa().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<PrestadorServicoNfseDTO> findOne(Long id) {
        return new ResponseEntity<>(findById(id), HttpStatus.OK);
    }

    public PrestadorServicoNfseDTO findById(Long id) {
        PrestadorServicoNfseDTO one = findOnMongo(id);
        if (one != null) return one;
        one = cadastroEconomicoJDBCRepository.findById(id);
        one = preencherCadastroEconomico(one);
        saveOnMongo(one);
        return one;
    }

    public PrestadorServicoNfseDTO gerarChaveAcesso(Long id) {
        PrestadorServicoNfseDTO prestador = findById(id);
        prestador.setChaveAcesso(new Generex("[0-9a-fA-F]{8}").random());
        return save(prestador);
    }

    public PrestadorServicoNfseDTO findByInscricaoMunicipal(String inscricaoMunicipal) {
        PrestadorServicoNfseDTO prestador = cadastroEconomicoJDBCRepository.findByInscricaoMunicipal(inscricaoMunicipal);
        preencherCadastroEconomico(prestador);
        return prestador;
    }

    public PrestadorServicoNfseDTO findByCnpj(String cpfCnpj) {
        PrestadorServicoNfseDTO prestador = cadastroEconomicoJDBCRepository.findFirstByCnpj(cpfCnpj);
        preencherCadastroEconomico(prestador);
        return prestador;
    }

    public ResponseEntity<PrestadorServicoNfseDTO> findReceitaByCnpj(String cpfCnpj) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/cadastro-economico/buscar-na-receita-por-cnpj";
        url = UriComponentsBuilder.fromUriString(url).queryParam("cnpj", cpfCnpj).toUriString();
        return restTemplate.exchange(url, HttpMethod.GET, null, getResponseType());
    }

    public void saveUsuarioEmpresa(Long prestadorId, String login) {
        UserNfseDTO user = userService.findByLogin(login);
        PrestadorServicoNfseDTO prestador = findById(prestadorId);

        PrestadorUsuarioNfseDTO prestadorUsuario = new PrestadorUsuarioNfseDTO();
        prestadorUsuario.setPermitido(true);
        prestadorUsuario.setIdUsuario(user.getId());
        prestadorUsuario.setPrestador(prestador);

        userService.inserirPrestadorUsuario(user, prestadorUsuario);
    }

    public void criarUsuarioParaEmpresa(PrestadorServicoNfseDTO dto) {
        String url = urlsProperties.getWebpublicoPathNfse() + "/cadastro-economico/criar-usuario";
        url = UriComponentsBuilder.fromUriString(url)
                .toUriString();
        restTemplate.postForEntity(url, dto, Void.class);
    }

    public void ativarOuDesativarUsuarioEmpresa(Long prestadorId, String login, String loginResponsavel) {
        UserNfseDTO user = userService.findByLogin(login);
        UserNfseDTO userResponsavel = userService.findByLogin(loginResponsavel);
        PrestadorUsuarioNfseDTO prestadorUsuario = userService.getUserCadastroEconomicoService().findByUserAndPrestador(user.getId(), prestadorId);

        userService.getUserCadastroEconomicoService().ativarOrDesativarUserCadastroEconomico(prestadorUsuario.getId(), userResponsavel.getId());
    }

    public void convidarUsuario(ConviteUsuarioNfseDTO convite) {
        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/usuario/convidar").toUriString();
        restTemplate.postForEntity(url, convite, ConviteUsuarioNfseDTO.class);
    }

    public PrestadorServicoNfseDTO salvarPrestador(PrestadorServicoNfseDTO prestador) {
        String url = UriComponentsBuilder.fromUriString(urlsProperties.getWebpublicoPathNfse() + "/cadastro-economico").toUriString();
        ResponseEntity<PrestadorServicoNfseDTO> response = restTemplate.postForEntity(url, prestador, PrestadorServicoNfseDTO.class);
        return response.getBody();
    }

    public List<ResultadoParcela> buscarDebitosDoPrestador(Long prestadorId, FiltroParcelaDTO filtroParcelaDTO) {
        ConfiguracaoNfseDTO configuracaoFromServer = configuracaoService.getConfiguracaoFromServer();
        ConsultaParcela consultaParcela = new ConsultaParcela(consultaDebitosService);
        consultaParcela.addParameter(ConsultaParcela.Campo.CMC_ID, ConsultaParcela.Operador.IGUAL, prestadorId);
        if (configuracaoFromServer.getIdsDividasIss() != null && !configuracaoFromServer.getIdsDividasIss().isEmpty()) {
            consultaParcela.addParameter(ConsultaParcela.Campo.DIVIDA_ID, ConsultaParcela.Operador.IN, configuracaoFromServer.getIdsDividasIss());
        }

        if (filtroParcelaDTO.getSituacoes() != null && !filtroParcelaDTO.getSituacoes().isEmpty()) {
            consultaParcela.addParameter(ConsultaParcela.Campo.PARCELA_SITUACAO,
                    ConsultaParcela.Operador.IN, filtroParcelaDTO.getSituacoes());
        }
        if (filtroParcelaDTO.getVencimentoInicio() != null) {
            consultaParcela.addParameter(ConsultaParcela.Campo.PARCELA_VENCIMENTO,
                    ConsultaParcela.Operador.MAIOR_IGUAL, filtroParcelaDTO.getVencimentoInicio());
        }
        if (filtroParcelaDTO.getVencimentoFim() != null) {
            consultaParcela.addParameter(ConsultaParcela.Campo.PARCELA_VENCIMENTO,
                    ConsultaParcela.Operador.MENOR_IGUAL, filtroParcelaDTO.getVencimentoFim());
        }
        return consultaParcela.executaConsulta().getResultados();
    }

    public ParcelaDTO getDebito(Long idParcela) {
        ConsultaParcela consultaParcela = new ConsultaParcela(consultaDebitosService);
        consultaParcela.addParameter(ConsultaParcela.Campo.PARCELA_ID, ConsultaParcela.Operador.IGUAL, idParcela);
        consultaParcela.executaConsulta();
        List<ResultadoParcela> parcelas = consultaParcela.getResultados();
        return parcelas != null && !parcelas.isEmpty() ? parcelas.stream().findFirst().get().toParcelaDTO() : null;
    }

    public DamDTO buscarDamParcela(AgrupadorParcelas agrupador) {
        ResponseEntity<DamDTO> response = restTemplate.postForEntity(urlsProperties.getWebpublicoPathTributario() + "/consultar-dam", agrupador, DamDTO.class);
        return response.getBody();
    }


    @Override
    public String getTableName() {
        return "CadastroEconomico";
    }

    @Override
    public String getDefaltSearchFields() {
        return "pessoa.nome";
    }

    @Override
    public ParameterizedTypeReference<List<PrestadorServicoNfseDTO>> getResponseTypeList() {
        return new ParameterizedTypeReference<List<PrestadorServicoNfseDTO>>() {
        };
    }

    @Override
    public ParameterizedTypeReference<PrestadorServicoNfseDTO> getResponseType() {
        return new ParameterizedTypeReference<PrestadorServicoNfseDTO>() {
        };
    }

    public PrestadorServicoNfseDTO save(PrestadorServicoNfseDTO prestador) {
        pessoaService.save(prestador.getPessoa());
        cadastroEconomicoJDBCRepository.update(prestador);
        enquadramentoFiscalService.update(prestador.getEnquadramentoFiscal());
        saveOnMongo(prestador);
        return prestador;
    }

    public List<PrestadorUsuarioNfseDTO> buscarUsuariosPrestador(Long idCadastroEconomico, String situacao) {
        return userService.findUserCadastroEconomico(idCadastroEconomico, situacao);
    }

    public List<PrestadorUsuarioNfseDTO> buscarUsuariosInativosPrestador(Long idCadastroEconomico) {
        return buscarUsuariosPrestador(idCadastroEconomico, "PENDENTE");
    }

    public Page<PrestadorServicoNfseDTO> buscarPrestadoresUsuario(UserNfseDTO user,
                                                                  Pageable pageable,
                                                                  String filtro) {
        Page<PrestadorServicoNfseDTO> page;
        if (user.getRoles().contains("ROLE_ADMIN")) {
            page = cadastroEconomicoJDBCRepository.findByFiltro(pageable, filtro);
        } else {
            page = cadastroEconomicoJDBCRepository.findByUserNfseFiltro(pageable, user, filtro);
        }
        if (page.getContent() != null) {
            for (PrestadorServicoNfseDTO dto : page.getContent()) {
                if (dto.getPessoa() != null) {
                    dto.setPessoa(pessoaService.findById(dto.getPessoa().getId()));
                }
            }
        }
        return page;
    }

    public void enviarDam(AgrupadorParcelas agrupador) {
        restTemplate.postForEntity(urlsProperties.getWebpublicoPathTributario() + "/enviar-dam", agrupador, Void.class);
    }


    public List<String> findRolesEmpresa(Long idUserCadastroEconomico) {
        return cadastroEconomicoJDBCRepository.findRolesEmpresa(idUserCadastroEconomico);
    }

    public void trocarEmpresa(Long prestadorId) {
        UserNfseDTO user = userService.getUserWithAuthorities();
        PrestadorUsuarioNfseDTO prestadorUsuario = findByUserAndPrestador(user.getId(), prestadorId);

        if (prestadorUsuario == null && user.getRoles().contains("ROLE_ADMIN")) {
            prestadorUsuario = new PrestadorUsuarioNfseDTO();
            prestadorUsuario.setContador(false);
            prestadorUsuario.setPermitido(true);
            prestadorUsuario.setPrestador(findOne(prestadorId).getBody());
            prestadorUsuario.setRoles(Lists.newArrayList(PermissaoUsuarioEmpresaNfse.values()).stream().map(Enum::name).collect(Collectors.toList()));
            userService.inserirPrestadorUsuario(user, prestadorUsuario);
        }

        if (prestadorUsuario != null) {
            user.setEmpresa(prestadorUsuario);
            userService.atualizarPrestadorUsuario(user);
        }
        userService.saveOnMongo(user);
    }

    public List<AnexoLei1232006NfseDTO> buscarAnexosLei1232006() {
        return anexoLei1232006Service.buscarAnexos();
    }

    public void salvarTributosFederais(TributosFederaisNfseDTO tributosFederaisNfse) {
        if (tributosFederaisNfse.getId() == null) {
            tributosFederaisService.inserir(tributosFederaisNfse);
            cadastroEconomicoJDBCRepository.atualizarTributosFederais(tributosFederaisNfse.getId(),
                    tributosFederaisNfse.getPrestadorServicosId());
        } else {
            tributosFederaisService.atualizar(tributosFederaisNfse);
        }
    }

    public TributosFederaisNfseDTO buscarTributosFederais(Long prestadorId) {
        return tributosFederaisService.findByPrestadorId(prestadorId);
    }


    private PrestadorServicoNfseDTO preencherCadastroEconomico(PrestadorServicoNfseDTO dto) {
        if (dto == null)
            return dto;
        if (dto.getPessoa() != null) {
            dto.setPessoa(pessoaService.findById(dto.getPessoa().getId()));
        }
        if (dto.getNaturezaJuridica() != null) {
            dto.setNaturezaJuridica(naturezaJuridicaService.findById(dto.getNaturezaJuridica().getId()));
        }
        if (dto.getEnquadramentoFiscal() != null) {
            dto.setEnquadramentoFiscal(enquadramentoFiscalService.findById(dto.getEnquadramentoFiscal().getId()));
            dto.setEmiteNfs(TipoNotaFiscalServicoNfseDTO.ELETRONICA.equals(dto.getEnquadramentoFiscal().getTipoNotaFiscal()));
        }
        dto.setSocios(socioService.findByIdCadastro(dto.getId()));
        dto.setServicos(servicoService.findByIdCadastro(dto.getId()));
        return dto;
    }

    public List<PrestadorServicoNfseDTO> findByCnpjAtivo(String cnpj) {
        List<PrestadorServicoNfseDTO> prestadores = cadastroEconomicoJDBCRepository.findByCnpjAtivo(cnpj);
        if (prestadores != null && !prestadores.isEmpty()) {
            for (PrestadorServicoNfseDTO prestador : prestadores) {
                preencherCadastroEconomico(prestador);
            }
        }
        return prestadores;
    }

    public PrestadorUsuarioNfseDTO findByUserAndPrestador(Long idUsuario, Long idCadstro) {
        PrestadorUsuarioNfseDTO userCadastroEconomico = cadastroEconomicoJDBCRepository.findByUserAndPrestador(idUsuario, idCadstro);
        if (userCadastroEconomico != null) {
            userCadastroEconomico.setPrestador(findOne(userCadastroEconomico.getPrestador().getId()).getBody());
            userCadastroEconomico.setRoles(findRolesEmpresa(userCadastroEconomico.getId()));
        }
        return userCadastroEconomico;
    }

    public List<SubstitutoTributarioDTO> buscarSubstitutosTributarios(String cpf_cnpj, String inscricao, String nome_razaosocial) {
        return cadastroEconomicoJDBCRepository.buscarSubstitutosTributarios(cpf_cnpj, inscricao, nome_razaosocial);
    }

    public Long buscarQuantidadePrestadoresAutorizados() {
        return cadastroEconomicoJDBCRepository.buscarQuantidadePrestadoresAutorizados();
    }

    public BigDecimal getRBT12ParaRetencao(Long prestadorId) {
        Date referencia = new Date();
        referencia = DateUtils.adicionarMeses(referencia, -1);
        return cadastroEconomicoJDBCRepository.getRBT12(prestadorId, DateUtils.getAno(referencia), DateUtils.getMes(referencia));
    }

    public ImagemUsuarioNfseDTO getImagemPrestador(Long id) {
        PrestadorServicoNfseDTO prestador = findById(id);
        return pessoaService.getImagemPessoa(prestador.getPessoa().getId());
    }
}
