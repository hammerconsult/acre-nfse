package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.exception.NfseOperacaoNaoPermitidaException;
import br.com.webpublico.service.CadastroEconomicoService;
import br.com.webpublico.service.DamService;
import br.com.webpublico.service.DeclaracaoMensalServicoService;
import br.com.webpublico.tributario.consultadebitos.ResultadoParcela;
import br.com.webpublico.tributario.consultadebitos.dtos.DamDTO;
import br.com.webpublico.tributario.consultadebitos.dtos.ParcelaDTO;
import br.com.webpublico.web.rest.dto.AgrupadorParcelas;
import br.com.webpublico.web.rest.dto.FiltroParcelaDTO;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PrestadorServicos.
 */
@RestController
@RequestMapping("/api")
public class DebitosResource {

    private final Logger log = LoggerFactory.getLogger(DebitosResource.class);

    @Inject
    private CadastroEconomicoService cadastroEconomicoService;
    @Inject
    private DeclaracaoMensalServicoService declaracaoMensalServicoService;
    @Inject
    private DamService damService;


    @RequestMapping(value = "/debitos-prestador",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ResultadoParcela> getDebitosPrestador(@RequestBody FiltroParcelaDTO filtroParcelaDTO, @RequestHeader Long prestador) {
        log.debug("REST request to get PrestadorServicos : {} {}", prestador, filtroParcelaDTO);
        return cadastroEconomicoService.buscarDebitosDoPrestador(prestador, filtroParcelaDTO);
    }

    @RequestMapping(value = "/debito-prestador",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParcelaDTO> getDebitosPrestador(@RequestParam Long idParcela) {
        return new ResponseEntity(cadastroEconomicoService.getDebito(idParcela), HttpStatus.OK);
    }

    @RequestMapping(value = "/dam-parcela",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DamDTO> getDamParcela(@RequestBody AgrupadorParcelas agrupador) {
        log.debug("REST request to get DAM parcela: {}", agrupador);
        Optional<ParcelaDTO> first = agrupador.getParcelas().stream().filter(
                ParcelaDTO::getBloqueiaImpressao
        ).findFirst();
        if(first.isPresent()){
            throw  new NfseOperacaoNaoPermitidaException(first.get().getDivida() + " Bloqueada para impressão de guia");
        }
        return new ResponseEntity<DamDTO>(cadastroEconomicoService.buscarDamParcela(agrupador), HttpStatus.OK);
    }

    @RequestMapping(value = "/debito/buscar-ultimo-dam-parcela",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DamDTO> buscarUltimoDamParcela(@RequestParam Long idParcela) {
        log.debug("REST request to get buscarUltimoDamParcela: {}", idParcela);
        return new ResponseEntity<DamDTO>(damService.buscarUltimoDamParcela(idParcela), HttpStatus.OK);
    }

    @RequestMapping(value = "/imprimir-dam",
            method = RequestMethod.POST)
    @Timed
    public void imprimirDam(@RequestBody List<Long> idsParcelas, HttpServletResponse response) {
        log.debug("REST request to idsParcelas: {}", idsParcelas);
        damService.imprimirDam(idsParcelas, response);
    }

    @RequestMapping(value = "/enviar-dam",
            method = RequestMethod.POST)
    @Timed
    public void getEnviarDam(@RequestBody AgrupadorParcelas agrupador) {
        log.debug("REST request to get DAM parcela: {}", agrupador);
        cadastroEconomicoService.enviarDam(agrupador);
    }

    @RequestMapping(value = "/imprimir-dam-declaracao/{id}",
            method = RequestMethod.GET)
    @Timed
    public void imprimirDamDeclaracao(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get IdDeclaracao : {}", id);
        declaracaoMensalServicoService.imprimirDam(response, id);
    }


}
