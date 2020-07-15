package com.accounting.service;

import com.accounting.model.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.accounting.config.Utils.*;
import static com.accounting.model.Columns.SIMBOL;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
public class F1102ConversionService extends AbstractConversionService implements ConversionService {

    private static final Logger logger = LogManager.getLogger(F1102ConversionService.class);

    private String codSector;

    private BigDecimal sumaControl;

    public F1102ConversionService(MailService mailService, ConfigsProviderService configsProviderService) {
        super(mailService, configsProviderService);
        sumaControl = BigDecimal.ZERO;
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response, EmailDTO emailDTO) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1102.xml");

        this.codSector = getCodSector(formData.getSector());

        if(formData.getDocumentFaraValori()==0) {

            Map<String, Map<Object, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile(), ConversionType.F1102);

            if (!extractedColumns.isEmpty()) {
                extractedColumns.forEach((className, columns) -> extractedColumns.put(className, filterClass(className, columns, configsProviderService.getSymbols(), configsProviderService.getExceptions())));

                List<F1102ContType> contTypes = getContType(extractedColumns);

                generateXml(formData, response, contTypes, emailDTO);
            } else {

                executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO, "No extracted columns, F1102 file not generated for " + formData.getNumeIp(),
                        "No extracted columns, F1102 file not generated !!!", Collections.singletonList(new Attachment(F1102_RESULT_NAME, formData.toString()
                                .getBytes())))));
                logger.info("No extracted columns, F1102 file not generated!!!");
            }
        } else {
            F1102ContType contType = new F1102ContType();
            contType.setRulajCred(BigDecimal.ZERO);
            contType.setRulajDeb(BigDecimal.ZERO);
            contType.setSimbolPCont("1000000");
            contType.setCodSector(codSector);
            contType.setCodSursa(String.valueOf('G'));
            contType.setStrCont(contType.getSimbolPCont() + codSector + contType.getCodSursa() + X.repeat(30));
            generateXml(formData, response, List.of(contType), emailDTO);
        }
    }

    @Override
    public ConversionType getType() {
        return ConversionType.F1102;
    }

    private void generateXml(FormData formData, HttpServletResponse response, List<F1102ContType> contTypes, EmailDTO emailDTO) {
        F1102Type f1102Type = convertFromDTO(formData);
        List<F1102ContType> copyContTypes = new ArrayList<>(contTypes);
        f1102Type.setCont(contTypes.stream()
                .filter(contType -> removeDuplicatesAndSumValues(contType, copyContTypes))
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
            attachments.add(new Attachment(formData.getXlsFile()
                    .getOriginalFilename(), formData.getXlsFile()
                    .getBytes()));
            attachments.add(new Attachment(F1102_RESULT_NAME, content));
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO, "F1102 generated with success for " + formData.getNumeIp(),
                    "F1102 file generated with success !!!", attachments)));
            logger.info("F1102 file generated with success!");
        } catch (Exception e) {
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO, "Error while generating F1102 for " + formData.getNumeIp(),
                    "Error while generating F1102 !!!", Collections.singletonList(new Attachment("error.txt", e.toString()
                            .getBytes())))));
            logger.error(e.getMessage());
        }
    }

    private boolean removeDuplicatesAndSumValues(F1102ContType contType, List<F1102ContType> contTypes) {
        int indexCont = contTypes.indexOf(contType);
        if (indexCont > 0) {
            F1102ContType previousCont = contTypes.get(indexCont - 1);
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

    private Map<Object, List<Cell>> filterClass(String className, Map<Object, List<Cell>> columns, Map<String, AccountSymbols> symbols, Map<String, String> exceptions) {

        List<Cell> symbolColumn = columns.get(SIMBOL);
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

        columns.put(SIMBOL, filteredSymbolColumn);
        columns.put(Columns.DEBITOR, debitorColumn);
        columns.put(Columns.CREDITOR, creditorColumn);

        return columns;
    }

    private List<F1102ContType> getContType(Map<String, Map<Object, List<Cell>>> extractedColumns) {
        Map<String, F1102ContType> contTypes = new LinkedHashMap<>();
        extractedColumns.forEach((key, value) -> value.forEach((column, cells) -> {
            switch ((Columns) column) {
                case SIMBOL:
                    cells.forEach(cell -> {
                        F1102ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new F1102ContType());
                        fillContType(contType, cell.getStringCellValue());
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                case DEBITOR:
                    cells.forEach(cell -> {
                        BigDecimal rulDeb = BigDecimal.valueOf(cell.getNumericCellValue());
                        sumaControl = sumaControl.add(rulDeb);
                        F1102ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new F1102ContType());
                        contType.setRulajDeb(rulDeb.equals(ZERO_DECIMAL) ? null : rulDeb.stripTrailingZeros());
                        contTypes.put(String.valueOf(cell.getRowIndex()), contType);
                    });
                    break;
                case CREDITOR:
                    cells.forEach(cell -> {
                        BigDecimal rulCred = BigDecimal.valueOf(cell.getNumericCellValue());
                        sumaControl = sumaControl.add(rulCred);
                        F1102ContType contType = contTypes.getOrDefault(String.valueOf(cell.getRowIndex()), new F1102ContType());
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

    private F1102Type convertFromDTO(FormData formData) {
        F1102Type f1102Type = new F1102Type();
        f1102Type.setAn(formData.getAn());
        f1102Type.setCuiIp(formData.getCuiIp());
        f1102Type.setDataDocument(dateFormatter(formData.getDataDocument()));
        f1102Type.setLunaR(formData.getLunaR());
        f1102Type.setNumeIp(formData.getNumeIp());
        f1102Type.setDRec(formData.getdRec() ? 1 : 0);
        f1102Type.setSumaControl(sumaControl.add(BigDecimal.valueOf(Long.parseLong(formData.getCuiIp()))).toBigInteger());
        f1102Type.setFormularFaraValori(formData.getDocumentFaraValori());

        return f1102Type;
    }

    private String getCodSector(String codSector) {
        return codSector.substring(0,codSector.indexOf(LINE) - 1);
    }

    private void fillContType(F1102ContType contType, String symbol) {
        contType.setCodSector(codSector);
        contType.setCodSursa(String.valueOf('G'));
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
