package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ArquivoNfseDTO;
import br.com.webpublico.service.ArquivoService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ArquivoResource {

    @Autowired
    private ArquivoService service;

    @GetMapping("/arquivo/{id}")
    @Timed
    public HttpEntity<byte[]> downloadArquivo(@PathVariable Long id) {
        ArquivoNfseDTO arquivo = service.findById(id);
        byte[] byteFile = service.montarArquivoParaDownload(arquivo);
        String fileName = arquivo.getNome() != null && arquivo.getNome().contains(".") ?
                arquivo.getNome() : arquivo.getNome()
                + service.getExtension(arquivo.getMimeType());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return new HttpEntity<>(byteFile, httpHeaders);

    }
}
