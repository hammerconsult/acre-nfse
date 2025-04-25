package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.IntegerNfseDTO;
import br.com.webpublico.domain.dto.MensagemContribuinteUsuarioNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.MensagemContribuinteUsuarioService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/mensagem-contribuinte-usuario")
public class MensagemContribuinteUsuarioResource extends AbstractResource<MensagemContribuinteUsuarioNfseDTO> {

    @Autowired
    private MensagemContribuinteUsuarioService service;

    @Override
    protected AbstractService getService() {
        return service;
    }

    @GetMapping(value = "/count-nao-lida-no-prazo")
    @Timed
    public ResponseEntity<IntegerNfseDTO> countMensagensNaoLidaNoPrazo(@RequestHeader Long prestador) {
        Integer count = service.countMensagensNaoLidaNoPrazo(prestador);
        return new ResponseEntity<>(new IntegerNfseDTO(count), HttpStatus.OK);
    }

    @GetMapping("/proxima-nao-lida")
    @Timed
    public ResponseEntity<MensagemContribuinteUsuarioNfseDTO> buscarProximaMensagemNaoLida(@RequestHeader Long prestador) throws Exception {
        return ResponseEntity.ok(service.buscarProximaMensagemNaoLida(prestador));
    }

    @PostMapping("/marcar-como-lida")
    @Timed
    public void marcarMensagemComoLida(@RequestBody MensagemContribuinteUsuarioNfseDTO mensagemContribuinteUsuario) throws IOException {
        service.marcarMensagemComoLida(mensagemContribuinteUsuario);
    }
}
