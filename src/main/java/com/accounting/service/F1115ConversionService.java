package com.accounting.service;

import com.accounting.model.Columns;
import com.accounting.model.FormData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

import static com.accounting.config.Utils.XML_CONTENT_TYPE;

@Service
public class F1115ConversionService extends AbstractConversionService implements ConversionService {

    private static final Logger logger = LogManager.getLogger(F1115ConversionService.class);

    public F1115ConversionService(AccountSymbolsService accountSymbolsService, ExceptionsService exceptionsService, MailService mailService) {
        super(accountSymbolsService, exceptionsService, mailService);
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1102.xml");

        Map<String, Map<Columns, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile());

    }
}
