package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.WebReportDTO;
import br.com.webpublico.service.WebReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/spring/report")
public class WebReportResource {

    private static final Logger log = LoggerFactory.getLogger(WebReportResource.class);

    @Autowired
    private WebReportService service;


    @RequestMapping(value = "/porcentagem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void publicar(@RequestParam("uuid") String uuid, @RequestParam("porcentagem") BigDecimal porcentagem) {
        log.debug("REST porcentagem .....");
        try {
            service.porcentagemRelatorio(uuid, porcentagem);
        } catch (Exception e) {
            log.error("Erro ao atribuir a porcentagem do relatório ", e.getMessage());
        }
    }

    @RequestMapping(value = "/publicar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void publicar(@RequestParam("uuid") String uuid) {
        log.debug("REST imprimir .....");
        try {
            service.publicarRelatorio(uuid);
        } catch (Exception e) {
            log.error("Erro ao publicar o relatório ", e);
        }
    }

    @RequestMapping(value = "/get-by-uuid", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebReportDTO> getByUuid(@RequestParam("uuid") String uuid) {
        return ResponseEntity.ok(service.getByUuid(uuid));
    }

    @RequestMapping(value = "/imprimir/{uuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void imprimir(HttpServletResponse response, @PathVariable String uuid) throws IOException {
        service.imprimir(response, uuid);
    }

}
