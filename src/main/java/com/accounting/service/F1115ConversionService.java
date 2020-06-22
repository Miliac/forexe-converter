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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.accounting.config.Utils.F1115_RESULT_NAME;
import static com.accounting.config.Utils.XML_CONTENT_TYPE;

@Service
public class F1115ConversionService extends AbstractConversionService implements ConversionService {

    private static final Logger logger = LogManager.getLogger(F1115ConversionService.class);

    private F1115Config f1115Config;

    public F1115ConversionService(MailService mailService, ConfigsProviderService configsProviderService) {
        super(mailService);
        this.f1115Config = configsProviderService.getF1115Config();
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response, EmailDTO emailDTO) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1115.xml");

        Map<String, Map<Columns, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile());

        if (!extractedColumns.isEmpty()) {
            Map<Columns, List<Cell>> filtered;
            filtered = extractedColumns.entrySet().stream()
                    .filter(entry -> entry.getKey().equals("Clasa 5"))
                    .map(entry -> entry.getValue().entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                    .findFirst().orElseGet(LinkedHashMap::new);

            List<Cell> symbolColumn = filtered.get(Columns.SIMBOL);
            List<Cell> debitorColumn = filtered.get(Columns.DEBITOR);
            List<Cell> creditorColumn = filtered.get(Columns.CREDITOR);

            List<Cell> finalSymbolColumn = symbolColumn.stream()
                    .map(this::getCellTrimValue)
                    .filter(cell -> f1115Config.getAccounts().contains(cell.getStringCellValue()))
                    .collect(Collectors.toList());

            debitorColumn = debitorColumn.stream()
                    .filter(cell -> filterCell(cell, finalSymbolColumn))
                    .collect(Collectors.toList());
            creditorColumn = creditorColumn.stream()
                    .filter(cell -> filterCell(cell, finalSymbolColumn))
                    .collect(Collectors.toList());

            AtomicReference<BigDecimal> sumaDebitor = new AtomicReference<>(BigDecimal.ZERO);
            debitorColumn.forEach(cell -> sumaDebitor.set(sumaDebitor.get().add(BigDecimal.valueOf(cell.getNumericCellValue()))));
            AtomicReference<BigDecimal> sumaCreditor = new AtomicReference<>(BigDecimal.ZERO);
            creditorColumn.forEach(cell -> sumaCreditor.set(sumaCreditor.get().add(BigDecimal.valueOf(cell.getNumericCellValue()))));
            BigDecimal soldFinal = BigDecimal.valueOf(f1115Config.getSoldInitial()).add(sumaDebitor.get()).subtract(sumaCreditor.get());

            F1115Type f1115Type = convertFromDTO(formData);
            f1115Type.setCodSecBug(f1115Config.getCodSecBug());
            F1115TabelType f1115TabelType = new F1115TabelType();
            F1115RandType f1115RandTypeVenituri = new F1115RandType();
            f1115RandTypeVenituri.setCodRand("1");
            F1115RandType f1115RandTypeCheltuieli = new F1115RandType();
            f1115RandTypeCheltuieli.setCodRand("2");
            F1115RandType f1115RandTypeDisponibilitati = new F1115RandType();
            f1115RandTypeDisponibilitati.setCodRand("3");
            f1115RandTypeDisponibilitati.setSoldInitCreditTotal(f1115Config.getSoldInitial());
            f1115RandTypeDisponibilitati.setRulajCumDebitTotal(sumaDebitor.get().doubleValue());
            f1115RandTypeDisponibilitati.setRulajCumCreditTotal(sumaCreditor.get().doubleValue());
            f1115RandTypeDisponibilitati.setSoldFinalCreditTotal(soldFinal.doubleValue());
            f1115TabelType.setF1115Rand(List.of(f1115RandTypeVenituri, f1115RandTypeCheltuieli, f1115RandTypeDisponibilitati));
            f1115TabelType.setCodProgBug("0000000000");
            f1115TabelType.setCodSfin("G");
            f1115TabelType.setSect("F");
            f1115Type.setF1115Tabel(Collections.singletonList(f1115TabelType));
            generateXml(formData, response, f1115Type, emailDTO);
        } else {
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "No extracted columns, F1115 file not generated for " + formData.getNumeIp(),
                    "No extracted columns, F1115 file not generated !!!", Collections.singletonList(new Attachment(F1115_RESULT_NAME, formData.toString()
                            .getBytes())))));
            logger.info("No extracted columns, F1115 file not generated!!!");
        }
    }

    @Override
    public ConversionType getType() {
        return ConversionType.F1115;
    }

    private void generateXml(FormData formData, HttpServletResponse response, F1115Type f1115Type, EmailDTO emailDTO) {

        try {
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBContext contextObj = JAXBContext.newInstance(F1115Type.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "mfp:anaf:dgti:f1115:declaratie:v1");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshallerObj.marshal(objectFactory.createF1115(f1115Type), byteArrayOutputStream);
            byte[] content = byteArrayOutputStream.toByteArray();
            IOUtils.copy(new ByteArrayInputStream(content), response.getOutputStream());
            List<Attachment> attachments = new ArrayList<>();
            attachments.add(new Attachment(formData.getXlsFile()
                    .getOriginalFilename(), formData.getXlsFile()
                    .getBytes()));
            attachments.add(new Attachment(F1115_RESULT_NAME, content));
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO, "F1115 generated with success for " + formData.getNumeIp(),
                    "F1115 file generated with success !!!", attachments)));
            logger.info("F1115 file generated with success!");
        } catch (Exception e) {
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "Error while generating F1115 for " + formData.getNumeIp(),
                    "Error while generating F1115 !!!", Collections.singletonList(new Attachment("error.txt", e.toString()
                            .getBytes())))));
            logger.error(e.getMessage());
        }
    }

    private F1115Type convertFromDTO(FormData formData) {
        F1115Type f1115Type = new F1115Type();
        f1115Type.setAn(formData.getAn());
        f1115Type.setCuiIp(formData.getCuiIp());
        f1115Type.setDataIntocmire(dateFormatter(formData.getDataDocument()));
        f1115Type.setLunaR(formData.getLunaR());
        f1115Type.setNumeIp(formData.getNumeIp());
        f1115Type.setDRec(formData.getdRec() ? 1 : 0);
        f1115Type.setSumaControl(Long.parseLong(formData.getCuiIp()));
        f1115Type.setFormularFaraValori(formData.getDocumentFaraValori());

        return f1115Type;
    }


}
