package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.DeclaracaoMensalServicoNfseDTO;
import br.com.webpublico.domain.dto.desif.UploadCosifDTO;
import br.com.webpublico.domain.dto.importacaodesif.ArquivoDesifNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.ImportacaoArquivoDesifNfseDTO;
import br.com.webpublico.domain.dto.importacaodesif.SituacaoArquivoDesifNfseDTO;
import br.com.webpublico.service.AbstractService;
import br.com.webpublico.service.ArquivoDesifService;
import io.micrometer.core.annotation.Timed;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/arquivo-desif")
public class ArquivoDesifResource extends AbstractResource<ArquivoDesifNfseDTO> {

    @Autowired
    private ArquivoDesifService service;

    @Override
    protected AbstractService getService() {
        return service;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping(value = "/validar")
    public ResponseEntity<ImportacaoArquivoDesifNfseDTO> validar(@RequestBody ImportacaoArquivoDesifNfseDTO dto) {
        return ResponseEntity.ok(service.validarImportacaoArquivoDesif(dto));
    }

    @GetMapping(value = "/enviar")
    public ResponseEntity<Void> enviar(@RequestParam Long id) {
        ArquivoDesifNfseDTO arquivoDesif = service.findById(id);
        service.processarArquivoDesif(arquivoDesif);
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "/consultar-situacao")
    public ResponseEntity<SituacaoArquivoDesifNfseDTO> consultarSituacao(@RequestParam Long id) {
        ArquivoDesifNfseDTO arquivoDesif = service.findById(id);
        return ResponseEntity.ok(arquivoDesif.getSituacao());
    }

    @RequestMapping(value = "/importar-legado",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeclaracaoMensalServicoNfseDTO> importarDesifLegado(@RequestBody UploadCosifDTO dto) throws Exception {
        log.debug("REST request to import arquivo cosif : {}", dto);
        return ResponseEntity.ok(service.importarArquivoDesifLegado(dto));
    }

    @RequestMapping(value = "/imprimir-recibo/{id}",
            method = RequestMethod.GET)
    @Timed
    public void imprimirRecibo(HttpServletResponse response, @PathVariable Long id) throws JRException, IOException {
        service.imprimirRecibo(response, id);
    }
}
