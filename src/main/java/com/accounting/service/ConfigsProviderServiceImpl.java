package com.accounting.service;

import com.accounting.model.AccountSymbols;
import com.accounting.model.F1115Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConfigsProviderServiceImpl implements ConfigsProviderService {

    private static final Logger logger = LogManager.getLogger(ConfigsProviderServiceImpl.class);
    private final AccountSymbolsService accountSymbolsService;
    private final ExceptionsService exceptionsService;
    private final F1115ConfigService configService;
    private Map<String, AccountSymbols> symbols;
    private Map<String, String> exceptions;
    private F1115Config f1115Config;

    public ConfigsProviderServiceImpl(AccountSymbolsService accountSymbolsService, ExceptionsService exceptionsService, F1115ConfigService configService) {
        this.accountSymbolsService = accountSymbolsService;
        this.exceptionsService = exceptionsService;
        this.configService = configService;
        init();
    }

    private void init() {
        readSymbols();
        readExceptions();
        readConfig();
    }

    @Override
    public Map<String, AccountSymbols> getSymbols() {
        return symbols;
    }

    @Override
    public Map<String, String> getExceptions() {
        return exceptions;
    }

    @Override
    public F1115Config getF1115Config(){
        return f1115Config;
    }

    private void readSymbols() {
        symbols = accountSymbolsService.read();
        logger.info("Account symbols file loaded in memory!");
    }

    private void readExceptions() {
        exceptions = exceptionsService.read();
        logger.info("Exceptions file loaded in memory!");
    }

    private void readConfig() {
        f1115Config = configService.read();
        logger.info("F1115 config file loaded in memory!");
    }
}
