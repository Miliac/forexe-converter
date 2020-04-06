package com.accounting.service;

import com.accounting.model.AccountSymbols;

import java.io.InputStream;
import java.util.Map;


public interface AccountSymbolsService {

    Map<String, AccountSymbols> read();

    InputStream readFile();

    boolean write(InputStream inputStream);
}
