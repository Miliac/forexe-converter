package com.accounting.service;

import com.accounting.model.AccountSymbols;
import com.accounting.reader.XLSReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractConversionService {
    private static final Logger logger = LogManager.getLogger(AbstractConversionService.class);

    Map<String, AccountSymbols> symbols;
    Map<String, String> exceptions;

    XLSReader xlsReader;
    MailService mailService;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private AccountSymbolsService accountSymbolsService;
    private ExceptionsService exceptionsService;

    AbstractConversionService(AccountSymbolsService accountSymbolsService, ExceptionsService exceptionsService, MailService mailService) {
        this.accountSymbolsService = accountSymbolsService;
        this.exceptionsService = exceptionsService;
        this.mailService = mailService;
        xlsReader = new XLSReader();
        init();
    }

    private void init() {
        readSymbols();
        readExceptions();
    }

    public void readSymbols() {
        symbols = accountSymbolsService.read();
        logger.info("Account symbols file loaded in memory!");
    }

    public void readExceptions() {
        exceptions = exceptionsService.read();
        logger.info("Exceptions file loaded in memory!");
    }
}
