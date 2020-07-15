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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.accounting.config.Utils.F1125_RESULT_NAME;
import static com.accounting.config.Utils.XML_CONTENT_TYPE;

@Service
public class F1125ConversionService extends AbstractConversionService implements ConversionService  {

    private static final Logger logger = LogManager.getLogger(F1125ConversionService.class);

    F1125ConversionService(MailService mailService, ConfigsProviderService configsProviderService) {
        super(mailService, configsProviderService);
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response, EmailDTO emailDTO) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1125.xml");

        Map<String, Map<Object, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile(), ConversionType.F1125);
        if (!extractedColumns.isEmpty()) {

            extractedColumns.forEach((className, columns) -> extractedColumns.put(className, filterClass(className, columns, configsProviderService.getSymbols(), configsProviderService.getExceptions())));

            F1125Type f1125Type = convertFromDTO(formData);
            generateXml(formData, response, f1125Type, emailDTO);
        } else {
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "No extracted columns, F1125 file not generated for " + formData.getNumeIp(),
                    "No extracted columns, F1125 file not generated !!!", Collections.singletonList(new Attachment(F1125_RESULT_NAME, formData.toString()
                            .getBytes())))));
            logger.info("No extracted columns, F1125 file not generated!!!");
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

        return f1125Type;
    }
}
