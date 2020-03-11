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
            Map<Columns, List<Cell>> extractedColumns = xlsReader.read(selectedFile);
            Map<String, ContType> contTypes = new LinkedHashMap<>();
            extractedColumns.forEach((key, value) -> {
                switch (key) {
                    case SIMBOL:
                        value.forEach(cell -> {
                            contTypes.put(String.valueOf(cell.getRowIndex()), new ContType());
                        });
                        break;
                    case DEBITOR:
                        value.forEach(cell -> {
                            double rulDeb = cell.getNumericCellValue();
                            ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                            contType.setRulajDeb(rulDeb);
                            contTypes.replace(String.valueOf(cell.getRowIndex()), contType);
                        });
                        break;
                    case CREDITOR:
                        value.forEach(cell -> {
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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
