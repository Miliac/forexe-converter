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

import static com.accounting.config.Utils.F1125_RESULT_NAME;
import static com.accounting.config.Utils.XML_CONTENT_TYPE;

@Service
public class F1125ConversionService extends AbstractConversionService implements ConversionService  {

    private static final Logger logger = LogManager.getLogger(F1125ConversionService.class);

    F1125ConversionService(MailService mailService) {
        super(mailService);
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1125.xml");

        Map<String, Map<Columns, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile());
        if (!extractedColumns.isEmpty()) {
            F1115Type f1115Type = convertFromDTO(formData);
            generateXml(formData, response, f1115Type);
        } else {
            executor.submit(() -> mailService.sendMail("No extracted columns, F1125 file not generated for " + formData.getNumeIp(),
                    "No extracted columns, F1125 file not generated !!!", Collections.singletonList(new Attachment(F1125_RESULT_NAME, formData.toString()
                            .getBytes()))));
            logger.info("No extracted columns, F1125 file not generated!!!");
        }
    }

    @Override
    public ConversionType getType() {
        return ConversionType.F1125;
    }

    private void generateXml(FormData formData, HttpServletResponse response, F1115Type f1115Type) {

        try {
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBContext contextObj = JAXBContext.newInstance(F1115Type.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "mfp:anaf:dgti:f1125:declaratie:v1");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshallerObj.marshal(objectFactory.createF1115(f1115Type), byteArrayOutputStream);
            byte[] content = byteArrayOutputStream.toByteArray();
            IOUtils.copy(new ByteArrayInputStream(content), response.getOutputStream());
            List<Attachment> attachments = new ArrayList<>();
            attachments.add(new Attachment(formData.getXlsFile()
                    .getOriginalFilename(), formData.getXlsFile()
                    .getBytes()));
            attachments.add(new Attachment(F1125_RESULT_NAME, content));
            executor.submit(() -> mailService.sendMail("F1125 generated with success for " + formData.getNumeIp(),
                    "F1125 file generated with success !!!", attachments));
            logger.info("F1125 file generated with success!");
        } catch (Exception e) {
            executor.submit(() -> mailService.sendMail("Error while generating F1125 for " + formData.getNumeIp(),
                    "Error while generating F1125 !!!", Collections.singletonList(new Attachment("error.txt", e.toString()
                            .getBytes()))));
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
