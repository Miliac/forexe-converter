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

import static com.accounting.config.Utils.F1127_RESULT_NAME;
import static com.accounting.config.Utils.XML_CONTENT_TYPE;

@Service
public class F1127ConversionService extends AbstractConversionService implements ConversionService {

    private static final Logger logger = LogManager.getLogger(F1127ConversionService.class);

    F1127ConversionService(MailService mailService) {
        super(mailService);
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response, EmailDTO emailDTO) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1127.xml");

        Map<String, Map<Columns, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile());
        if (!extractedColumns.isEmpty()) {
            F1127Type f1127Type = convertFromDTO(formData);
            generateXml(formData, response, f1127Type, emailDTO);
        } else {
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "No extracted columns, F1127 file not generated for " + formData.getNumeIp(),
                    "No extracted columns, F1127 file not generated !!!", Collections.singletonList(new Attachment(F1127_RESULT_NAME, formData.toString()
                            .getBytes())))));
            logger.info("No extracted columns, F1127 file not generated!!!");
        }
    }

    @Override
    public ConversionType getType() {
        return ConversionType.F1127;
    }

    private void generateXml(FormData formData, HttpServletResponse response, F1127Type f1127Type, EmailDTO emailDTO) {

        try {
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBContext contextObj = JAXBContext.newInstance(F1127Type.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "mfp:anaf:dgti:f1127:declaratie:v1");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshallerObj.marshal(objectFactory.createF1127(f1127Type), byteArrayOutputStream);
            byte[] content = byteArrayOutputStream.toByteArray();
            IOUtils.copy(new ByteArrayInputStream(content), response.getOutputStream());
            List<Attachment> attachments = new ArrayList<>();
            attachments.add(new Attachment(formData.getXlsFile()
                    .getOriginalFilename(), formData.getXlsFile()
                    .getBytes()));
            attachments.add(new Attachment(F1127_RESULT_NAME, content));
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "F1127 generated with success for " + formData.getNumeIp(),
                    "F1127 file generated with success !!!", attachments)));
            logger.info("F1127 file generated with success!");
        } catch (Exception e) {
            executor.submit(() -> mailService.sendMail(buildEmailDto(emailDTO,  "Error while generating F1127 for " + formData.getNumeIp(),
                    "Error while generating F1127 !!!", Collections.singletonList(new Attachment("error.txt", e.toString()
                            .getBytes())))));
            logger.error(e.getMessage());
        }
    }

    private F1127Type convertFromDTO(FormData formData) {
        F1127Type f1127Type = new F1127Type();
        f1127Type.setAn(formData.getAn());
        f1127Type.setCuiIp(formData.getCuiIp());
        f1127Type.setLunaR(formData.getLunaR());
        f1127Type.setNumeIp(formData.getNumeIp());
        f1127Type.setDRec(formData.getdRec() ? 1 : 0);
        f1127Type.setSumaControl(Long.parseLong(formData.getCuiIp()));

        return f1127Type;
    }
}
