
package com.accounting.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for F1115_randType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="F1115_randType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="f1115_indicator" type="{mfp:anaf:dgti:f1115:declaratie:v1}F1115_indicatorType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="cod_rand" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="sold_init_debit_total" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="sold_init_credit_total" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cum_debit_total" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cum_credit_total" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cum_debit_trez_total" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cum_credit_trez_total" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="sold_final_debit_total" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="sold_final_credit_total" type="{mfp:anaf:dgti:f1115:declaratie:v1}DblNeg12_2SType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F1115_randType", namespace = "mfp:anaf:dgti:f1115:declaratie:v1", propOrder = {
    "f1115Indicator"
})
public class F1115RandType {

    @XmlElement(name = "f1115_indicator", namespace = "mfp:anaf:dgti:f1115:declaratie:v1")
    protected List<F1115IndicatorType> f1115Indicator;
    @XmlAttribute(name = "cod_rand", required = true)
    protected String codRand;
    @XmlAttribute(name = "sold_init_debit_total")
    protected double soldInitDebitTotal;
    @XmlAttribute(name = "sold_init_credit_total")
    protected double soldInitCreditTotal;
    @XmlAttribute(name = "rulaj_cum_debit_total")
    protected double rulajCumDebitTotal;
    @XmlAttribute(name = "rulaj_cum_credit_total")
    protected double rulajCumCreditTotal;
    @XmlAttribute(name = "rulaj_cum_debit_trez_total")
    protected double rulajCumDebitTrezTotal;
    @XmlAttribute(name = "rulaj_cum_credit_trez_total")
    protected double rulajCumCreditTrezTotal;
    @XmlAttribute(name = "sold_final_debit_total")
    protected double soldFinalDebitTotal;
    @XmlAttribute(name = "sold_final_credit_total")
    protected double soldFinalCreditTotal;
    @XmlAttribute(name = "exec_banci_comerc_total")
    protected int execBanciComercTotal;

    public int getExecBanciComercTotal() {
        return execBanciComercTotal;
    }

    public void setExecBanciComercTotal(int execBanciComercTotal) {
        this.execBanciComercTotal = execBanciComercTotal;
    }

    /**
     * Gets the value of the f1115Indicator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the f1115Indicator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getF1115Indicator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link F1115IndicatorType }
     * 
     * 
     */
    public List<F1115IndicatorType> getF1115Indicator() {
        if (f1115Indicator == null) {
            f1115Indicator = new ArrayList<F1115IndicatorType>();
        }
        return this.f1115Indicator;
    }

    /**
     * Gets the value of the codRand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodRand() {
        return codRand;
    }

    /**
     * Sets the value of the codRand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodRand(String value) {
        this.codRand = value;
    }

    /**
     * Gets the value of the soldInitDebitTotal property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getSoldInitDebitTotal() {
        return soldInitDebitTotal;
    }

    /**
     * Sets the value of the soldInitDebitTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setSoldInitDebitTotal(double value) {
        this.soldInitDebitTotal = value;
    }

    /**
     * Gets the value of the soldInitCreditTotal property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getSoldInitCreditTotal() {
        return soldInitCreditTotal;
    }

    /**
     * Sets the value of the soldInitCreditTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setSoldInitCreditTotal(double value) {
        this.soldInitCreditTotal = value;
    }

    /**
     * Gets the value of the rulajCumDebitTotal property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getRulajCumDebitTotal() {
        return rulajCumDebitTotal;
    }

    /**
     * Sets the value of the rulajCumDebitTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setRulajCumDebitTotal(double value) {
        this.rulajCumDebitTotal = value;
    }

    /**
     * Gets the value of the rulajCumCreditTotal property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getRulajCumCreditTotal() {
        return rulajCumCreditTotal;
    }

    /**
     * Sets the value of the rulajCumCreditTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setRulajCumCreditTotal(double value) {
        this.rulajCumCreditTotal = value;
    }

    /**
     * Gets the value of the rulajCumDebitTrezTotal property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getRulajCumDebitTrezTotal() {
        return rulajCumDebitTrezTotal;
    }

    /**
     * Sets the value of the rulajCumDebitTrezTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setRulajCumDebitTrezTotal(double value) {
        this.rulajCumDebitTrezTotal = value;
    }

    /**
     * Gets the value of the rulajCumCreditTrezTotal property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getRulajCumCreditTrezTotal() {
        return rulajCumCreditTrezTotal;
    }

    /**
     * Sets the value of the rulajCumCreditTrezTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setRulajCumCreditTrezTotal(double value) {
        this.rulajCumCreditTrezTotal = value;
    }

    /**
     * Gets the value of the soldFinalDebitTotal property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getSoldFinalDebitTotal() {
        return soldFinalDebitTotal;
    }

    /**
     * Sets the value of the soldFinalDebitTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setSoldFinalDebitTotal(double value) {
        this.soldFinalDebitTotal = value;
    }

    /**
     * Gets the value of the soldFinalCreditTotal property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getSoldFinalCreditTotal() {
        return soldFinalCreditTotal;
    }

    /**
     * Sets the value of the soldFinalCreditTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setSoldFinalCreditTotal(double value) {
        this.soldFinalCreditTotal = value;
    }

}
