package com.accounting.writer;

import com.accounting.service.ParserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class ExceptionsWriter {

    private static final Logger logger = LogManager.getLogger(ExceptionsWriter.class);

    private ParserService parserService;

    @Autowired
    public ExceptionsWriter(ParserService parserService) {
        this.parserService = parserService;
    }

    public boolean write(Map<String, String> classSymbols, String path) {
        boolean result = false;
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(path))) {
            byte[] content = parserService.toBytes(classSymbols);
            fileOutputStream.write(content);
            result = true;
            logger.info("Exceptions file wrote with success!");
        } catch (FileNotFoundException e) {
            logger.error("Could not found exceptions file: {} ", e.getMessage());
        } catch (IOException e) {
            logger.error("Could not write exceptions file: {} ", e.getMessage());
        }
        return result;
    }
}
