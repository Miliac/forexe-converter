
package com.accounting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for F1115_indicatorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="F1115_indicatorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="cf_cap" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="cf_scap" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="cf_par" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="ce_titlu" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="ce_art" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="ce_alin" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="sold_init_debit" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="sold_init_credit" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cum_debit" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cum_credit" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cum_debit_trez" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cum_credit_trez" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="sold_final_debit" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="sold_final_credit" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F1115_indicatorType", namespace = "mfp:anaf:dgti:f1115:declaratie:v1")
public class F1115IndicatorType {

    @XmlAttribute(name = "cf_cap", required = true)
    protected String cfCap;
    @XmlAttribute(name = "cf_scap", required = true)
    protected String cfScap;
    @XmlAttribute(name = "cf_par", required = true)
    protected String cfPar;
    @XmlAttribute(name = "ce_titlu", required = true)
    protected String ceTitlu;
    @XmlAttribute(name = "ce_art", required = true)
    protected String ceArt;
    @XmlAttribute(name = "ce_alin", required = true)
    protected String ceAlin;
    @XmlAttribute(name = "sold_init_debit")
    protected Double soldInitDebit;
    @XmlAttribute(name = "sold_init_credit")
    protected Double soldInitCredit;
    @XmlAttribute(name = "rulaj_cum_debit")
    protected Double rulajCumDebit;
    @XmlAttribute(name = "rulaj_cum_credit")
    protected Double rulajCumCredit;
    @XmlAttribute(name = "rulaj_cum_debit_trez")
    protected Double rulajCumDebitTrez;
    @XmlAttribute(name = "rulaj_cum_credit_trez")
    protected Double rulajCumCreditTrez;
    @XmlAttribute(name = "sold_final_debit")
    protected Double soldFinalDebit;
    @XmlAttribute(name = "sold_final_credit")
    protected Double soldFinalCredit;

    /**
     * Gets the value of the cfCap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfCap() {
        return cfCap;
    }

    /**
     * Sets the value of the cfCap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfCap(String value) {
        this.cfCap = value;
    }

    /**
     * Gets the value of the cfScap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfScap() {
        return cfScap;
    }

    /**
     * Sets the value of the cfScap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfScap(String value) {
        this.cfScap = value;
    }

    /**
     * Gets the value of the cfPar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfPar() {
        return cfPar;
    }

    /**
     * Sets the value of the cfPar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfPar(String value) {
        this.cfPar = value;
    }

    /**
     * Gets the value of the ceTitlu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCeTitlu() {
        return ceTitlu;
    }

    /**
     * Sets the value of the ceTitlu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCeTitlu(String value) {
        this.ceTitlu = value;
    }

    /**
     * Gets the value of the ceArt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCeArt() {
        return ceArt;
    }

    /**
     * Sets the value of the ceArt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCeArt(String value) {
        this.ceArt = value;
    }

    /**
     * Gets the value of the ceAlin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCeAlin() {
        return ceAlin;
    }

    /**
     * Sets the value of the ceAlin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCeAlin(String value) {
        this.ceAlin = value;
    }

    /**
     * Gets the value of the soldInitDebit property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSoldInitDebit() {
        return soldInitDebit;
    }

    /**
     * Sets the value of the soldInitDebit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSoldInitDebit(Double value) {
        this.soldInitDebit = value;
    }

    /**
     * Gets the value of the soldInitCredit property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSoldInitCredit() {
        return soldInitCredit;
    }

    /**
     * Sets the value of the soldInitCredit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSoldInitCredit(Double value) {
        this.soldInitCredit = value;
    }

    /**
     * Gets the value of the rulajCumDebit property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRulajCumDebit() {
        return rulajCumDebit;
    }

    /**
     * Sets the value of the rulajCumDebit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRulajCumDebit(Double value) {
        this.rulajCumDebit = value;
    }

    /**
     * Gets the value of the rulajCumCredit property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRulajCumCredit() {
        return rulajCumCredit;
    }

    /**
     * Sets the value of the rulajCumCredit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRulajCumCredit(Double value) {
        this.rulajCumCredit = value;
    }

    /**
     * Gets the value of the rulajCumDebitTrez property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRulajCumDebitTrez() {
        return rulajCumDebitTrez;
    }

    /**
     * Sets the value of the rulajCumDebitTrez property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRulajCumDebitTrez(Double value) {
        this.rulajCumDebitTrez = value;
    }

    /**
     * Gets the value of the rulajCumCreditTrez property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRulajCumCreditTrez() {
        return rulajCumCreditTrez;
    }

    /**
     * Sets the value of the rulajCumCreditTrez property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRulajCumCreditTrez(Double value) {
        this.rulajCumCreditTrez = value;
    }

    /**
     * Gets the value of the soldFinalDebit property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSoldFinalDebit() {
        return soldFinalDebit;
    }

    /**
     * Sets the value of the soldFinalDebit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSoldFinalDebit(Double value) {
        this.soldFinalDebit = value;
    }

    /**
     * Gets the value of the soldFinalCredit property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSoldFinalCredit() {
        return soldFinalCredit;
    }

    /**
     * Sets the value of the soldFinalCredit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSoldFinalCredit(Double value) {
        this.soldFinalCredit = value;
    }

}
