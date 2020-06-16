package com.accounting.service;

import com.accounting.model.Columns;
import com.accounting.model.ConversionType;
import com.accounting.model.F1115Config;
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

    private F1115Config f1115Config;
    private F1115ConfigService configService;

    public F1115ConversionService(AccountSymbolsService accountSymbolsService, ExceptionsService exceptionsService,
                                  MailService mailService, F1115ConfigService configService) {
        super(accountSymbolsService, exceptionsService, mailService);
        this.configService = configService;
        readConfig();
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1115.xml");

        Map<String, Map<Columns, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile());


    }

    @Override
    public ConversionType getType() {
        return ConversionType.F1115;
    }

    public void readConfig() {
        f1115Config = configService.read();
        logger.info("F1115 config file loaded in memory!");
    }
}
