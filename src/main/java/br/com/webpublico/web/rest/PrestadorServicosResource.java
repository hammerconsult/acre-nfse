package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.*;
import br.com.webpublico.security.SecurityUtils;
import br.com.webpublico.service.CadastroEconomicoService;
import br.com.webpublico.service.UserService;
import br.com.webpublico.web.rest.util.PaginationUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing PrestadorServicos.
 */
@RestController
@RequestMapping("/api")
public class PrestadorServicosResource {

    private final Logger log = LoggerFactory.getLogger(PrestadorServicosResource.class);

    @Inject
    private CadastroEconomicoService cadastroEconomicoService;
    @Inject
    private UserService userService;

    @RequestMapping(value = "/prestadorServicos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrestadorServicoNfseDTO>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                                @RequestParam(value = "per_page", required = false) Integer limit) {
        return cadastroEconomicoService.findAll(PaginationUtil.generatePageRequest(offset, limit));
    }

    @RequestMapping(value = "/prestadorServicos",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServicoNfseDTO> save(@RequestBody PrestadorServicoNfseDTO prestador) {
        return ResponseEntity.ok(cadastroEconomicoService.save(prestador));
    }

    /**
     * GET  /prestadorServicoss/:id -> get the "id" prestadorServicos.
     */
    @RequestMapping(value = "/prestadorServicos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServicoNfseDTO> get(@PathVariable Long id) {
        log.debug("REST request to get PrestadorServicos : {}", id);
        return cadastroEconomicoService.findOne(id);
    }

    @RequestMapping(value = "/prestadorServicos/gerar-chave-acesso/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServicoNfseDTO> gerarChaveAcesso(@PathVariable Long id) {
        log.debug("REST request to get PrestadorServicos : {}", id);
        return ResponseEntity.ok(cadastroEconomicoService.gerarChaveAcesso(id));
    }

    /**
     * SEARCH  /_search/prestadorServicoss/:query -> search for the prestadorServicos corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/prestadorServicos/{query}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrestadorServicoNfseDTO> search(@PathVariable String query) {
        return cadastroEconomicoService.findByQuery(query).getBody();
    }


    @RequestMapping(value = "/prestadorServicos_por_cpfCnpj/{cpfCnpj}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServicoNfseDTO> getPorCpfCnpj(@PathVariable String cpfCnpj) {
        log.debug("REST request to get PrestadorServicos Repo: {}", cpfCnpj);
        try {
            return new ResponseEntity<>(cadastroEconomicoService.findByCnpj(cpfCnpj), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erro ao recuperar prestador", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/buscar_prestadorServicos_por_cpfCnpj/{cpfCnpj}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrestadorServicoNfseDTO>> buscarPorCpfCnpj(@PathVariable String cpfCnpj) {
        log.debug("REST request to get PrestadorServicos Repo: {}", cpfCnpj);
        try {
            return new ResponseEntity<>(cadastroEconomicoService.findByCnpjAtivo(cpfCnpj), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erro ao recuperar prestador", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/prestadorServicos_receita_por_cpfCnpj/{cpfCnpj}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServicoNfseDTO> getReceitaPorCpfCnpj(@PathVariable String cpfCnpj) {
        log.debug("REST request to get PrestadorServicos Repo: {}", cpfCnpj);
        try {
            return cadastroEconomicoService.findReceitaByCnpj(cpfCnpj);
        } catch (Exception e) {
            log.error("Erro ao recuperar prestador na recei", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * GET  /prestadorServicoss/:id -> get the "id" prestadorServicos.
     */
    @RequestMapping(value = "/servicos-prestador",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ServicoNfseDTO>> getServicosDoPrestador(@RequestHeader Long prestador) {
        log.debug("REST request to get PrestadorServicos : {}", prestador);
        ResponseEntity<PrestadorServicoNfseDTO> one = cadastroEconomicoService.findOne(prestador);
        return new ResponseEntity<>(one.getBody().getServicos(), HttpStatus.OK);
    }


    @RequestMapping(value = "/prestador-servico/salvar-novo",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServicoNfseDTO> salvarPrestador(@RequestBody PrestadorServicoNfseDTO prestador) throws URISyntaxException {
        log.debug("REST request to save prestador-servico : {}", prestador);
        if (prestador.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prestador-servico cannot already have an ID").body(null);
        }
        prestador.validarCampos();
        UserNfseDTO user = userService.getUserWithAuthorities();
        if (user != null) {
            prestador.getUsuarios().add(user);
        }
        PrestadorServicoNfseDTO result = cadastroEconomicoService.salvarPrestador(prestador);
        return ResponseEntity.ok().body(result);
    }


    @RequestMapping(value = "/credenciamento/imprime/{id}",
            method = RequestMethod.GET)
    @Timed
    public void imprimeCredenciamento(@PathVariable Long id, HttpServletResponse response,
                                      HttpServletRequest request) throws IOException {
//        PrestadorServicos prestadorServicos = prestadorServicosService.findOne(id);
//        prestadorServicosService.imprimeCredenciamento(response, prestadorServicos);
    }

    @RequestMapping(value = "/prestador-servico/remove-usuario-empresa/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void removerUsuarioEmpresa(@PathVariable Long id) {
        userService.removerUserNfseCadastroEconomico(id);
    }

    @RequestMapping(value = "/prestador-servico/solicitacao-cadastro-usuario",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void enviarEmail(@RequestBody ConviteUsuarioNfseDTO convite) {
        cadastroEconomicoService.convidarUsuario(convite);
    }

    @RequestMapping(value = "/prestador-servico/vincular-usuario-empresa",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void saveUsuarioEmpresa(@RequestBody PrestadorServicoNfseDTO prestador) {
        cadastroEconomicoService.saveUsuarioEmpresa(prestador.getId(), SecurityUtils.getCurrentLogin());
    }

    @RequestMapping(value = "/prestador-servico/vincular-novo-usuario-empresa/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void saveUsuarioEmpresa(@PathVariable String login, @RequestHeader Long prestador) {
        cadastroEconomicoService.saveUsuarioEmpresa(prestador, login);
    }

    @RequestMapping(value = "/prestador-servico/atualizar-prestador-usuario",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void atualizarPrestadorUsuario(@RequestBody PrestadorUsuarioNfseDTO prestadorUsuario) {
        userService.atualizarPrestadorUsuario(prestadorUsuario);
    }

    @RequestMapping(value = "/prestador-servico/ativar-desativar-usuario/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void ativarOuDesativarUsuarioEmpresa(@PathVariable String login, @RequestHeader Long prestador) {
        cadastroEconomicoService.ativarOuDesativarUsuarioEmpresa(prestador, login, SecurityUtils.getCurrentLogin());
    }

    @RequestMapping(value = "/prestador-servico/usuarios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrestadorUsuarioNfseDTO>> getUsuariosDoPrestador(@RequestHeader Long prestador) {
        log.debug("REST request to get PrestadorServicos : {}", prestador);
        return new ResponseEntity<>(cadastroEconomicoService.buscarUsuariosPrestador(prestador, null), HttpStatus.OK);
    }

    @RequestMapping(value = "/prestador-servico/usuarios-inativos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrestadorUsuarioNfseDTO>> getUsuariosInativosDoPrestador(@RequestHeader Long prestador) {
        log.debug("REST request to get PrestadorServicos : {}", prestador);
        return new ResponseEntity<>(cadastroEconomicoService.buscarUsuariosInativosPrestador(prestador), HttpStatus.OK);
    }

    @RequestMapping(value = "/prestador-servico/por-login/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PrestadorServicoNfseDTO>> getUsuariosDoPrestador(@RequestParam(value = "page", required = false) Integer offset,
                                                                                @RequestParam(value = "per_page", required = false) Integer limit,
                                                                                @RequestParam(value = "filtro", required = false) String filtro,
                                                                                @PathVariable String login) throws URISyntaxException {
        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        Page<PrestadorServicoNfseDTO> cadastros = cadastroEconomicoService.buscarPrestadoresUsuario(userService.findByLogin(login), pageable, filtro);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(cadastros, "", pageable.getPageNumber(), pageable.getPageSize());
        return new ResponseEntity<>(cadastros.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/prestador-servico/trocar-empresa",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void trocarEmpresa(@RequestParam(value = "prestadorId") Long prestadorId) {
        cadastroEconomicoService.trocarEmpresa(prestadorId);
    }

    @RequestMapping(value = "/anexos-lei-123-2006",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AnexoLei1232006NfseDTO>> buscarAnexosLei1232006() {
        return ResponseEntity.ok(cadastroEconomicoService.buscarAnexosLei1232006());
    }

    @RequestMapping(value = "/prestador-servico/tributosFederais",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void salvarTributosFederais(@RequestBody TributosFederaisNfseDTO tributosFederaisNfse) {
        cadastroEconomicoService.salvarTributosFederais(tributosFederaisNfse);
    }

    @RequestMapping(value = "/cadastro-economico/buscar-tributos-federais",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TributosFederaisNfseDTO> getTributosFederais(@RequestParam(value = "prestadorId") Long prestadorId) {
        try {
            return ResponseEntity.ok(cadastroEconomicoService.buscarTributosFederais(prestadorId));
        } catch (Exception e) {
            log.error("Erro ao recuperar prestador", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/prestador-servico/criar-usuario-empresa",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void criarUsuarioParaEmpresa(@RequestBody PrestadorServicoNfseDTO prestador) {
        cadastroEconomicoService.criarUsuarioParaEmpresa(prestador);
    }

    @RequestMapping(value = "/publico/prestador-servico/remove-mongo/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void removerMongo(@PathVariable Long id) {
        cadastroEconomicoService.removeOnMongo(id);
    }


    @RequestMapping(value = "/prestador-servico/get-rbt12-retencao",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ValueNfseDTO> getRBT12ParaRetencao(@RequestHeader Long prestador) {
        BigDecimal rbt12 = cadastroEconomicoService.getRBT12ParaRetencao(prestador);
        return ResponseEntity.ok(new ValueNfseDTO(rbt12));
    }
}
