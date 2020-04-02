package com.accounting.model;

import com.accounting.validators.ValidXLSX;
import org.springframework.web.multipart.MultipartFile;


public class F1102TypeDTO {

    private int an;
    private int lunaR;
    private String dataDocument;
    private String numeIp;
    private String cuiIp;
    private String sector;
    private int documentCuValori;
    private long sumaControl;

    @ValidXLSX(message = "Please chose a valid XLSX file.")
    private MultipartFile xlsFile;

    public F1102TypeDTO() {
    }

    public F1102TypeDTO(int an, int lunaR, String dataDocument, String numeIp, String cuiIp, String sector, int documentCuValori, long sumaControl, MultipartFile xlsFile) {
        this.an = an;
        this.lunaR = lunaR;
        this.dataDocument = dataDocument;
        this.numeIp = numeIp;
        this.cuiIp = cuiIp;
        this.sector = sector;
        this.documentCuValori = documentCuValori;
        this.sumaControl = sumaControl;
        this.xlsFile = xlsFile;
    }

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

    public int getDocumentCuValori() {
        return documentCuValori;
    }

    public void setDocumentCuValori(int documentCuValori) {
        this.documentCuValori = documentCuValori;
    }

    public long getSumaControl() {
        return sumaControl;
    }

    public void setSumaControl(long sumaControl) {
        this.sumaControl = sumaControl;
    }

    public MultipartFile getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(MultipartFile xlsFile) {
        this.xlsFile = xlsFile;
    }
}
