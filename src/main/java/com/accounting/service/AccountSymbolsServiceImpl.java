package com.accounting.service;

import com.accounting.model.AccountSymbols;
import com.accounting.reader.AccountSymbolsReader;
import com.accounting.writer.ClassSymbolsWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class AccountSymbolsServiceImpl implements AccountSymbolsService {

    private static final Logger logger = LogManager.getLogger(AccountSymbolsServiceImpl.class);
    private static final String RESOURCE_PATH = "account-symbols.json";

    private AccountSymbolsReader reader;
    private ClassSymbolsWriter writer;
    private ParserService parserService;

    @Autowired
    public AccountSymbolsServiceImpl(AccountSymbolsReader reader, ClassSymbolsWriter writer, ParserService parserService) {
        this.reader = reader;
        this.writer = writer;
        this.parserService = parserService;
    }

    @Override
    public Map<String, AccountSymbols> read() {
        return reader.read(RESOURCE_PATH);
    }

    @Override
    public InputStream readFile() {
        Map<String, AccountSymbols> accountSymbols = reader.read(RESOURCE_PATH);
        byte[] accountSymbolsContent = new byte[0];
        try {
            accountSymbolsContent = parserService.toBytes(accountSymbols);
        } catch (IOException e) {
            logger.error("Could not parse content into account symbols: {} ", e.getMessage());
        }
        logger.info("Account symbols file loaded in memory!");
        return new ByteArrayInputStream(accountSymbolsContent);
    }

    @Override
    public boolean write(InputStream inputStream) {
        Map<String, AccountSymbols> classSymbols = null;
        try {
            classSymbols = parserService.toObject(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            logger.error("Could not parse content into account symbols: {} ", e.getMessage());
        }
        logger.info("Account symbols file wrote in memory!");
        return writer.write(classSymbols, RESOURCE_PATH);
    }
}
