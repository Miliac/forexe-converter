
package com.accounting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for ContType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="simbol_p_cont" use="required" type="{mfp:anaf:dgti:f1127:declaratie:v1}Str7" />
 *       &lt;attribute name="cod_sursa" use="required" type="{mfp:anaf:dgti:f1127:declaratie:v1}Str2" />
 *       &lt;attribute name="cod_sector" use="required" type="{mfp:anaf:dgti:f1127:declaratie:v1}Str2" />
 *       &lt;attribute name="cf" type="{mfp:anaf:dgti:f1127:declaratie:v1}Str6" />
 *       &lt;attribute name="ce" type="{mfp:anaf:dgti:f1127:declaratie:v1}Str6" />
 *       &lt;attribute name="cod_esa" type="{mfp:anaf:dgti:f1127:declaratie:v1}Str5" />
 *       &lt;attribute name="cui_p" type="{mfp:anaf:dgti:f1127:declaratie:v1}CuiSType" />
 *       &lt;attribute name="str_cont" use="required" type="{mfp:anaf:dgti:f1127:declaratie:v1}Str40" />
 *       &lt;attribute name="rulaj_deb" type="{mfp:anaf:dgti:f1127:declaratie:v1}DblNeg12_2SType" />
 *       &lt;attribute name="rulaj_cred" type="{mfp:anaf:dgti:f1127:declaratie:v1}DblNeg12_2SType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContType", namespace = "mfp:anaf:dgti:f1127:declaratie:v1")
public class ContType {

    @XmlAttribute(name = "simbol_p_cont", required = true)
    protected String simbolPCont;
    @XmlAttribute(name = "cod_sursa", required = true)
    protected String codSursa;
    @XmlAttribute(name = "cod_sector", required = true)
    protected String codSector;
    @XmlAttribute(name = "cf")
    protected String cf;
    @XmlAttribute(name = "ce")
    protected String ce;
    @XmlAttribute(name = "cod_esa")
    protected String codEsa;
    @XmlAttribute(name = "cui_p")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String cuiP;
    @XmlAttribute(name = "str_cont", required = true)
    protected String strCont;
    @XmlAttribute(name = "rulaj_deb")
    protected Double rulajDeb;
    @XmlAttribute(name = "rulaj_cred")
    protected Double rulajCred;

    /**
     * Gets the value of the simbolPCont property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimbolPCont() {
        return simbolPCont;
    }

    /**
     * Sets the value of the simbolPCont property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimbolPCont(String value) {
        this.simbolPCont = value;
    }

    /**
     * Gets the value of the codSursa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSursa() {
        return codSursa;
    }

    /**
     * Sets the value of the codSursa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSursa(String value) {
        this.codSursa = value;
    }

    /**
     * Gets the value of the codSector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSector() {
        return codSector;
    }

    /**
     * Sets the value of the codSector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSector(String value) {
        this.codSector = value;
    }

    /**
     * Gets the value of the cf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCf() {
        return cf;
    }

    /**
     * Sets the value of the cf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCf(String value) {
        this.cf = value;
    }

    /**
     * Gets the value of the ce property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCe() {
        return ce;
    }

    /**
     * Sets the value of the ce property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCe(String value) {
        this.ce = value;
    }

    /**
     * Gets the value of the codEsa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEsa() {
        return codEsa;
    }

    /**
     * Sets the value of the codEsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEsa(String value) {
        this.codEsa = value;
    }

    /**
     * Gets the value of the cuiP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuiP() {
        return cuiP;
    }

    /**
     * Sets the value of the cuiP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuiP(String value) {
        this.cuiP = value;
    }

    /**
     * Gets the value of the strCont property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrCont() {
        return strCont;
    }

    /**
     * Sets the value of the strCont property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrCont(String value) {
        this.strCont = value;
    }

    /**
     * Gets the value of the rulajDeb property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRulajDeb() {
        return rulajDeb;
    }

    /**
     * Sets the value of the rulajDeb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRulajDeb(Double value) {
        this.rulajDeb = value;
    }

    /**
     * Gets the value of the rulajCred property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRulajCred() {
        return rulajCred;
    }

    /**
     * Sets the value of the rulajCred property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRulajCred(Double value) {
        this.rulajCred = value;
    }

}
