package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.importacaodesif.PlanoGeralContasComentadoNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.PlanoGeralContasComentadoService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/plano-geral-contas-comentado")
public class PlanoGeralContasComentadoResource extends AbstractResource<PlanoGeralContasComentadoNfseDTO> {

    @Inject
    private PlanoGeralContasComentadoService service;

    @Override
    protected AbstractService getService() {
        return service;
    }

    @GetMapping(value = "/buscar-por-conta",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanoGeralContasComentadoNfseDTO> buscarPorConta(@RequestHeader Long prestador,
                                                                           @RequestParam String conta) throws Exception {
        PlanoGeralContasComentadoNfseDTO byConta = service.findByContaAndDesdobramentoAndReferencia(prestador, conta, null, new Date());
        return new ResponseEntity<>(byConta, HttpStatus.OK);
    }

    @GetMapping(value = "/buscar-contas-tributaveis-nao-declaradas",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PlanoGeralContasComentadoNfseDTO>> buscarContasTributaveisNaoDeclaradas(@RequestHeader Long prestador,
                                                                                                       @RequestParam Long arquivo) {
        return new ResponseEntity<>(service.buscarContasTributaveisNaoDeclaradas(prestador, arquivo), HttpStatus.OK);
    }

}
