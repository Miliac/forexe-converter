package com.accounting.writer;

import com.accounting.model.AccountSymbols;
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
public class ClassSymbolsWriter {

    private static final Logger logger = LogManager.getLogger(ClassSymbolsWriter.class);

    private final ParserService parserService;

    @Autowired
    public ClassSymbolsWriter(ParserService parserService) {
        this.parserService = parserService;
    }

    public boolean write(Map<String, AccountSymbols> classSymbols, String path) {
        boolean result = false;
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(path))) {
            byte[] content = parserService.toBytes(classSymbols);
            fileOutputStream.write(content);
            result = true;
            logger.info("Account symbols file wrote with success!");
        } catch (FileNotFoundException e) {
            logger.error("Could not found account symbols file: {} ", e.getMessage());
        } catch (IOException e) {
            logger.error("Could not write account symbols file: {} ", e.getMessage());
        }
        return result;
    }
}
