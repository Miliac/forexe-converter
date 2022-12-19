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
        logger.info("Trying to load Account symbols file in memory!");
        symbols = accountSymbolsService.read();
    }

    private void readExceptions() {
        logger.info("Trying to load Exceptions file in memory!");
        exceptions = exceptionsService.read();
    }

    private void readConfig() {
        logger.info("Trying to load F1115 config file in memory!");
        f1115Config = configService.read();
    }
}
