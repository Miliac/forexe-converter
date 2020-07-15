package com.accounting.writer;

import com.accounting.model.F1125Row;
import com.accounting.service.ParserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class F1125ConfigWriter {

    private static final Logger logger = LogManager.getLogger(F1125ConfigWriter.class);

    private final ParserService parserService;

    @Autowired
    public F1125ConfigWriter(ParserService parserService) {
        this.parserService = parserService;
    }

    public boolean write(List<F1125Row> config, String path) {
        boolean result = false;
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(path))) {
            byte[] content = parserService.toBytes(config);
            fileOutputStream.write(content);
            result = true;
            logger.info("F1125 config file wrote with success!");
        } catch (FileNotFoundException e) {
            logger.error("Could not found f1125 config file: {} ", e.getMessage());
        } catch (IOException e) {
            logger.error("Could not write f1125 config file: {} ", e.getMessage());
        }
        return result;
    }
}
