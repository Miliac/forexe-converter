package com.accounting.model;

import org.springframework.web.multipart.MultipartFile;


public class FormData {

    private int an;
    private int lunaR;
    private String dataDocument;
    private String numeIp;
    private String cuiIp;
    private String sector;
    private int documentFaraValori;
    private boolean dRec;
    private ConversionType conversionType;

    public FormData(int an, int lunaR, String dataDocument, String numeIp, String cuiIp) {
        this.an = an;
        this.lunaR = lunaR;
        this.dataDocument = dataDocument;
        this.numeIp = numeIp;
        this.cuiIp = cuiIp;
    }

    private MultipartFile xlsFile;

    public int getAn() {
        return an;
    }

    public void setAn(int an) {
        this.an = an;
    }

    public int getLunaR() {
        return lunaR;
    }

    public void setLunaR(int lunaR) {
        this.lunaR = lunaR;
    }

    public String getDataDocument() {
        return dataDocument;
    }

    public void setDataDocument(String dataDocument) {
        this.dataDocument = dataDocument;
    }

    public String getNumeIp() {
        return numeIp;
    }

    public void setNumeIp(String numeIp) {
        this.numeIp = numeIp;
    }

    public String getCuiIp() {
        return cuiIp;
    }

    public void setCuiIp(String cuiIp) {
        this.cuiIp = cuiIp;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getDocumentFaraValori() {
        return documentFaraValori;
    }

    public void setDocumentFaraValori(int documentCuValori) {
        this.documentFaraValori = documentCuValori;
    }

    public boolean getdRec() {
        return dRec;
    }

    public void setdRec(boolean dRec) {
        this.dRec = dRec;
    }

    public MultipartFile getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(MultipartFile xlsFile) {
        this.xlsFile = xlsFile;
    }

    public boolean isdRec() {
        return dRec;
    }

    public void setConversionType(ConversionType conversionType) {
        this.conversionType = conversionType;
    }

    public ConversionType getConversionType() {
        return conversionType;
    }
}
