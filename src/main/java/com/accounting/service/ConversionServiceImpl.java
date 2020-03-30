package com.accounting.service;

import com.accounting.model.*;
import com.accounting.reader.ClassSymbolsReader;
import com.accounting.reader.ExceptionsReader;
import com.accounting.reader.XLSReader;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConversionServiceImpl implements ConversionService {

    private static final String RESULT_PATH = "src/main/resources/result.xml";

    private XLSReader xlsReader;
    private ClassSymbolsReader symbolsReader;
    private ExceptionsReader exceptionsReader;

    public ConversionServiceImpl() {
        xlsReader = new XLSReader();
        symbolsReader = new ClassSymbolsReader();
        exceptionsReader = new ExceptionsReader();
    }

    private List<Cell> cellsWithCF = new ArrayList<>();
    private List<Cell> cellsWithCFAndCE = new ArrayList<>();

    @Override
    public void convert(MultipartFile multipartFile, F1102Type f1102Type) {

        try {
            Map<String, Map<Columns,List<Cell>>> extractedColumns = xlsReader.read(multipartFile);
            Map<String, ClassSymbols> symbols = symbolsReader.read();
            Map<String, String> exceptions = exceptionsReader.read();
            extractedColumns.forEach((className, columns) -> extractedColumns.put(className, filterClass(className, columns, symbols, exceptions)));

            List<ContType> contTypes = getContType(extractedColumns);
            f1102Type.setCont(contTypes.stream().filter(contType -> filterByCont(contType, contTypes)).collect(Collectors.toList()));
            try {
                ObjectFactory objectFactory = new ObjectFactory();
                JAXBContext contextObj = JAXBContext.newInstance(F1102Type.class);
                Marshaller marshallerObj = contextObj.createMarshaller();
                marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshallerObj.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "mfp:anaf:dgti:f1102:declaratie:v1");
                marshallerObj.marshal(objectFactory.createF1102(f1102Type), new FileOutputStream(RESULT_PATH));
                System.out.println("Xml file generated with success");
            } catch (JAXBException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean filterByCont(ContType contType, List<ContType> contTypes) {
        int indexCont = contTypes.indexOf(contType);
        if(indexCont > 0) {
            ContType previousCont = contTypes.get(indexCont - 1);
            if (contType.getStrCont().equals(previousCont.getStrCont())) {
                previousCont.setRulajDeb(previousCont.getRulajDeb().add(contType.getRulajDeb()));
                previousCont.setRulajCred(previousCont.getRulajCred().add(contType.getRulajCred()));
                return false;
            }
        }
        return true;
    }

    private Map<Columns, List<Cell>> filterClass(String className, Map<Columns, List<Cell>> columns, Map<String, ClassSymbols> symbols, Map<String, String> exceptions) {

        List<Cell> symbolColumn = columns.get(Columns.SIMBOL);
        List<Cell> debitorColumn = columns.get(Columns.DEBITOR);
        List<Cell> creditorColumn = columns.get(Columns.CREDITOR);

        List<Cell> finalDebitorColumn = debitorColumn;
        List<Cell> finalCreditorColumn = creditorColumn;
        List<Cell> finalSymbolColumn = symbolColumn.stream().filter(cell ->
            (finalDebitorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() < 0 || finalDebitorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() >= 1) ||
            (finalCreditorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() < 0 || finalCreditorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() >= 1))
            .map(this::getCellTrimValue)
            .collect(Collectors.toList());

        List<Cell> filteredSymbolColumn = finalSymbolColumn.stream().filter(cell -> filterByClass(className, cell, symbols, finalSymbolColumn, exceptions)).collect(Collectors.toList());
        debitorColumn = debitorColumn.stream().filter(cell -> filterCell(cell, filteredSymbolColumn))
                .collect(Collectors.toList());
        creditorColumn = creditorColumn.stream().filter(cell -> filterCell(cell, filteredSymbolColumn)).collect(Collectors.toList());

        columns.put(Columns.SIMBOL, filteredSymbolColumn);
        columns.put(Columns.DEBITOR, debitorColumn);
        columns.put(Columns.CREDITOR, creditorColumn);

        return columns;
    }

    private boolean filterByClass(String className, Cell cell, Map<String, ClassSymbols> symbols, List<Cell> symbolColumns, Map<String, String> exceptions) {
        ClassSymbols classSymbols = symbols.getOrDefault(className, new ClassSymbols());

        if(exceptions.containsKey(cell.getStringCellValue())) {
            cell.setCellValue(exceptions.get(cell.getStringCellValue()));
            return true;
        } else {

            if (cell.getStringCellValue().length() == 3) {
                String symbol = cell.getStringCellValue().concat("0000");
                if (classSymbols.getAccountSymbolsEndInFourZeros().contains(symbol)) {
                    cellsWithCF = symbolColumns.stream()
                            .filter(nextCell -> nextCell.getStringCellValue().startsWith(symbol) && nextCell.getStringCellValue().length() == 16)
                            .collect(Collectors.toList());
                    cellsWithCF = cellsWithCF.stream()
                            .filter(nextCell -> filterDifferentSymbol(nextCell, cellsWithCF))
                            .collect(Collectors.toList());
                    return cellsWithCF.size() < 2;
                } else {
                    cellsWithCF.clear();
                    return false;
                }
            }

            if (cell.getStringCellValue().length() == 5 || cell.getStringCellValue().length() == 7) {
                String symbol = cell.getStringCellValue().length() == 5 ? cell.getStringCellValue().concat("00") : cell.getStringCellValue();
                if (classSymbols.getAccountSymbolsWithCFAndCE().contains(symbol)) {
                    cellsWithCFAndCE = symbolColumns.stream()
                            .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) &&
                                    (nextCell.getStringCellValue().length() == 20 || nextCell.getStringCellValue().length() == 22))
                            .filter(nextCell -> !classSymbols.getAccountSymbolsWithCFAndCE().contains(getSymbol(nextCell)))
                            .collect(Collectors.toList());
                    return cellsWithCFAndCE.size() != 0;
                }

                if (!symbol.endsWith("00")) {
                    return classSymbols.getAccountSymbols().contains(symbol);
                }

                if (classSymbols.getAccountSymbolsEndInTwoZeros().contains(symbol)) {
                    cellsWithCF = symbolColumns.stream()
                            .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) && nextCell.getStringCellValue().length() == 16)
                            .collect(Collectors.toList());
                    cellsWithCF = cellsWithCF.stream()
                            .filter(nextCell -> filterDifferentSymbol(nextCell, cellsWithCF))
                            .collect(Collectors.toList());
                    return cellsWithCF.size() < 2;
                } else {
                    cellsWithCF.clear();
                    return false;
                }
            }

            if (cell.getStringCellValue().length() >= 14 && cell.getStringCellValue().length() < 20) {
                String symbol = getSymbol(cell);
                if (classSymbols.getAccountSymbolsWithCF().contains(symbol)) {
                    return true;
                } else {
                    if (cellsWithCF.size() >= 2) {
                        return cellsWithCF.contains(cell);
                    } else {
                        cell.setCellValue(cell.getStringCellValue().substring(0, 10));
                        return classSymbols.getAccountSymbols().contains(symbol);
                    }
                }
            }

            if (cell.getStringCellValue().length() >= 20 && cell.getStringCellValue().length() <= 22) {
                String symbol = getSymbol(cell);
                if (classSymbols.getAccountSymbolsWithCFAndCE().contains(symbol) && cell.getStringCellValue().length() == 20) {
                    cellsWithCFAndCE = symbolColumns.stream()
                            .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) && nextCell.getStringCellValue().length() == 22)
                            .collect(Collectors.toList());
                    return cellsWithCFAndCE.size() == 0;
                }
                return classSymbols.getAccountSymbolsWithCFAndCE().contains(symbol);
            }
        }

        return false;
    }

    private boolean filterCell(Cell cell, List<Cell> cells) {
        for (Cell c : cells) {
            if (c.getRowIndex() == cell.getRowIndex()) {
                return true;
            }
        }
        return false;
    }

    private Cell getCellTrimValue(Cell cell) {
        cell.setCellValue(cell.getStringCellValue().replaceAll(" ",""));
        return cell;
    }

    private boolean filterDifferentSymbol(Cell cell, List<Cell> symbolColumns) {
        String symbol = cell.getStringCellValue().substring(0,7);
        boolean result = true;
        for(Cell nextCell : symbolColumns) {
            if(!nextCell.equals(cell) && nextCell.getStringCellValue().startsWith(symbol)) {
                result = false;
            }
        }
        return result;
    }

    private String getSymbol(Cell cell) {
        return cell.getStringCellValue().charAt(7) == 'G' ?
                cell.getStringCellValue().substring(0, 5).concat("00") : cell.getStringCellValue().substring(0, 7);
    }

    private List<ContType> getContType(Map<String, Map<Columns, List<Cell>>> extractedColumns) {
        Map<String, ContType> contTypes = new LinkedHashMap<>();
        extractedColumns.forEach((key, value) -> value.forEach((column, cells) -> {
            switch (column) {
                case SIMBOL:
                    cells.forEach(cell -> {
                        String simbol = cell.getStringCellValue();
                        ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                        contType.setCodSector("02");
                        contType.setCodSursa("G");
                        if(simbol.length() <= 10) {
                            while(simbol.length()<7) {
                                simbol = simbol.concat("0");
                            }
                            contType.setSimbolPCont(simbol.substring(0, 7));
                            contType.setCf("");
                            contType.setCe("");

                        } else {
                            String simbolPCont = simbol.substring(0, 7);
                            if(simbol.length() <= 16) {
                                String cf = simbol.substring(10);
                                while(cf.length()<6) {
                                    cf = cf.concat("0");
                                }
                                contType.setCf(cf);
                                contType.setCe("");
                            } else {
                                String cf = simbol.substring(10,16);
                                String ce = simbol.substring(17);
                                while(ce.length()<6) {
                                    ce = ce.concat("0");
                                }
                                contType.setCf(cf);
                                contType.setCe(ce);
                            }
                            contType.setSimbolPCont(simbolPCont);
                        }
                        String strCont = contType.getSimbolPCont() + contType.getCodSector() + contType.getCodSursa() + contType.getCf() + contType.getCe();
                        while(strCont.length() < 40) {
                            strCont = strCont.concat("X");
                        }
                        contType.setStrCont(strCont);
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                case DEBITOR:
                    cells.forEach(cell -> {
                        BigDecimal rulDeb = BigDecimal.valueOf(cell.getNumericCellValue());
                        ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                        contType.setRulajDeb(rulDeb.equals(BigDecimal.valueOf(0.0)) ? null : rulDeb);
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                case CREDITOR:
                    cells.forEach(cell -> {
                        BigDecimal rulCred = BigDecimal.valueOf(cell.getNumericCellValue());
                        ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                        contType.setRulajCred(rulCred.equals(BigDecimal.valueOf(0.0)) ? null : rulCred);
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                default:
                    break;
            }
        }));

        return new ArrayList<>(contTypes.values());
    }

    @Override
    public F1102Type getFromDTO(F1102TypeDTO f1102TypeDTO){
        return convertFromDTO(f1102TypeDTO);
    }

    private F1102Type convertFromDTO(F1102TypeDTO f1102TypeDTO){
        F1102Type f1102Type = new F1102Type();
        f1102Type.setAn(f1102TypeDTO.getAn());
        f1102Type.setCuiIp(f1102TypeDTO.getCuiIp());
        f1102Type.setDataDocument(f1102TypeDTO.getDataDocument());
        f1102Type.setLunaR(f1102TypeDTO.getLunaR());
        f1102Type.setNumeIp(f1102TypeDTO.getNumeIp());
        f1102Type.setSumaControl(f1102TypeDTO.getSumaControl());
        return f1102Type;
    }
}
