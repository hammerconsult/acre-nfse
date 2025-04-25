package br.com.webpublico.service;

import br.com.webpublico.domain.dto.ConfiguracaoNfseDTO;
import br.com.webpublico.security.SecurityUtils;
import br.com.webpublico.util.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ConfiguracaoService configuracaoService;

    private Map<String, JasperReport> japers = Maps.newHashMap();

    public JasperReport compilarJrxml(String jrxml) throws JRException {
        if (japers.get(jrxml) != null) {
            return japers.get(jrxml);
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(NotaFiscalService.class.getResourceAsStream("/report/" + jrxml));
        japers.put(jrxml, jasperReport);
        return jasperReport;
    }

    public JasperPrint gerarJasper(String jrxml, HashMap<String, Object> parametros, List dados) throws JRException {
        return JasperFillManager.fillReport(compilarJrxml(jrxml), parametros,
                new JRBeanCollectionDataSource(dados));
    }

    public byte[] gerarRelatorio(String jrxml,
                                 HashMap<String, Object> parametros,
                                 List dados,
                                 boolean excel) throws JRException, IOException {
        if (excel) {
            return gerarRelatorioXls(jrxml, parametros, dados);
        } else {
            return gerarRelatorioPdf(jrxml, parametros, dados);
        }
    }

    public byte[] gerarRelatorioXls(String jrxl,
                                    HashMap<String, Object> parametros,
                                    List dados) throws JRException, IOException {

        JasperPrint jasperPrint = gerarJasper(jrxl, parametros, dados);

        ByteArrayOutputStream streamDados = new ByteArrayOutputStream();

        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, streamDados);
        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, Integer.decode("65000"));
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
        exporter.exportReport();

        streamDados.flush();
        streamDados.close();

        return streamDados.toByteArray();
    }

    public byte[] gerarRelatorioPdf(String jrxl,
                                    HashMap<String, Object> parametros,
                                    List dados) throws JRException {
        JasperPrint jasperPrint = gerarJasper(jrxl, parametros, dados);

        ByteArrayOutputStream dadosByte = new ByteArrayOutputStream();

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, Lists.newArrayList(jasperPrint));
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, dadosByte);
        exporter.exportReport();

        return dadosByte.toByteArray();
    }

    public void imprimirRelatorio(HttpServletResponse response, byte[] bytes, String fileName) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + fileName + ".pdf");
        response.setContentLength(bytes.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes, 0, bytes.length);
        outputStream.flush();
        outputStream.close();
    }

    public void parametrosDefault(HashMap<String, Object> parametros) {
        ConfiguracaoNfseDTO configuracaoFromServer = configuracaoService.getConfiguracaoFromServer();
        parametros.put("IMAGEM", Util.base64ToInputStream(configuracaoFromServer.getLogoMunicipio()));
        parametros.put("MUNICIPIO", "Prefeitura do Município de Rio Branco");
        parametros.put("SECRETARIA", "Secretaria Municipal de Finanças");
        parametros.put("DEPARTAMENTO", "Departamento de Tributação");
        parametros.put("ENDERECO", "Rua Rui Barbosa, 285 – Centro - Rio Branco/AC - CEP: 69.900-901 - Tel.: (68) 3212-7042");
        parametros.put("URLSITE", "https://nota.riobranco.ac.gov.br");
        parametros.put("USUARIO", SecurityUtils.getCurrentLogin());
        parametros.put("HOMOLOGACAO", !configuracaoFromServer.getProducao());
    }
}
