package reader;

import model.Columns;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class XLSReader {

    public Map<String, Map<Columns,List<Cell>>> read(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        //creating Workbook instance that refers to .xlsx file
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        //creating a Sheet object to retrieve object
        XSSFSheet sheet = wb.getSheet("Pagina 1");

        Iterator<Row> iterator = sheet.iterator();

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
        Map<String, Map<Columns,List<Cell>>> extractedColumns = new LinkedHashMap<>();

        for(Row row : sheet) {
            if(row.getRowNum() >= 2) {
                for(int i=0; i < Columns.values().length;i++) {
                    if (columnNumbers[i] != null) {
                        Cell cell = row.getCell(columnNumbers[i]);
                        if (cell != null && cell.getCellType() != CellType.BLANK) {
                            if (Columns.values()[i].equals(Columns.SIMBOL)) {
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
                        System.out.println("Could not find column " + Columns.values()[i] + " in first row of " + file.getPath());
                    }
                }

            }

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
