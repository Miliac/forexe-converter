package service;

import model.Columns;
import model.ContType;
import org.apache.poi.ss.usermodel.Cell;
import reader.XLSReader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConversionService {

    private XLSReader xlsReader;

    public ConversionService() {
        xlsReader = new XLSReader();
    }

    public void convert(File selectedFile) {

        try {
            Map<String, Map<Columns,List<Cell>>> extractedColumns = xlsReader.read(selectedFile);

            //filtrare

            Map<String, ContType> contTypes = getContType(extractedColumns);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Map<String, ContType> getContType(Map<String, Map<Columns, List<Cell>>> extractedColumns) {
        Map<String, ContType> contTypes = new LinkedHashMap<>();
        extractedColumns.forEach((key, value) -> {
            value.forEach((column, cells) -> {
                switch (column) {
                    case SIMBOL:
                        cells.forEach(cell -> {
                            contTypes.put(String.valueOf(cell.getRowIndex()), new ContType());
                        });
                        break;
                    case DEBITOR:
                        cells.forEach(cell -> {
                            double rulDeb = cell.getNumericCellValue();
                            ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                            contType.setRulajDeb(rulDeb);
                            contTypes.replace(String.valueOf(cell.getRowIndex()), contType);
                        });
                        break;
                    case CREDITOR:
                        cells.forEach(cell -> {
                            double rulCred = cell.getNumericCellValue();
                            ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                            contType.setRulajCred(rulCred);
                            contTypes.replace(String.valueOf(cell.getRowIndex()), contType);
                        });
                        break;
                    default:
                        break;
                }
            });
        });

        return contTypes;
    }
}
