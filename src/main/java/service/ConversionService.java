package service;

import model.Columns;
import model.ContType;
import org.apache.poi.ss.usermodel.Cell;
import reader.XLSReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConversionService {

    private XLSReader xlsReader;

    public ConversionService() {
        xlsReader = new XLSReader();
    }

    private List<Cell> addedCells = new ArrayList<>();

    public void convert(File selectedFile) {

        try {
            Map<String, Map<Columns,List<Cell>>> extractedColumns = xlsReader.read(selectedFile);

//          extractedColumns.forEach((k, v) -> extractedColumns.put(k, filterClasa(extractedColumns, k)));

            extractedColumns.put("Clasa 2", filterClasa(extractedColumns, "Clasa 2"));

            Map<String, ContType> contTypes = getContType(extractedColumns);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Map<Columns, List<Cell>> filterClasa(Map<String, Map<Columns, List<Cell>>> extractedClolumns, String clasa) {

        Map<Columns, List<Cell>> columns = extractedClolumns.get(clasa);
        List<Cell> symbolColumn = columns.get(Columns.SIMBOL);
        List<Cell> debitorColumn = columns.get(Columns.DEBITOR);
        List<Cell> creditorColumn = columns.get(Columns.CREDITOR);

        List<Cell> finalDebitorColumn = debitorColumn;
        List<Cell> finalCreditorColumn = creditorColumn;
        List<Cell> finalSymbolColumn = symbolColumn;
        symbolColumn = symbolColumn.stream().filter(cell -> filterByClass(clasa, cell, finalSymbolColumn) &&
                (finalDebitorColumn.get(cell.getRowIndex()).getNumericCellValue() > 0 ||
                        finalCreditorColumn.get(cell.getRowIndex()).getNumericCellValue() > 0)).collect(Collectors.toList());
        List<Cell> filteredSymbolColumn = symbolColumn;
        debitorColumn = debitorColumn.stream().filter(cell -> filterCell(cell, filteredSymbolColumn))
                .collect(Collectors.toList());
        creditorColumn = creditorColumn.stream().filter(cell -> filterCell(cell, filteredSymbolColumn)).collect(Collectors.toList());

        columns.put(Columns.SIMBOL, symbolColumn);
        columns.put(Columns.DEBITOR, debitorColumn);
        columns.put(Columns.CREDITOR, creditorColumn);

        return columns;
    }

    private boolean filterByClass(String clasa, Cell cell, List<Cell> symbolColumn) {
        switch (clasa) {
            case "Clasa 1":
                return cell.getStringCellValue().length() == 6;
            case "Clasa 2":
                if (cell.getStringCellValue().length() == 6) {
                    int i = symbolColumn.indexOf(cell) + 1;
                    while ((symbolColumn.get(i).getStringCellValue().length() == 6 ||
                            symbolColumn.get(i).getStringCellValue().length() == 3) &&
                            i < symbolColumn.size()) {
                        addedCells.add(symbolColumn.get(i));
                        i++;
                    }
                    if (i > symbolColumn.indexOf(cell) + 2) {
                        return false;
                    } else {
                        addedCells.clear();
                        return true;
                    }
                } else {
                    if (addedCells.contains(cell)) {
                        return true;
                    }
                    addedCells.clear();
                    return false;
                }
            default:
                return false;
        }
    }

    private boolean filterCell(Cell cell, List<Cell> cells) {
        for (Cell c : cells) {
            if (c.getRowIndex() == cell.getRowIndex()) {
                return true;
            }
        }
        return false;
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
