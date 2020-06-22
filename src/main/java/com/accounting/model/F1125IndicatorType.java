
package com.accounting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for F1125_indicatorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="F1125_indicatorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="cod_rand" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}Str10" />
 *       &lt;attribute name="sold_inceput" type="{mfp:anaf:dgti:f1125:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="sold_sfarsit" type="{mfp:anaf:dgti:f1125:declaratie:v1}DblNeg12_2SType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F1125_indicatorType", namespace = "mfp:anaf:dgti:f1125:declaratie:v1")
public class F1125IndicatorType {

    @XmlAttribute(name = "cod_rand", required = true)
    protected String codRand;
    @XmlAttribute(name = "sold_inceput")
    protected Double soldInceput;
    @XmlAttribute(name = "sold_sfarsit")
    protected Double soldSfarsit;

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
     * Gets the value of the soldInceput property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSoldInceput() {
        return soldInceput;
    }

    /**
     * Sets the value of the soldInceput property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSoldInceput(Double value) {
        this.soldInceput = value;
    }

    /**
     * Gets the value of the soldSfarsit property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSoldSfarsit() {
        return soldSfarsit;
    }

    /**
     * Sets the value of the soldSfarsit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSoldSfarsit(Double value) {
        this.soldSfarsit = value;
    }

}
