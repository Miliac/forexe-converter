package com.accounting.model;

public enum Columns {

    SIMBOL("Simbol"), DEBITOR("Rulaj"), CREDITOR("cumulat");

    private String definition;

    public String getDefinition()
    {
        return this.definition;
    }

    private Columns(String definition)
    {
        this.definition = definition;
    }
}
