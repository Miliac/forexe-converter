package com.accounting.service;

import com.accounting.model.*;
import com.accounting.reader.XLSReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.accounting.config.Utils.*;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
public class ConversionServiceImpl implements ConversionService {

    private static final Logger logger = LogManager.getLogger(ConversionServiceImpl.class);

    private XLSReader xlsReader;
    private AccountSymbolsService accountSymbolsService;
    private ExceptionsService exceptionsService;
    private MailService mailService;

    private Map<String, AccountSymbols> symbols;
    private Map<String, String> exceptions;
    private List<Cell> cellsWithCF;
    private List<Cell> cellsWithCFAndCE;
    private String codSector;
    private char codSursa;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private BigDecimal sumaControl;

    public ConversionServiceImpl(AccountSymbolsService accountSymbolsService, ExceptionsService exceptionsService, MailService mailService) {
        xlsReader = new XLSReader();
        this.accountSymbolsService = accountSymbolsService;
        this.exceptionsService = exceptionsService;
        this.mailService = mailService;
        init();
        cellsWithCF = new ArrayList<>();
        cellsWithCFAndCE = new ArrayList<>();
        sumaControl = BigDecimal.ZERO;
    }

    private void init() {
        readSymbols();
        readExceptions();
    }

    public void readSymbols() {
        symbols = accountSymbolsService.read();
        logger.info("Account symbols file loaded in memory!");
    }

    public void readExceptions() {
        exceptions = exceptionsService.read();
        logger.info("Exceptions file loaded in memory!");
    }

    @Override
    public void convert(F1102TypeDTO f1102TypeDTO, HttpServletResponse response) {

        this.codSector = getCodSector(f1102TypeDTO.getSector());
        this.codSursa = 'G';

        if(f1102TypeDTO.getDocumentFaraValori()==0) {

            Map<String, Map<Columns, List<Cell>>> extractedColumns = xlsReader.read(f1102TypeDTO.getXlsFile());

            if (!extractedColumns.isEmpty()) {
                extractedColumns.forEach((className, columns) -> extractedColumns.put(className, filterClass(className, columns, symbols, exceptions)));

                List<ContType> contTypes = getContType(extractedColumns);

                generateXml(f1102TypeDTO, response, contTypes);
            } else {
                executor.submit(() -> mailService.sendMail("No extracted columns, Xml file not generated for " + f1102TypeDTO.getNumeIp(),
                        "No extracted columns, Xml file not generated !!!", Collections.singletonList(new Attachment(XML_RESULT_NAME, f1102TypeDTO.toString()
                                .getBytes()))));
                logger.info("No extracted columns, Xml file not generated!!!");
            }
        } else {
            ContType contType = new ContType();
            contType.setRulajCred(BigDecimal.ZERO);
            contType.setRulajDeb(BigDecimal.ZERO);
            contType.setSimbolPCont("1000000");
            contType.setCodSector(codSector);
            contType.setCodSursa(String.valueOf(codSursa));
            contType.setStrCont(contType.getSimbolPCont() + codSector + contType.getCodSursa() + X.repeat(30));
            generateXml(f1102TypeDTO, response, List.of(contType));
        }


    }

    private void generateXml(F1102TypeDTO f1102TypeDTO, HttpServletResponse response, List<ContType> contTypes) {
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
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshallerObj.marshal(objectFactory.createF1102(f1102Type), byteArrayOutputStream);
            byte[] content = byteArrayOutputStream.toByteArray();
            IOUtils.copy(new ByteArrayInputStream(content), response.getOutputStream());
            List<Attachment> attachments = new ArrayList<>();
            attachments.add(new Attachment(f1102TypeDTO.getXlsFile()
                    .getOriginalFilename(), f1102TypeDTO.getXlsFile()
                    .getBytes()));
            attachments.add(new Attachment(XML_RESULT_NAME, content));
            executor.submit(() -> mailService.sendMail("Xml generated with success for " + f1102TypeDTO.getNumeIp(),
                    "Xml file generated with success !!!", attachments));
            logger.info("Xml file generated with success!");
        } catch (Exception e) {
            executor.submit(() -> mailService.sendMail("Error while generating Xml for " + f1102TypeDTO.getNumeIp(),
                    "Error while generating Xml !!!", Collections.singletonList(new Attachment("error.txt", e.toString()
                            .getBytes()))));
            logger.error(e.getMessage());
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
                contTypes.remove(contType);
                return false;
            }
        }
        return true;
    }

    private Map<Columns, List<Cell>> filterClass(String className, Map<Columns, List<Cell>> columns, Map<String, AccountSymbols> symbols, Map<String, String> exceptions) {

        List<Cell> symbolColumn = columns.get(Columns.SIMBOL);
        List<Cell> debitorColumn = columns.get(Columns.DEBITOR);
        List<Cell> creditorColumn = columns.get(Columns.CREDITOR);

        List<Cell> finalDebitorColumn = debitorColumn;
        List<Cell> finalCreditorColumn = creditorColumn;
        List<Cell> finalSymbolColumn = symbolColumn.stream()
                .filter(cell ->
                        (finalDebitorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() != 0) ||
                                (finalCreditorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() != 0))
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

    private boolean filterByClass(String className, Cell cell, Map<String, AccountSymbols> symbols, List<Cell> symbolColumns, Map<String, String> exceptions) {
        AccountSymbols accountSymbols = symbols.getOrDefault(className, new AccountSymbols());

        if (exceptions.containsKey(cell.getStringCellValue())) {
            cell.setCellValue(exceptions.get(cell.getStringCellValue()));
            return true;
        } else {

            if (cell.getStringCellValue().length() == 3) {
                return filterCellsWith3Length(cell, symbolColumns, accountSymbols);
            }

            if (cell.getStringCellValue().length() == 5 || cell.getStringCellValue().length() == 7) {
                return filterCellsWithLengthBetween5And7(cell, symbolColumns, accountSymbols);
            }

            if (cell.getStringCellValue().length() >= 14 && cell.getStringCellValue().length() < 20) {
                return filterCellsWithLengthBetween14And20(cell, accountSymbols);
            }

            if (cell.getStringCellValue().length() >= 20 && cell.getStringCellValue().length() <= 22) {
                return filterCellsWithLengthBetween20And22(cell, symbolColumns, accountSymbols);
            }
        }

        return false;
    }

    private boolean filterCellsWith3Length(Cell cell, List<Cell> symbolColumns, AccountSymbols accountSymbols) {
        String symbol = cell.getStringCellValue().concat(ZERO.repeat(4));
        if (accountSymbols.getAccountSymbolsEndInFourZeros()
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

    private boolean filterCellsWithLengthBetween5And7(Cell cell, List<Cell> symbolColumns, AccountSymbols accountSymbols) {
        String symbol = cell.getStringCellValue().length() == 5 ? cell.getStringCellValue().concat(ZERO.repeat(2)) : cell.getStringCellValue();
        if (accountSymbols.getAccountSymbolsWithCFAndCE().contains(symbol)) {
            cellsWithCFAndCE = symbolColumns.stream()
                    .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) &&
                            (nextCell.getStringCellValue().length() == 20 || nextCell.getStringCellValue().length() == 22))
                    .filter(nextCell -> !accountSymbols.getAccountSymbolsWithCFAndCE().contains(getSymbol(nextCell)))
                    .collect(Collectors.toList());
            return !cellsWithCFAndCE.isEmpty();
        }

        if (!symbol.endsWith(ZERO.repeat(2))) {
            return accountSymbols.getAccountSymbols().contains(symbol);
        }

        if (accountSymbols.getAccountSymbolsEndInTwoZeros().contains(symbol)) {
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

    private boolean filterCellsWithLengthBetween14And20(Cell cell, AccountSymbols accountSymbols) {
        String symbol = getSymbol(cell);
        if (accountSymbols.getAccountSymbolsWithCF().contains(symbol)) {
            cell.setCellValue(getCellContent(cell));
            return true;
        } else {
            if (cellsWithCF.size() >= 2) {
                if(accountSymbols.getAccountSymbols().contains(symbol) ||
                        accountSymbols.getAccountSymbolsEndInTwoZeros().contains(symbol) ||
                        accountSymbols.getAccountSymbolsEndInFourZeros().contains(symbol)) {
                    cell.setCellValue(cell.getStringCellValue().substring(0, 10));
                }
                cell.setCellValue(getCellContent(cell));
                return cellsWithCF.contains(cell);
            } else {
                cell.setCellValue(cell.getStringCellValue().substring(0, 10));
                return accountSymbols.getAccountSymbols().contains(symbol);
            }
        }
    }

    private boolean filterCellsWithLengthBetween20And22(Cell cell, List<Cell> symbolColumns, AccountSymbols accountSymbols) {
        String symbol = getSymbol(cell);
        cell.setCellValue(getCellContent(cell));
        if (accountSymbols.getAccountSymbolsWithCFAndCE().contains(symbol) && cell.getStringCellValue().length() == 20) {
            cellsWithCFAndCE = symbolColumns.stream()
                    .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) &&
                            nextCell.getStringCellValue().length() == 22)
                    .collect(Collectors.toList());
            return cellsWithCFAndCE.isEmpty();
        }

        return accountSymbols.getAccountSymbolsWithCFAndCE().contains(symbol);
    }

    private String getCellContent(Cell cell) {
        String content = cell.getStringCellValue();
        int indexG = content.indexOf(codSursa);
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
        String symbol = cell.getStringCellValue().substring(0, content.indexOf(codSursa)-2);
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
                        sumaControl = sumaControl.add(rulDeb);
                        ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new ContType());
                        contType.setRulajDeb(rulDeb.equals(ZERO_DECIMAL) ? null : rulDeb.stripTrailingZeros());
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                case CREDITOR:
                    cells.forEach(cell -> {
                        BigDecimal rulCred = BigDecimal.valueOf(cell.getNumericCellValue());
                        sumaControl = sumaControl.add(rulCred);
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
        f1102Type.setDRec(f1102TypeDTO.getdRec() ? 1 : 0);
        f1102Type.setSumaControl(sumaControl.add(BigDecimal.valueOf(Long.parseLong(f1102TypeDTO.getCuiIp()))).toBigInteger());
        f1102Type.setFormularFaraValori(f1102TypeDTO.getDocumentFaraValori());

        return f1102Type;
    }


    private String dateFormatter(String dateString) {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT));
        return date.format(DateTimeFormatter.ofPattern(F1102_DATE_FORMAT));
    }

    private String getCodSector(String codSector) {
        return codSector.substring(0,codSector.indexOf(LINE) - 1);
    }

    private void fillContType(ContType contType, String symbol) {
        contType.setCodSector(codSector);
        contType.setCodSursa(String.valueOf(codSursa));
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
