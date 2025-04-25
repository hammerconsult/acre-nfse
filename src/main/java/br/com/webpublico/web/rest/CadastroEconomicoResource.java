package br.com.webpublico.web.rest;

import br.com.webpublico.service.CadastroEconomicoService;
import br.com.webpublico.web.rest.dto.SubstitutoTributarioDTO;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/publico/cadastro-economico")
public class CadastroEconomicoResource {

    @Autowired
    private CadastroEconomicoService service;

    @RequestMapping(value = "/consultar-substitutos-tributarios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SubstitutoTributarioDTO>> buscarSubstitutosTributarios(
            @RequestParam(value = "cpfCnpj", required = false) String cpfCnpj,
            @RequestParam(value = "inscricao", required = false) String inscricao,
            @RequestParam(value = "nome_razaosocial", required = false) String nome_razaosocial) {
        return new ResponseEntity<>(service.buscarSubstitutosTributarios(cpfCnpj, inscricao, nome_razaosocial), HttpStatus.OK);
    }

}
