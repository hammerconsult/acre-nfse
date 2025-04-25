package br.com.webpublico.web.rest;

import br.com.webpublico.domain.dto.ArquivoBase64DTO;
import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.service.ConfiguracaoService;
import br.com.webpublico.web.rest.dto.ExportarDTO;
import com.google.gson.Gson;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/api")
public class ExportarResource {

    private final Logger log = LoggerFactory.getLogger(ExportarResource.class);

    @Autowired
    private ConfiguracaoService configuracaoService;

    @RequestMapping(value = "/exportar/xls",
            method = RequestMethod.POST)
    @Timed
    public ResponseEntity<ArquivoBase64DTO> baixarXls(
            @RequestBody ExportarDTO exportarXlsDTO) {
        try {
            return new ResponseEntity<>(gerarXls(exportarXlsDTO), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/exportar/pdf",
            method = RequestMethod.POST)
    @Timed
    public void baixarPdf(HttpServletResponse response,
                          @RequestBody ExportarDTO exportarDTO) {
        try {
            configuracaoService.imprimirPdf(response, gerarConteudoPdf(exportarDTO));
        } catch (JSONException e) {
            log.error("Erro ", e);
        }
    }

    private ArquivoBase64DTO gerarXls(ExportarDTO exportarXlsDTO) throws IOException, JSONException {

        String json = new Gson().toJson(exportarXlsDTO.getLinhas());
        json = "{\"infile\":" + json + "}";
        JSONObject output;

        output = new JSONObject(json);

        JSONArray docs = output.getJSONArray("infile");

        File file = File.createTempFile("tempfile", ".csv");
        String csv = CDL.toString(docs).replace(",", ";");
        FileUtils.writeStringToFile(file, csv);
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        String base64 = new String(encoded, StandardCharsets.UTF_8);
        return new ArquivoBase64DTO(base64);
    }

    public String gerarConteudoPdf(ExportarDTO dto) throws JSONException {
        ConfiguracaoNfseDTO configuracaoNfseDTO = configuracaoService.getConfiguracaoFromServer();
        String json = new Gson().toJson(dto.getLinhas());
        json = "{\"infile\":" + json + "}";
        JSONObject output;
        output = new JSONObject(json);
        JSONArray docs = output.getJSONArray("infile");

        StringBuilder tabela = new StringBuilder();
        tabela.append("<table class=\"table\">").append("<tr>");
        for (ExportarDTO.Coluna coluna : dto.getColunas()) {
            tabela.append("<th>").append(coluna.getDescricao()).append("</th>");
        }
        tabela.append("</tr>");

        for (int i = 0; i < docs.length(); i++) {
            JSONObject o = (JSONObject) docs.get(i);
            tabela.append("<tr>");
            for (ExportarDTO.Coluna coluna : dto.getColunas()) {
                if (!o.isNull(coluna.getValor())) {
                    Object valor = o.get(coluna.getValor());
                    if (valor instanceof Boolean)
                        valor = (boolean) valor ? "Sim" : "Não";
                    tabela.append("<td>").append(valor).append("</td>");
                } else {
                    tabela.append("<td>").append("</td>");
                }
            }
            log.debug("o > {}", o);
            tabela.append("</tr>");
        }

        String content = "<html> " +
                "<style> " +
                "    .table { " +
                "        font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif; " +
                "        border-collapse: collapse; " +
                "        width: 100%; padding: 0px; " +
                "    } " +
                " " +
                "    .table td, .table th { " +
                "        border: 1px solid #ddd; " +
                "        padding: 5px; " +
                "    } " +
                " " +
                "    .table th { " +
                "        padding-top: 12px; " +
                "        padding-bottom: 12px; " +
                "        text-align: left; " +
                "        background-color: #e1e1e1; " +
                "    } " +
                "</style> " +
                "<body> " +
                "<table class=\"table\"> " +
                "    <tbody> " +
                "    <tr> " +
                "        <td style=\"width:90px;\"> " +
                "           <img src=\"" + configuracaoNfseDTO.getLogoMunicipio() + "\" style=\"width: 90px !important; height: 100px !important;\"/>" +
                "        </td> " +
                "        <td> " +
                "           " + configuracaoNfseDTO.getMunicipio().getNome() + "</br>" +
                "           " + configuracaoNfseDTO.getSecretaria() + "</br>" +
                "           " + configuracaoNfseDTO.getDepartamento() + "</br>" +
                "        </td> " +
                "    </tr> " +
                "    </tbody> " +
                "</table> " +
                tabela +
                "</body> " +
                "</html> ";
        return content;
    }

}
