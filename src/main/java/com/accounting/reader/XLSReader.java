package com.accounting.reader;

import com.accounting.model.Columns;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XLSReader {

    private static final Logger logger = LogManager.getLogger(ClassSymbolsReader.class);

    public Map<String, Map<Columns,List<Cell>>> read(MultipartFile multipartFile) {
        Map<String, Map<Columns,List<Cell>>> extractedColumns = new LinkedHashMap<>();

        try {
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(multipartFile.getInputStream());
            //creating a Sheet object to retrieve object
            XSSFSheet sheet = wb.getSheet("Pagina 1");

            Row firstRow = sheet.getRow(0);
            Integer[] columnNumbers = new Integer[Columns.values().length];

            for(Cell cell:firstRow) {
                for(int i=0; i < Columns.values().length;i++) {
                    if (cell.getStringCellValue().equals(Columns.values()[i].getDefinition())) {
                        columnNumbers[i] = cell.getColumnIndex();
                    }
                }
            }

            String currentClass = "";

            for(Row row : sheet) {
                if(row.getRowNum() >= 2) {
                    for(int i=0; i < Columns.values().length;i++) {
                        if (columnNumbers[i] != null) {
                            Cell cell = row.getCell(columnNumbers[i]);
                            if (cell != null && cell.getCellType() != CellType.BLANK) {
                                if (Columns.values()[i].equals(Columns.SIMBOL )) {
                                    if (cell.getCellType().equals(CellType.STRING) && !cell.getStringCellValue().isBlank()) {
                                        if(cell.getStringCellValue().contains("Clasa")) {
                                            currentClass = cell.getStringCellValue();
                                            Map<Columns, List<Cell>> columns = extractedColumns.getOrDefault(currentClass, new LinkedHashMap<>());
                                            columns.put(Columns.values()[i], new ArrayList<>());
                                            extractedColumns.put(currentClass, columns);
                                        } else {
                                            addCell(currentClass,i, extractedColumns, cell);
                                        }
                                    }
                                } else {
                                    addCell(currentClass,i, extractedColumns, cell);
                                }
                            }
                        } else{
                            logger.error("Could not find column {} in first row of {} ", Columns.values()[i], multipartFile.getName());
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Could not read xls file content {} ", e.getMessage());
        }

        return extractedColumns;
    }

    private void addCell(String currentClass, int column, Map<String, Map<Columns, List<Cell>>> extractedColumns, Cell cell) {
        Map<Columns, List<Cell>> columns = extractedColumns.getOrDefault(currentClass, new LinkedHashMap<>());
        List<Cell> cells = columns.getOrDefault(Columns.values()[column], new ArrayList<>());
        cells.add(cell);
        columns.put(Columns.values()[column], cells);
        extractedColumns.put(currentClass, columns);
    }
}
