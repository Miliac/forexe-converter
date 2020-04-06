package com.accounting.model;

import java.util.ArrayList;
import java.util.List;

public class AccountSymbols {

    private List<String> accountSymbols;
    private List<String> accountSymbolsEndInFourZeros;
    private List<String> accountSymbolsEndInTwoZeros;
    private List<String> accountSymbolsWithCF;
    private List<String> accountSymbolsWithCFAndCE;

    public AccountSymbols() {
        accountSymbols = new ArrayList<>();
        accountSymbolsEndInFourZeros = new ArrayList<>();
        accountSymbolsEndInTwoZeros = new ArrayList<>();
        accountSymbolsWithCF = new ArrayList<>();
        accountSymbolsWithCFAndCE = new ArrayList<>();
    }

    public List<String> getAccountSymbols() {
        return accountSymbols;
    }

    public void setAccountSymbols(List<String> accountSymbols) {
        this.accountSymbols = accountSymbols;
    }

    public List<String> getAccountSymbolsEndInFourZeros() {
        return accountSymbolsEndInFourZeros;
    }

    public void setAccountSymbolsEndInFourZeros(List<String> accountSymbolsEndInFourZeros) {
        this.accountSymbolsEndInFourZeros = accountSymbolsEndInFourZeros;
    }

    public List<String> getAccountSymbolsEndInTwoZeros() {
        return accountSymbolsEndInTwoZeros;
    }

    public void setAccountSymbolsEndInTwoZeros(List<String> accountSymbolsEndInTwoZeros) {
        this.accountSymbolsEndInTwoZeros = accountSymbolsEndInTwoZeros;
    }

    public List<String> getAccountSymbolsWithCF() {
        return accountSymbolsWithCF;
    }

    public void setAccountSymbolsWithCF(List<String> accountSymbolsWithCF) {
        this.accountSymbolsWithCF = accountSymbolsWithCF;
    }

    public List<String> getAccountSymbolsWithCFAndCE() {
        return accountSymbolsWithCFAndCE;
    }

    public void setAccountSymbolsWithCFAndCE(List<String> accountSymbolsWithCFAndCE) {
        this.accountSymbolsWithCFAndCE = accountSymbolsWithCFAndCE;
    }

}
