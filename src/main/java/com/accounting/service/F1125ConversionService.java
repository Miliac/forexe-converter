package com.accounting.service;

import com.accounting.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.accounting.config.Utils.F1125_RESULT_NAME;
import static com.accounting.config.Utils.SYMBOL_LENGTH;
import static com.accounting.config.Utils.XML_CONTENT_TYPE;
import static com.accounting.config.Utils.ZERO;
import static com.accounting.config.Utils.ZERO_DECIMAL;

@Service
public class F1125ConversionService extends AbstractConversionService implements ConversionService  {

    private static final Logger logger = LogManager.getLogger(F1125ConversionService.class);
    private final F1125ConfigService f1125ConfigService;

    F1125ConversionService(MailService mailService, ConfigsProviderService configsProviderService, F1125ConfigService f1125ConfigService) {
        super(mailService, configsProviderService);
        this.f1125ConfigService = f1125ConfigService;
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response, EmailDTO emailDTO) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1125.xml");

        Map<String, Map<Object, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile(), ConversionType.F1125);
        if (!extractedColumns.isEmpty()) {

            extractedColumns.forEach((className, columns) -> extractedColumns.put(className, filterClass(className, columns, configsProviderService.getSymbols(), configsProviderService.getExceptions())));

            F1125Type f1125Type = convertFromDTO(formData);
            F1125SursaType sursaType = new F1125SursaType();
            List<F1125IndicatorType> f1125IndicatorTypes = getIndicatorTypes(extractedColumns);
            sursaType.setF1125Indicator(f1125IndicatorTypes);
            sursaType.setCodSursa("G");
            List<F1125SursaType> sursaTypes = List.of(sursaType);
            f1125Type.setF1125Sursa(sursaTypes);
            generateXml(formData, response, f1125Type, emailDTO);
        } else {
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "No extracted columns, F1125 file not generated for " + formData.getNumeIp(),
                    "No extracted columns, F1125 file not generated !!!", Collections.singletonList(new Attachment(F1125_RESULT_NAME, formData.toString()
                            .getBytes())))));
            logger.info("No extracted columns, F1125 file not generated!!!");
        }
    }

    private List<F1125IndicatorType> getIndicatorTypes(Map<String, Map<Object, List<Cell>>> extractedColumns) {
        List<F1125IndicatorType> f1125IndicatorTypes = new LinkedList<>();
        List<F1125Row> rows = f1125ConfigService.read();
        for(int i=1; i < 412; i++) {
            F1125IndicatorType f1125IndicatorType = new F1125IndicatorType();
            int index = i;
            rows.stream().filter(row -> row.getNumber() == index).findFirst()
                    .ifPresent(row -> setIndicatorTypeAccounts(f1125IndicatorType, row, extractedColumns));
            f1125IndicatorType.setCodRand(String.valueOf(index));
            f1125IndicatorTypes.add(f1125IndicatorType);
        }

        for(int i=1; i < 412; i++) {
            F1125IndicatorType f1125IndicatorType = f1125IndicatorTypes.get(i-1);
            int index = i;
            rows.stream().filter(row -> row.getNumber() == index).findFirst()
                    .ifPresent(row -> setIndicatorTypeRows(f1125IndicatorType, row, f1125IndicatorTypes));
        }
        return f1125IndicatorTypes;
    }

    private void setIndicatorTypeRows(F1125IndicatorType f1125IndicatorType, F1125Row f1125Row, List<F1125IndicatorType> f1125IndicatorTypes) {
        List<String> rowNumbers = f1125Row.getRows();
        if(!CollectionUtils.isEmpty(rowNumbers)) {
            BigDecimal soldInceput = BigDecimal.ZERO;
            BigDecimal soldFinal = BigDecimal.ZERO;
            List<F1125IndicatorType> filteredTypes = f1125IndicatorTypes.stream()
                    .filter(indicatorType -> rowNumbers.contains(indicatorType.getCodRand())).collect(Collectors.toList());
            for (F1125IndicatorType filteredType : filteredTypes) {
                soldInceput = soldInceput.add(filteredType.getSoldInceput() != null ? filteredType.getSoldInceput() : BigDecimal.ZERO);
                soldFinal = soldFinal.add(filteredType.getSoldSfarsit() != null ? filteredType.getSoldSfarsit() : BigDecimal.ZERO);
            }
            f1125IndicatorType.setSoldInceput(soldInceput);
            f1125IndicatorType.setSoldSfarsit(soldFinal);
        }
    }

    private void setIndicatorTypeAccounts(F1125IndicatorType f1125IndicatorType, F1125Row row, Map<String, Map<Object, List<Cell>>> extractedColumns) {
        List<F1125Account> accounts = row.getAccounts();
        if(!CollectionUtils.isEmpty(accounts)) {
            BigDecimal soldInceput = BigDecimal.ZERO;
            BigDecimal soldFinal = BigDecimal.ZERO;
            for (F1125Account account : accounts) {
                String accountNumber = account.getNumber();
                Map<Object, List<Cell>> columns = extractedColumns.get("Clasa " + accountNumber.charAt(0));
                List<Cell> cells = columns.get(ColumnsF1125.SIMBOL);
                List<Cell> siDebitorCells = columns.get(ColumnsF1125.SI_SOLD);
                List<Cell> siCreditorCells = columns.get(ColumnsF1125.SI_INITIAL);
                List<Cell> sfDebitorCells = columns.get(ColumnsF1125.SF_SOLD);
                List<Cell> sfCreditorCells = columns.get(ColumnsF1125.SF_FINAL);

                for (Cell cell : cells) {
                    if(cell.getStringCellValue().startsWith(accountNumber)) {
                        int index = cells.indexOf(cell);
                        Cell siDebitorCell = siDebitorCells.get(index);
                        if(BigDecimal.valueOf(siDebitorCell.getNumericCellValue()).stripTrailingZeros().equals(BigDecimal.ZERO)) {
                            Cell siCreditorCell = siCreditorCells.get(index);
                            if(account.getSymbol().equals("+")) {
                                soldInceput = soldInceput.add(BigDecimal.valueOf(siCreditorCell.getNumericCellValue()));
                            } else {
                                soldInceput = soldInceput.subtract(BigDecimal.valueOf(siCreditorCell.getNumericCellValue()));
                            }
                        } else {
                            if(account.getSymbol().equals("+")) {
                                soldInceput = soldInceput.add(BigDecimal.valueOf(siDebitorCell.getNumericCellValue()));
                            } else {
                                soldInceput = soldInceput.subtract(BigDecimal.valueOf(siDebitorCell.getNumericCellValue()));
                            }
                        }

                        Cell sfDebitorCell = sfDebitorCells.get(index);
                        if(BigDecimal.valueOf(sfDebitorCell.getNumericCellValue()).stripTrailingZeros().equals(BigDecimal.ZERO)) {
                            Cell sfCreditorCell = sfCreditorCells.get(index);
                            if(account.getSymbol().equals("+")) {
                                soldFinal = soldFinal.add(BigDecimal.valueOf(sfCreditorCell.getNumericCellValue()));
                            } else {
                                soldFinal = soldFinal.subtract(BigDecimal.valueOf(sfCreditorCell.getNumericCellValue()));
                            }
                        } else {
                            if(account.getSymbol().equals("+")) {
                                soldFinal = soldFinal.add(BigDecimal.valueOf(sfDebitorCell.getNumericCellValue()));
                            } else {
                                soldFinal = soldFinal.subtract(BigDecimal.valueOf(sfDebitorCell.getNumericCellValue()));
                            }
                        }
                    }
                }
            }
            f1125IndicatorType.setSoldInceput(soldInceput.equals(ZERO_DECIMAL) ? null : soldInceput.stripTrailingZeros());
            f1125IndicatorType.setSoldSfarsit(soldFinal.equals(ZERO_DECIMAL) ? null : soldFinal.stripTrailingZeros());
        }
    }

    @Override
    public ConversionType getType() {
        return ConversionType.F1125;
    }

    private Map<Object, List<Cell>> filterClass(String className, Map<Object, List<Cell>> columns, Map<String, AccountSymbols> symbols, Map<String, String> exceptions) {

        List<Cell> symbolColumn = columns.get(ColumnsF1125.SIMBOL);
        List<Cell> siDebitorColumn = columns.get(ColumnsF1125.SI_SOLD);
        List<Cell> siCreditorColumn = columns.get(ColumnsF1125.SI_INITIAL);
        List<Cell> sfDebitorColumn = columns.get(ColumnsF1125.SF_SOLD);
        List<Cell> sfCreditorColumn = columns.get(ColumnsF1125.SF_FINAL);

        List<Cell> finalSiDebitorColumn = siDebitorColumn;
        List<Cell> finalSiCreditorColumn = siCreditorColumn;
        List<Cell> finalSfDebitorColumn = sfDebitorColumn;
        List<Cell> finalSfCreditorColumn = sfCreditorColumn;
        List<Cell> finalSymbolColumn = symbolColumn.stream()
                .filter(cell ->
                        (finalSiDebitorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() != 0) ||
                                (finalSiCreditorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() != 0) ||
                                (finalSfDebitorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() != 0) ||
                                (finalSfCreditorColumn.get(symbolColumn.indexOf(cell)).getNumericCellValue() != 0))
                .map(this::getCellTrimValue)
                .collect(Collectors.toList());

        List<Cell> filteredSymbolColumn = finalSymbolColumn.stream()
                .filter(cell -> filterByClass(className, cell, symbols, finalSymbolColumn, exceptions))
                .map(this::fillCell)
                .collect(Collectors.toList());
        siDebitorColumn = siDebitorColumn.stream()
                .filter(cell -> filterCell(cell, filteredSymbolColumn))
                .collect(Collectors.toList());
        siCreditorColumn = siCreditorColumn.stream()
                .filter(cell -> filterCell(cell, filteredSymbolColumn))
                .collect(Collectors.toList());
        sfDebitorColumn = sfDebitorColumn.stream()
                .filter(cell -> filterCell(cell, filteredSymbolColumn))
                .collect(Collectors.toList());
        sfCreditorColumn = sfCreditorColumn.stream()
                .filter(cell -> filterCell(cell, filteredSymbolColumn))
                .collect(Collectors.toList());

        columns.put(ColumnsF1125.SIMBOL, filteredSymbolColumn);
        columns.put(ColumnsF1125.SI_SOLD, siDebitorColumn);
        columns.put(ColumnsF1125.SI_INITIAL, siCreditorColumn);
        columns.put(ColumnsF1125.SF_SOLD, sfDebitorColumn);
        columns.put(ColumnsF1125.SF_FINAL, sfCreditorColumn);

        return columns;
    }

    private Cell fillCell(Cell cell) {
        String symbol = cell.getStringCellValue();
        if (symbol.length() <= 10) {
            symbol = symbol.length() < SYMBOL_LENGTH ? symbol.concat(ZERO.repeat(SYMBOL_LENGTH - symbol.length())) : symbol;
        }
        cell.setCellValue(symbol);
        return cell;
    }

    private void generateXml(FormData formData, HttpServletResponse response, F1125Type f1125Type, EmailDTO emailDTO) {

        try {
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBContext contextObj = JAXBContext.newInstance(F1125Type.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "mfp:anaf:dgti:f1125:declaratie:v1");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshallerObj.marshal(objectFactory.createF1125(f1125Type), byteArrayOutputStream);
            byte[] content = byteArrayOutputStream.toByteArray();
            IOUtils.copy(new ByteArrayInputStream(content), response.getOutputStream());
            List<Attachment> attachments = new ArrayList<>();
            attachments.add(new Attachment(formData.getXlsFile()
                    .getOriginalFilename(), formData.getXlsFile()
                    .getBytes()));
            attachments.add(new Attachment(F1125_RESULT_NAME, content));
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "F1125 generated with success for " + formData.getNumeIp(),
                    "F1125 file generated with success !!!", attachments)));
            logger.info("F1125 file generated with success!");
        } catch (Exception e) {
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "Error while generating F1125 for " + formData.getNumeIp(),
                    "Error while generating F1125 !!!", Collections.singletonList(new Attachment("error.txt", e.toString()
                            .getBytes())))));
            logger.error(e.getMessage());
        }
    }

    private F1125Type convertFromDTO(FormData formData) {
        F1125Type f1125Type = new F1125Type();
        f1125Type.setAn(formData.getAn());
        f1125Type.setCuiIp(formData.getCuiIp());
        f1125Type.setDataIntocmire(dateFormatter(formData.getDataDocument()));
        f1125Type.setLunaR(formData.getLunaR());
        f1125Type.setNumeIp(formData.getNumeIp());
        f1125Type.setDRec(formData.getdRec() ? 1 : 0);
        f1125Type.setSumaControl(Long.parseLong(formData.getCuiIp()));
        f1125Type.setFormularFaraValori(formData.getDocumentFaraValori());
        f1125Type.setCodSecBug("02");

        return f1125Type;
    }
}
