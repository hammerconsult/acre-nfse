package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ImagemUsuarioNfseDTO;
import br.com.webpublico.security.SecurityUtils;
import br.com.webpublico.service.CadastroEconomicoService;
import br.com.webpublico.service.ImagemService;
import br.com.webpublico.service.PessoaService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class ImagemResource {

    private final Logger log = LoggerFactory.getLogger(ImagemResource.class);
    private final ImagemService imagemService;
    private final CadastroEconomicoService cadastroEconomicoService;
    private final PessoaService pessoaService;

    public ImagemResource(ImagemService imagemService,
                          CadastroEconomicoService cadastroEconomicoService,
                          PessoaService pessoaService) {
        this.imagemService = imagemService;
        this.cadastroEconomicoService = cadastroEconomicoService;
        this.pessoaService = pessoaService;
    }

    @RequestMapping(value = "/imagem",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> updateImagem(@RequestBody ImagemUsuarioNfseDTO imagemDTO) throws URISyntaxException {
        try {
            pessoaService.updateImagemPessoa(imagemDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Ocorreu um erro ao inserir uma logo: {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/imagem-pessoa/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImagemUsuarioNfseDTO> getImagemFromPessoa(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaService.getImagemPessoa(id));

    }

    @RequestMapping(value = "/imagem-prestador/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImagemUsuarioNfseDTO> getImagemFromPrestadorServicos(@PathVariable Long id) {
        return ResponseEntity.ok(cadastroEconomicoService.getImagemPrestador(id));

    }


    @RequestMapping(value = "/imagem-usuario",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImagemUsuarioNfseDTO> getImagemFromuser() {
        return ResponseEntity.ok(imagemService.getImagemUsuario(SecurityUtils.getCurrentLogin()));
    }

    @RequestMapping(value = "/imagem-usuario/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void deleteImagemFromuser(@PathVariable Long id) {
        imagemService.deleteImage(id);
    }

    @RequestMapping(value = "/imagem-usuario",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> updateImagemUsuario(@RequestBody ImagemUsuarioNfseDTO imagemDTO) throws URISyntaxException {
        try {
            imagemService.updateImagemUsuario(imagemDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Ocorreu um erro ao inserir uma logo: {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
