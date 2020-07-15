package com.accounting.model;

public enum ColumnsF1125 {
    SIMBOL("Simbol"), SI_SOLD("Sold"), SI_INITIAL(" initial"), SF_SOLD("Sold"), SF_FINAL(" final");

    private final String definition;

    public String getDefinition()
    {
        return this.definition;
    }

    ColumnsF1125(String definition)
    {
        this.definition = definition;
    }
}
