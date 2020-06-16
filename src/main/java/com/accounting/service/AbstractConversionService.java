package com.accounting.service;

import com.accounting.reader.XLSReader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractConversionService {
    XLSReader xlsReader;
    MailService mailService;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    AbstractConversionService(MailService mailService) {
        this.mailService = mailService;
        xlsReader = new XLSReader();
    }
}
