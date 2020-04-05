package com.accounting.service;

import com.accounting.model.*;
import com.accounting.reader.ClassSymbolsReader;
import com.accounting.reader.ExceptionsReader;
import com.accounting.reader.XLSReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.accounting.config.Utils.*;
import static org.apache.logging.log4j.util.Strings.*;

@Service
public class ConversionServiceImpl implements ConversionService {

    private static final Logger logger = LogManager.getLogger(ConversionServiceImpl.class);

    private XLSReader xlsReader;
    private ClassSymbolsReader symbolsReader;
    private ExceptionsReader exceptionsReader;

    private Map<String, ClassSymbols> symbols;
    private Map<String, String> exceptions;
    private List<Cell> cellsWithCF;
    private List<Cell> cellsWithCFAndCE;
    private String cod_sector;
    private char cod_sursa;

    public ConversionServiceImpl() {
        xlsReader = new XLSReader();
        symbolsReader = new ClassSymbolsReader();
        exceptionsReader = new ExceptionsReader();
        init();
        cellsWithCF = new ArrayList<>();
        cellsWithCFAndCE = new ArrayList<>();
    }

    private void init() {
        symbols = symbolsReader.read();
        exceptions = exceptionsReader.read();
    }

    @Override
    public void convert(F1102TypeDTO f1102TypeDTO, HttpServletResponse response) {

        this.cod_sector = getCodSector(f1102TypeDTO.getSector());
        this.cod_sursa = 'G';
        
        Map<String, Map<Columns, List<Cell>>> extractedColumns = xlsReader.read(f1102TypeDTO.getXlsFile());

        if (!extractedColumns.isEmpty()) {
            extractedColumns.forEach((className, columns) -> extractedColumns.put(className, filterClass(className, columns, symbols, exceptions)));

            List<ContType> contTypes = getContType(extractedColumns);
            F1102Type f1102Type = convertFromDTO(f1102TypeDTO);
            f1102Type.setCont(contTypes.stream()
                    .filter(contType -> removeDuplicatesAndSumValues(contType, contTypes))
                    .collect(Collectors.toList()));
            try {
                ObjectFactory objectFactory = new ObjectFactory();
                JAXBContext contextObj = JAXBContext.newInstance(F1102Type.class);
                Marshaller marshallerObj = contextObj.createMarshaller();
                marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshallerObj.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "mfp:anaf:dgti:f1102:declaratie:v1");
                marshallerObj.marshal(objectFactory.createF1102(f1102Type), response.getOutputStream());
                logger.info("Xml file generated with success!");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.info("No extracted columns, Xml file not generated!!!");
        }
    }

    private boolean removeDuplicatesAndSumValues(ContType contType, List<ContType> contTypes) {
        int indexCont = contTypes.indexOf(contType);
        if (indexCont > 0) {
            ContType previousCont = contTypes.get(indexCont - 1);
            if (contType.getStrCont().equals(previousCont.getStrCont())) {
                BigDecimal rulajDebPrevious = Objects.nonNull(previousCont.getRulajDeb()) ? previousCont.getRulajDeb() : ZERO_DECIMAL;
                BigDecimal rulajCredPrevious = Objects.nonNull(previousCont.getRulajCred()) ? previousCont.getRulajCred() : ZERO_DECIMAL;
                BigDecimal rulajDebCont = Objects.nonNull(contType.getRulajDeb()) ? contType.getRulajDeb() : ZERO_DECIMAL;
                BigDecimal rulajCredCont = Objects.nonNull(contType.getRulajCred()) ? contType.getRulajCred() : ZERO_DECIMAL;
                previousCont.setRulajDeb(rulajDebPrevious.add(rulajDebCont).stripTrailingZeros());
                previousCont.setRulajCred(rulajCredPrevious.add(rulajCredCont).stripTrailingZeros());
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
        List<Cell> finalSymbolColumn = symbolColumn.stream()
                .filter(cell ->
                        (finalDebitorColumn.get(symbolColumn.indexOf(cell))
                                .getNumericCellValue() < 0 || finalDebitorColumn.get(symbolColumn.indexOf(cell))
                                .getNumericCellValue() >= 1) ||
                                (finalCreditorColumn.get(symbolColumn.indexOf(cell))
                                        .getNumericCellValue() < 0 || finalCreditorColumn.get(symbolColumn.indexOf(cell))
                                        .getNumericCellValue() >= 1))
                .map(this::getCellTrimValue)
                .collect(Collectors.toList());

        List<Cell> filteredSymbolColumn = finalSymbolColumn.stream()
                .filter(cell -> filterByClass(className, cell, symbols, finalSymbolColumn, exceptions))
                .collect(Collectors.toList());
        debitorColumn = debitorColumn.stream()
                .filter(cell -> filterCell(cell, filteredSymbolColumn))
                .collect(Collectors.toList());
        creditorColumn = creditorColumn.stream()
                .filter(cell -> filterCell(cell, filteredSymbolColumn))
                .collect(Collectors.toList());

        columns.put(Columns.SIMBOL, filteredSymbolColumn);
        columns.put(Columns.DEBITOR, debitorColumn);
        columns.put(Columns.CREDITOR, creditorColumn);

        return columns;
    }

    private boolean filterByClass(String className, Cell cell, Map<String, ClassSymbols> symbols, List<Cell> symbolColumns, Map<String, String> exceptions) {
        ClassSymbols classSymbols = symbols.getOrDefault(className, new ClassSymbols());

        if (exceptions.containsKey(cell.getStringCellValue())) {
            cell.setCellValue(exceptions.get(cell.getStringCellValue()));
            return true;
        } else {

            if (cell.getStringCellValue().length() == 3) {
                String symbol = cell.getStringCellValue().concat(ZERO.repeat(4));
                if (classSymbols.getAccountSymbolsEndInFourZeros()
                        .contains(symbol)) {
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
                String symbol = cell.getStringCellValue().length() == 5 ? cell.getStringCellValue().concat(ZERO.repeat(2)) : cell.getStringCellValue();
                if (classSymbols.getAccountSymbolsWithCFAndCE().contains(symbol)) {
                    cellsWithCFAndCE = symbolColumns.stream()
                            .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) &&
                                    (nextCell.getStringCellValue().length() == 20 || nextCell.getStringCellValue().length() == 22))
                            .filter(nextCell -> !classSymbols.getAccountSymbolsWithCFAndCE().contains(getSymbol(nextCell)))
                            .collect(Collectors.toList());
                    return !cellsWithCFAndCE.isEmpty();
                }

                if (!symbol.endsWith(ZERO.repeat(2))) {
                    return classSymbols.getAccountSymbols().contains(symbol);
                }

                if (classSymbols.getAccountSymbolsEndInTwoZeros().contains(symbol)) {
                    cellsWithCF = symbolColumns.stream()
                            .filter(nextCell -> nextCell.getStringCellValue()
                                    .startsWith(cell.getStringCellValue()) && nextCell.getStringCellValue().length() == 16)
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
                    cell.setCellValue(getCellContent(cell));
                    return true;
                } else {
                    if (cellsWithCF.size() >= 2) {
                        if(classSymbols.getAccountSymbols().contains(symbol) ||
                                classSymbols.getAccountSymbolsEndInTwoZeros().contains(symbol) ||
                                classSymbols.getAccountSymbolsEndInFourZeros().contains(symbol)) {
                            cell.setCellValue(cell.getStringCellValue().substring(0, 10));
                        }
                        cell.setCellValue(getCellContent(cell));
                        return cellsWithCF.contains(cell);
                    } else {
                        cell.setCellValue(cell.getStringCellValue().substring(0, 10));
                        return classSymbols.getAccountSymbols().contains(symbol);
                    }
                }
            }

            if (cell.getStringCellValue()
                    .length() >= 20 && cell.getStringCellValue()
                    .length() <= 22) {
                String symbol = getSymbol(cell);
                cell.setCellValue(getCellContent(cell));
                if (classSymbols.getAccountSymbolsWithCFAndCE().contains(symbol) && cell.getStringCellValue().length() == 20) {
                    cellsWithCFAndCE = symbolColumns.stream()
                            .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) &&
                                    nextCell.getStringCellValue().length() == 22)
                            .collect(Collectors.toList());
                    return cellsWithCFAndCE.isEmpty();
                }

                return classSymbols.getAccountSymbolsWithCFAndCE()
                        .contains(symbol);
            }
        }

        return false;
    }

    private String getCellContent(Cell cell) {
        String content = cell.getStringCellValue();
        int indexG = content.indexOf(cod_sursa);
        if(indexG != 9) {
            String symbol = getSymbol(cell);
            String contSufix = content.substring(indexG-2);
            return symbol.concat(contSufix);
        }
        return content;
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
        cell.setCellValue(cell.getStringCellValue().replaceAll(SPACE, EMPTY));
        return cell;
    }

    private boolean filterDifferentSymbol(Cell cell, List<Cell> symbolColumns) {
        String symbol = cell.getStringCellValue().substring(0, SYMBOL_LENGTH);
        boolean result = true;
        for (Cell nextCell : symbolColumns) {
            if (!nextCell.equals(cell) && nextCell.getStringCellValue()
                    .startsWith(symbol)) {
                result = false;
            }
        }
        return result;
    }

    private String getSymbol(Cell cell) {
        String content = cell.getStringCellValue();
        String symbol = cell.getStringCellValue().substring(0, content.indexOf(cod_sursa)-2);
        return symbol.concat(ZERO.repeat(SYMBOL_LENGTH - symbol.length()));
    }

    private List<ContType> getContType(Map<String, Map<Columns, List<Cell>>> extractedColumns) {
        Map<String, ContType> contTypes = new LinkedHashMap<>();
        extractedColumns.forEach((key, value) -> value.forEach((column, cells) -> {
            switch (column) {
                case SIMBOL:
                    cells.forEach(cell -> {
                        ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                        fillContType(contType, cell.getStringCellValue());
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                case DEBITOR:
                    cells.forEach(cell -> {
                        BigDecimal rulDeb = BigDecimal.valueOf(cell.getNumericCellValue());
                        ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                        contType.setRulajDeb(rulDeb.equals(ZERO_DECIMAL) ? null : rulDeb.stripTrailingZeros());
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                case CREDITOR:
                    cells.forEach(cell -> {
                        BigDecimal rulCred = BigDecimal.valueOf(cell.getNumericCellValue());
                        ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                        contType.setRulajCred(rulCred.equals(ZERO_DECIMAL) ? null : rulCred.stripTrailingZeros());
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                default:
                    break;
            }
        }));

        return new ArrayList<>(contTypes.values());
    }

    private F1102Type convertFromDTO(F1102TypeDTO f1102TypeDTO) {
        F1102Type f1102Type = new F1102Type();
        f1102Type.setAn(f1102TypeDTO.getAn());
        f1102Type.setCuiIp(f1102TypeDTO.getCuiIp());
        f1102Type.setDataDocument(dateFormatter(f1102TypeDTO.getDataDocument()));
        f1102Type.setLunaR(f1102TypeDTO.getLunaR());
        f1102Type.setNumeIp(f1102TypeDTO.getNumeIp());
        f1102Type.setSumaControl(f1102TypeDTO.getSumaControl());
        return f1102Type;
    }

    private String dateFormatter(String dateString) {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT));
        return date.format(DateTimeFormatter.ofPattern(F1102_DATE_FORMAT));
    }

    private String getCodSector(String cod_sector) {
        return cod_sector.substring(0,cod_sector.indexOf(LINE) - 1);
    }

    private void fillContType(ContType contType, String symbol) {
        contType.setCodSector(cod_sector);
        contType.setCodSursa(String.valueOf(cod_sursa));
        if (symbol.length() <= 10) {
            symbol = symbol.length() < SYMBOL_LENGTH ? symbol.concat(ZERO.repeat(SYMBOL_LENGTH - symbol.length())) : symbol;
            contType.setSimbolPCont(symbol.substring(0, SYMBOL_LENGTH));
        } else {
            String symbolPCont = symbol.substring(0, SYMBOL_LENGTH);
            if (symbol.length() <= 16) {
                String cf = symbol.substring(10);
                cf = cf.concat(ZERO.repeat(CF_CE_LENGTH - cf.length()));
                contType.setCf(cf);
            } else {
                String cf = symbol.substring(10, 16);
                String ce = symbol.substring(16);
                ce = ce.concat(ZERO.repeat(CF_CE_LENGTH - ce.length()));
                contType.setCf(cf);
                contType.setCe(ce);
            }
            contType.setSimbolPCont(symbolPCont);
        }
        String strCont = contType.getSimbolPCont().concat(contType.getCodSector()).concat(contType.getCodSursa())
                .concat(Objects.nonNull(contType.getCf()) ? contType.getCf() : EMPTY)
                .concat(Objects.nonNull(contType.getCe()) ? contType.getCe() : EMPTY);
        strCont = strCont.concat(X.repeat(STRCONT_LENGTH - strCont.length()));
        contType.setStrCont(strCont);
    }
}
