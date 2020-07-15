package com.accounting.model;

import java.util.List;

public class F1125Row {

    private String number;
    private List<F1125Account> accounts;
    private List<String> rows;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<F1125Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<F1125Account> accounts) {
        this.accounts = accounts;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }
}
