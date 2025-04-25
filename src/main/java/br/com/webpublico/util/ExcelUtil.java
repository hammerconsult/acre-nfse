package br.com.webpublico.util;

import br.com.webpublico.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    public static final String XLS_CONTENTTYPE = "application/xls";
    public static final String XLS_EXTENCAO = ".xls";
    public static final String XLSX_CONTENTTYPE = "application/xlsx";
    public static final String XLSX_EXTENCAO = ".xlsx";
    public static final String CSV_CONTENTTYPE = "application/csv";
    public static final String CSV_EXTENCAO = ".csv";
    public static final String MUNICIPIO = "Prefeitura do Município de Rio Branco";
    public static final String WEBPUBLICO = "WebPúblico - Sistema Integrado de Gestão Pública";
    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private File file = null;
    private String nomeArquivo;
    private String contentType;
    private String extensao;
    private Boolean ajustarTamanhoColuna;

    public Boolean getAjustarTamanhoColuna() {
        return ajustarTamanhoColuna != null ? ajustarTamanhoColuna : true;
    }

    public void setAjustarTamanhoColuna(Boolean ajustarTamanhoColuna) {
        this.ajustarTamanhoColuna = ajustarTamanhoColuna;
    }

    public static String getValorCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (CellType.NUMERIC == cell.getCellType()) {
            BigDecimal valor = BigDecimal.valueOf(cell.getNumericCellValue());
            return valor + "";
        } else if (CellType.STRING == cell.getCellType()) {
            return cell.getStringCellValue();
        } else if (CellType.BOOLEAN == cell.getCellType()) {
            return cell.getBooleanCellValue() ? "Sim " : "Não";
        } else if (CellType.FORMULA == cell.getCellType()) {
            if (cell.getCachedFormulaResultType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            } else {
                return "";
            }
        } else if (CellType.ERROR == cell.getCellType()) {
            return "error";
        } else if (CellType.BLANK == cell.getCellType()) {
            return "";
        } else {
            return "";

        }
    }

    public void gerarExcelXLSX(String titulo, String nomeDoArquivo, List<String> titulos, List<Object[]> objetos, String filtros, Boolean ajustarColuna) throws IOException {
        this.contentType = XLSX_CONTENTTYPE;
        this.extensao = XLSX_EXTENCAO;
        this.ajustarTamanhoColuna = ajustarColuna;
        gerarExcel(titulo, nomeDoArquivo, titulos, objetos, filtros);
    }

    public void gerarExcel(String titulo, String nomeDoArquivo, List<String> titulos, List<Object[]> objetos, String filtros, Boolean ajustarColuna) throws IOException {
        this.ajustarTamanhoColuna = ajustarColuna;
        gerarExcel(titulo, nomeDoArquivo, titulos, objetos, filtros);
    }

    public void gerarExcel(String titulo, String nomeDoArquivo, List<String> titulos, List<Object[]> objetos, String filtros) throws IOException {
        if (contentType == null) {
            contentType = XLS_CONTENTTYPE;
            extensao = XLS_EXTENCAO;
        }
        nomeArquivo = nomeDoArquivo;
        String nomePlanilha = nomeDoArquivo + "_temp";
        file = File.createTempFile(nomePlanilha, extensao);

        int linhaInicial = 0;

        FileOutputStream fout = new FileOutputStream(file);


        XSSFWorkbook pastaDeTrabalho = new XSSFWorkbook();
        XSSFSheet sheet = criarSheet(pastaDeTrabalho, titulo);


        XSSFRow municipio = criaRow(sheet, linhaInicial);
        criaCell(municipio, linhaInicial).setCellValue(MUNICIPIO);
        linhaInicial++;

        XSSFRow tituloDaPlanilha = criaRow(sheet, linhaInicial);
        criaCell(tituloDaPlanilha, 0).setCellValue(titulo);
        linhaInicial++;
        if (filtros != null) {
            XSSFRow filtrosUtilizados = criaRow(sheet, linhaInicial);
            criaCell(filtrosUtilizados, 0).setCellValue(filtros.trim());
        }
        linhaInicial++;
        linhaInicial++;


        XSSFRow cabecalho = criaRow(sheet, linhaInicial);
        for (String atributo : titulos) {
            criaCell(cabecalho, titulos.indexOf(atributo)).setCellValue(atributo);
        }
        linhaInicial++;
        for (Object o : objetos) {
            XSSFRow linha = criaRow(sheet, linhaInicial);

            Object[] objeto = (Object[]) o;
            int i = 0;
            for (Object atributo : objeto) {
                if (atributo != null) {
                    if (atributo instanceof BigDecimal) {
                        XSSFCell XSSFCell = criaCell(linha, i);
                        XSSFCell.setCellType(CellType.NUMERIC);
                        XSSFCell.setCellValue(((BigDecimal) atributo).doubleValue());
                    } else {
                        criaCell(linha, i).setCellValue(atributo.toString());
                    }
                }
                i++;
            }
            linhaInicial++;
        }

        if (getAjustarTamanhoColuna()) {
            for (int i = 0; i < titulos.size(); i++) {
                sheet.autoSizeColumn(i);
            }
        }

        linhaInicial = linhaInicial + 5;

        XSSFRow rodape = criaRow(sheet, linhaInicial);
        criaCell(rodape, 1).setCellValue(WEBPUBLICO);

        linhaInicial++;

        XSSFRow geradoEm = criaRow(sheet, linhaInicial);
        criaCell(geradoEm, 1).setCellValue("Gerado em - " + DateUtils.getDataFormatada(new Date()));

        pastaDeTrabalho.write(fout);
    }

    public void gerarCSV(String titulo, String nomeDoArquivo, List<String> titulos, List<Object[]> objetos, boolean gerarCabecalhoAndRodape) {
        gerarCSV(titulo, nomeDoArquivo, titulos, objetos, gerarCabecalhoAndRodape, true, true);
    }

    public void gerarCSV(String titulo, String nomeDoArquivo, List<String> titulos, List<Object[]> objetos, boolean gerarCabecalhoAndRodape, Boolean substituirPontoNoBigDecimal, Boolean adicionarDelimitadorNaUltimaColunaDaLinha) {
        BufferedWriter fileWriter = null;
        try {
            if (contentType == null) {
                contentType = CSV_CONTENTTYPE;
                extensao = CSV_EXTENCAO;
            }
            nomeArquivo = nomeDoArquivo;
            String nomePlanilha = nomeDoArquivo + "_temp";
            file = File.createTempFile(nomePlanilha, extensao);
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "ISO-8859-1"));

            if (gerarCabecalhoAndRodape) {
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(MUNICIPIO);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(titulo);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }


            for (String t : titulos) {
                fileWriter.append(t);

                if (adicionarDelimitadorNaUltimaColunaDaLinha) {
                    fileWriter.append(DELIMITER);
                } else if (!titulos.get(titulos.size() - 1).equals(t)) {
                    fileWriter.append(DELIMITER);
                }
            }
            fileWriter.append(NEW_LINE_SEPARATOR);


            for (Object o : objetos) {
                Object[] objeto = (Object[]) o;
                for (Object atributo : objeto) {
                    if (atributo != null) {
                        if (atributo instanceof BigDecimal && substituirPontoNoBigDecimal) {
                            fileWriter.append(atributo.toString().replace(".", ","));
                        } else {
                            fileWriter.append(atributo.toString());
                        }
                        if (adicionarDelimitadorNaUltimaColunaDaLinha) {
                            fileWriter.append(DELIMITER);
                        } else if (!objeto[objeto.length - 1].equals(atributo)) {
                            fileWriter.append(DELIMITER);
                        }
                    }
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            if (gerarCabecalhoAndRodape) {
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(WEBPUBLICO);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append("Gerado em - " + DateUtils.getDataFormatada(new Date()));
            }
        } catch (Exception e) {
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public XSSFSheet criarSheet(XSSFWorkbook pasta, String nomeSheet) {
        return pasta.createSheet(nomeSheet);
    }

    public XSSFCell criaCell(XSSFRow row, Integer posicao) {
        return row.getCell(posicao) == null ? row.createCell(posicao) : row.getCell(posicao);
    }

    public XSSFCell criarCell(XSSFWorkbook workbook,
                              XSSFRow row,
                              Integer posicao,
                              CellStyle cellStyle) {
        XSSFCell cell = row.getCell(posicao);
        if (cell == null)
            cell = row.createCell(posicao);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    public XSSFCell criaCellDateTime(XSSFWorkbook workbook,
                                     XSSFRow row,
                                     Integer posicao,
                                     CellStyle cellStyle,
                                     Date value) {
        XSSFCell cell = row.getCell(posicao);
        if (cell == null) {
            cell = row.createCell(posicao);
        }
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
        formatCell(workbook, cell, "dd/MM/yyyy hh:mm");
        return cell;
    }

    private void formatCell(XSSFWorkbook workbook, XSSFCell cell, String format) {
        CreationHelper createHelper = workbook.getCreationHelper();
        cell.getCellStyle().setDataFormat(
                createHelper.createDataFormat().getFormat(format));
    }

    public XSSFCell criarCellDate(XSSFWorkbook workbook,
                                  XSSFRow row,
                                  Integer posicao,
                                  CellStyle cellStyle,
                                  Date value) {
        XSSFCell cell = criaCell(row, posicao);


        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
        formatCell(workbook, cell, "dd/MM/yyyy");
        return cell;
    }

    public XSSFCell criarCellMoeda(XSSFWorkbook workbook,
                                   XSSFRow row,
                                   Integer posicao,
                                   CellStyle cellStyle,
                                   BigDecimal value) {
        XSSFCell cell = criaCell(row, posicao);
        cell.setCellValue(value == null ? 0 : value.doubleValue());
        cell.setCellStyle(cellStyle);
        formatCell(workbook, cell, "R$ #,##0.00");
        return cell;
    }

    public XSSFCell criarCellPorcentagem(XSSFWorkbook workbook,
                                         XSSFRow row,
                                         Integer posicao,
                                         CellStyle cellStyle,
                                         BigDecimal value) {
        XSSFCell cell = criaCell(row, posicao);
        cell.setCellValue(value.doubleValue());
        cell.setCellStyle(cellStyle);
        formatCell(workbook, cell, "% #,##0.00");
        return cell;
    }

    private XSSFCell criaCell(XSSFRow row, Integer posicao, XSSFCellStyle estilo) {
        XSSFCell celula = row.getCell(posicao) == null ? row.createCell(posicao) : row.getCell(posicao);
        celula.setCellStyle(estilo);
        return celula;
    }

    public XSSFRow criaRow(XSSFSheet sheet, Integer linha) {
        return sheet.getRow(linha) == null ? sheet.createRow(linha) : sheet.getRow(linha);
    }

    public XSSFRow criarRow(XSSFSheet sheet, Integer linha, CellStyle cellStyle) {
        XSSFRow row = sheet.getRow(linha);
        if (row == null) {
            row = sheet.createRow(linha);
        }
        row.setRowStyle(cellStyle);
        return row;
    }

    public CellStyle styleFonteArial10(XSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBold(false);
        font.setItalic(false);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public CellStyle styleFonteArial10Negrito(XSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBold(true);
        font.setItalic(false);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }
}
