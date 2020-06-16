package com.accounting.service;

import com.accounting.model.AccountSymbols;
import com.accounting.model.F1115Config;

import java.util.Map;

public interface ConfigsProviderService {
    Map<String, AccountSymbols> getSymbols();

    Map<String, String> getExceptions();

    F1115Config getF1115Config();
}
