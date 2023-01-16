package com.accounting.reader;

import com.accounting.model.Columns;
import com.accounting.model.ColumnsF1125;
import com.accounting.model.ConversionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public class XLSReader {

    private static final Logger logger = LogManager.getLogger(XLSReader.class);

    public Map<String, Map<Object,List<Cell>>> read(MultipartFile multipartFile, ConversionType conversionType) {
        Map<String, Map<Object,List<Cell>>> extractedColumns = new LinkedHashMap<>();

        try {
            XSSFWorkbook wb = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet sheet = wb.getSheet("Pagina 1");
            if (sheet == null)
                sheet = wb.getSheet("Sheet1");

            int firstRowNum = removeHeaderAndFooter(sheet);
            Row firstRow = sheet.getRow(firstRowNum);

            Integer[] columnNumbers = getColumnNumbers(conversionType, firstRow);

            String currentClass = "";

            extractColumns(multipartFile, extractedColumns, sheet, columnNumbers, currentClass, firstRowNum, conversionType);
        } catch (IOException e) {
            logger.error("Could not read xls file content {} ", e.getMessage());
        }

        return extractedColumns;
    }

    private Integer[] getColumnNumbers(ConversionType conversionType, Row firstRow) {
        Integer[] columnNumbers;
        switch (conversionType) {
            case F1102:
            case F1115:
            case F1127:
                columnNumbers = new Integer[Columns.values().length];

                for(Cell cell:firstRow) {
                    for(int i=0; i < Columns.values().length;i++) {
                        if (cell.getStringCellValue().equals(Columns.values()[i].getDefinition())) {
                            columnNumbers[i] = cell.getColumnIndex();
                        }
                    }
                }
                break;
            case F1125:
                columnNumbers = new Integer[ColumnsF1125.values().length];
                columnNumbers[0] = 0;
                columnNumbers[1] = 2;
                columnNumbers[2] = 3;
                columnNumbers[3] = 10;
                columnNumbers[4] = 11;
                break;
            default:
                columnNumbers = new Integer[0];
        }
        return columnNumbers;
    }

    private int removeHeaderAndFooter(XSSFSheet sheet) {
        Row row;
        int firstRowNum = 0;
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum; i++) {
            row = sheet.getRow(i);
            if (!row.getCell(0).getStringCellValue().equals(Columns.SIMBOL.getDefinition())) {
                sheet.removeRow(row);
                firstRowNum = firstRowNum + 1;
            } else {
                break;
            }
        }

        for (int i = lastRowNum; i >= 0; i--) {
            row = sheet.getRow(i);
            if (Objects.nonNull(row)) {
                if (!row.getCell(0).getStringCellValue().equals("TOTAL GENERAL:")) {
                    sheet.removeRow(row);
                } else {
                    break;
                }
            }
        }
        return firstRowNum;
    }

    private void extractColumns(MultipartFile multipartFile, Map<String, Map<Object, List<Cell>>> extractedColumns, XSSFSheet sheet, Integer[] columnNumbers, String currentClass, int firstRowNum, ConversionType conversionType) {
        for(Row row : sheet) {
            if (row.getRowNum() >= firstRowNum + 2) {
                for(int i=0; i < columnNumbers.length; i++) {
                    if (columnNumbers[i] != null) {
                        Cell cell = row.getCell(columnNumbers[i]);
                        currentClass = extractNonEmptyColumns(extractedColumns, currentClass, i, cell, conversionType);
                    } else{
                        logger.error("Could not find column {} in first row of {} ", i, multipartFile.getName());
                    }
                }
            }
        }
    }

    private String extractNonEmptyColumns(Map<String, Map<Object, List<Cell>>> extractedColumns, String currentClass, int i, Cell cell, ConversionType conversionType) {
        if (cell != null && cell.getCellType() != CellType.BLANK) {
            if (conversionType.equals(ConversionType.F1102)) {
                if (Columns.values()[i].equals(Columns.SIMBOL)) {
                    currentClass = extractSymbol(extractedColumns, currentClass, i, cell, conversionType);
                } else {
                    addCell(currentClass, i, extractedColumns, cell, conversionType);
                }
            } else {
                if (ColumnsF1125.values()[i].equals(ColumnsF1125.SIMBOL)) {
                    currentClass = extractSymbol(extractedColumns, currentClass, i, cell, conversionType);
                } else {
                    addCell(currentClass, i, extractedColumns, cell, conversionType);
                }
            }

        }
        return currentClass;
    }

    private String extractSymbol(Map<String, Map<Object, List<Cell>>> extractedColumns, String currentClass, int i, Cell cell, ConversionType conversionType) {
        if (cell.getCellType().equals(CellType.STRING) && !cell.getStringCellValue().isBlank()) {
            if(cell.getStringCellValue().contains("Clasa")) {
                currentClass = cell.getStringCellValue();
                Map<Object, List<Cell>> columns = extractedColumns.getOrDefault(currentClass, new LinkedHashMap<>());
                if (conversionType.equals(ConversionType.F1102)) {
                    columns.put(Columns.values()[i], new ArrayList<>());
                } else {
                    columns.put(ColumnsF1125.values()[i], new ArrayList<>());
                }
                extractedColumns.put(currentClass, columns);
            } else {
                addCell(currentClass,i, extractedColumns, cell, conversionType);
            }
        }
        return currentClass;
    }

    private void addCell(String currentClass, int column, Map<String, Map<Object, List<Cell>>> extractedColumns, Cell cell, ConversionType conversionType) {
        Map<Object, List<Cell>> columns = extractedColumns.getOrDefault(currentClass, new LinkedHashMap<>());
        if (conversionType.equals(ConversionType.F1102)) {
            List<Cell> cells = columns.getOrDefault(Columns.values()[column], new ArrayList<>());
            cells.add(cell);
            columns.put(Columns.values()[column], cells);
        } else {
            List<Cell> cells = columns.getOrDefault(ColumnsF1125.values()[column], new ArrayList<>());
            cells.add(cell);
            columns.put(ColumnsF1125.values()[column], cells);

        }
        extractedColumns.put(currentClass, columns);
    }
}
