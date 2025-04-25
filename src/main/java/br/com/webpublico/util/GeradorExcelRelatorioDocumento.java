package br.com.webpublico.util;

import br.com.webpublico.domain.dto.RelatorioDocumentoDTO;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class GeradorExcelRelatorioDocumento implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(GeradorExcelRelatorioDocumento.class);
    private ExcelUtil excelUtil;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Integer linha;
    private Integer coluna;
    private String logoMunicipio;
    private String nomePlanilha;
    private String titulo;
    private String contribuinte;
    private String filtrosUtilizados;
    private List<RelatorioDocumentoDTO> registros;


    public GeradorExcelRelatorioDocumento(String logoMunicipio,
                                          String nomePlanilha,
                                          String titulo,
                                          String contribuinte,
                                          String filtrosUtilizados,
                                          List<RelatorioDocumentoDTO> registros) {
        this.logoMunicipio = logoMunicipio;
        this.nomePlanilha = nomePlanilha;
        this.titulo = titulo;
        this.contribuinte = contribuinte;
        this.filtrosUtilizados = filtrosUtilizados;
        this.registros = registros;
        this.excelUtil = new ExcelUtil();
        this.linha = 0;
        this.coluna = 0;
    }

    public File gerar() {
        try {
            String nomeArquivo = nomePlanilha + System.currentTimeMillis() + "_temp";
            File file = File.createTempFile(nomeArquivo, ExcelUtil.XLSX_EXTENCAO);
            FileOutputStream fout = new FileOutputStream(file);
            workbook = new XSSFWorkbook();
            criarPlanilha();
            workbook.write(fout);
            return file;
        } catch (Exception e) {
            logger.error("Erro ao gerar excel {}", e);
        }

        return null;
    }

    private Integer criarCabecalhoPlanilha() throws IOException {

        InputStream inputStream = new Util().base64ToInputStream(logoMunicipio);
        int idLogo = workbook.addPicture(inputStream, Workbook.PICTURE_TYPE_PNG);
        XSSFClientAnchor anchor = new XSSFClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(0);
        anchor.setCol2(2);
        anchor.setRow2(4);
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        drawing.createPicture(anchor, idLogo);

        XSSFRow municipio = excelUtil.criarRow(sheet, 1, excelUtil.styleFonteArial10(workbook));
        excelUtil.criarCell(workbook, municipio, 3, excelUtil.styleFonteArial10Negrito(workbook)).setCellValue(ExcelUtil.MUNICIPIO);

        linha = 4;
        coluna = 3;
        XSSFRow rowTitulo = excelUtil.criarRow(sheet, linha++, excelUtil.styleFonteArial10(workbook));
        excelUtil.criarCell(workbook, rowTitulo, coluna++, excelUtil.styleFonteArial10Negrito(workbook)).setCellValue(titulo);

        coluna = 0;
        XSSFRow rowContribuinte = excelUtil.criarRow(sheet, linha++, excelUtil.styleFonteArial10(workbook));
        excelUtil.criarCell(workbook, rowContribuinte, coluna++, excelUtil.styleFonteArial10Negrito(workbook)).setCellValue("Contribuinte:");
        excelUtil.criarCell(workbook, rowContribuinte, coluna++, excelUtil.styleFonteArial10(workbook)).setCellValue(contribuinte);

        coluna = 0;
        XSSFRow rowFiltrosUtilizados = excelUtil.criarRow(sheet, linha++, excelUtil.styleFonteArial10(workbook));
        excelUtil.criarCell(workbook, rowFiltrosUtilizados, coluna++, excelUtil.styleFonteArial10Negrito(workbook)).setCellValue("Filtros Utilizados:");
        excelUtil.criarCell(workbook, rowFiltrosUtilizados, coluna++, excelUtil.styleFonteArial10(workbook)).setCellValue(filtrosUtilizados);

        linha++;

        return linha;
    }

    private void criarPlanilha() throws IOException {
        sheet = excelUtil.criarSheet(workbook, nomePlanilha);
        linha = criarCabecalhoPlanilha();

        coluna = 0;
        XSSFRow rowHeader = excelUtil.criarRow(sheet, linha++, excelUtil.styleFonteArial10Negrito(workbook));
        excelUtil.criaCell(rowHeader, coluna++).setCellValue("Número da NFS-e");
        excelUtil.criaCell(rowHeader, coluna++).setCellValue("CPF/CNPJ");
        excelUtil.criaCell(rowHeader, coluna++).setCellValue("Prestador/Tomador");
        excelUtil.criaCell(rowHeader, coluna++).setCellValue("Emissão");
        excelUtil.criaCell(rowHeader, coluna++).setCellValue("Situação");
        excelUtil.criaCell(rowHeader, coluna++).setCellValue("Operação");
        excelUtil.criaCell(rowHeader, coluna++).setCellValue("Valor do Serviço");
        excelUtil.criaCell(rowHeader, coluna++).setCellValue("Valor do ISSQN");

        for (RelatorioDocumentoDTO registro : registros) {
            coluna = 0;
            XSSFRow rowNota = excelUtil.criarRow(sheet, linha++, excelUtil.styleFonteArial10(workbook));
            excelUtil.criaCell(rowNota, coluna++).setCellValue(registro.getNumeroNfse());
            excelUtil.criaCell(rowNota, coluna++).setCellValue(registro.getCpfCnpj());
            excelUtil.criaCell(rowNota, coluna++).setCellValue(registro.getNomeRazaoSocial());
            excelUtil.criarCellDate(workbook, rowNota, coluna++, excelUtil.styleFonteArial10(workbook), registro.getEmissao());
            excelUtil.criaCell(rowNota, coluna++).setCellValue(registro.getSituacao());
            excelUtil.criaCell(rowNota, coluna++).setCellValue(registro.getOperacao());
            excelUtil.criarCellMoeda(workbook, rowNota, coluna++, excelUtil.styleFonteArial10(workbook), registro.getValorServico());
            excelUtil.criarCellMoeda(workbook, rowNota, coluna++, excelUtil.styleFonteArial10(workbook), registro.getValorIss());
        }
    }
}
