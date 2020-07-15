package com.accounting.writer;

import com.accounting.model.F1115Config;
import com.accounting.service.ParserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class F1115ConfigWriter {

    private static final Logger logger = LogManager.getLogger(F1115ConfigWriter.class);

    private final ParserService parserService;

    @Autowired
    public F1115ConfigWriter(ParserService parserService) {
        this.parserService = parserService;
    }

    public boolean write(F1115Config config, String path) {
        boolean result = false;
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(path))) {
            byte[] content = parserService.toBytes(config);
            fileOutputStream.write(content);
            result = true;
            logger.info("F1115 config file wrote with success!");
        } catch (FileNotFoundException e) {
            logger.error("Could not found f1115 config file: {} ", e.getMessage());
        } catch (IOException e) {
            logger.error("Could not write f1115 config file: {} ", e.getMessage());
        }
        return result;
    }
}
