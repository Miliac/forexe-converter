package com.accounting.model;

import java.util.List;

public class F1115Config {

    private List<String> accounts;
    private double soldInitial;

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public double getSoldInitial() {
        return soldInitial;
    }

    public void setSoldInitial(double soldInitial) {
        this.soldInitial = soldInitial;
    }
}
