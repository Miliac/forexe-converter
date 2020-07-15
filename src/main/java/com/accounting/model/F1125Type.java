
package com.accounting.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for F1125Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="F1125Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="f1125_sursa" type="{mfp:anaf:dgti:f1125:declaratie:v1}F1125_sursaType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="luna_r" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}IntPoz2SType" />
 *       &lt;attribute name="suma_control" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}IntPoz17SType" />
 *       &lt;attribute name="d_rec" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}IntPoz1SType" />
 *       &lt;attribute name="cui_ip" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}CuiSType" />
 *       &lt;attribute name="nume_ip" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}Str100" />
 *       &lt;attribute name="an" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}IntPoz4SType" />
 *       &lt;attribute name="data_intocmire" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}DateSType" />
 *       &lt;attribute name="cod_sec_bug" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}Str2" />
 *       &lt;attribute name="formular_fara_valori" type="{mfp:anaf:dgti:f1125:declaratie:v1}IntPoz1SType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F1125Type", namespace = "mfp:anaf:dgti:f1125:declaratie:v1", propOrder = {
    "f1125Sursa"
})
public class F1125Type {

    @XmlElement(name = "f1125_sursa", namespace = "mfp:anaf:dgti:f1125:declaratie:v1")
    protected List<F1125SursaType> f1125Sursa;
    @XmlAttribute(name = "luna_r", required = true)
    protected int lunaR;
    @XmlAttribute(name = "suma_control", required = true)
    protected long sumaControl;
    @XmlAttribute(name = "d_rec", required = true)
    protected int dRec;
    @XmlAttribute(name = "cui_ip", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String cuiIp;
    @XmlAttribute(name = "nume_ip", required = true)
    protected String numeIp;
    @XmlAttribute(name = "an", required = true)
    protected int an;
    @XmlAttribute(name = "data_intocmire", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dataIntocmire;
    @XmlAttribute(name = "cod_sec_bug", required = true)
    protected String codSecBug;
    @XmlAttribute(name = "formular_fara_valori")
    protected Integer formularFaraValori;

    /**
     * Gets the value of the f1125Sursa property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the f1125Sursa property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getF1125Sursa().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link F1125SursaType }
     * 
     * 
     */
    public List<F1125SursaType> getF1125Sursa() {
        if (f1125Sursa == null) {
            f1125Sursa = new ArrayList<F1125SursaType>();
        }
        return this.f1125Sursa;
    }

    public void setF1125Sursa(List<F1125SursaType> f1125Sursa) {
        this.f1125Sursa = f1125Sursa;
    }

    /**
     * Gets the value of the lunaR property.
     * 
     */
    public int getLunaR() {
        return lunaR;
    }

    /**
     * Sets the value of the lunaR property.
     * 
     */
    public void setLunaR(int value) {
        this.lunaR = value;
    }

    /**
     * Gets the value of the sumaControl property.
     * 
     */
    public long getSumaControl() {
        return sumaControl;
    }

    /**
     * Sets the value of the sumaControl property.
     * 
     */
    public void setSumaControl(long value) {
        this.sumaControl = value;
    }

    /**
     * Gets the value of the dRec property.
     * 
     */
    public int getDRec() {
        return dRec;
    }

    /**
     * Sets the value of the dRec property.
     * 
     */
    public void setDRec(int value) {
        this.dRec = value;
    }

    /**
     * Gets the value of the cuiIp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuiIp() {
        return cuiIp;
    }

    /**
     * Sets the value of the cuiIp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuiIp(String value) {
        this.cuiIp = value;
    }

    /**
     * Gets the value of the numeIp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeIp() {
        return numeIp;
    }

    /**
     * Sets the value of the numeIp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeIp(String value) {
        this.numeIp = value;
    }

    /**
     * Gets the value of the an property.
     * 
     */
    public int getAn() {
        return an;
    }

    /**
     * Sets the value of the an property.
     * 
     */
    public void setAn(int value) {
        this.an = value;
    }

    /**
     * Gets the value of the dataIntocmire property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataIntocmire() {
        return dataIntocmire;
    }

    /**
     * Sets the value of the dataIntocmire property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataIntocmire(String value) {
        this.dataIntocmire = value;
    }

    /**
     * Gets the value of the codSecBug property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSecBug() {
        return codSecBug;
    }

    /**
     * Sets the value of the codSecBug property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSecBug(String value) {
        this.codSecBug = value;
    }

    /**
     * Gets the value of the formularFaraValori property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFormularFaraValori() {
        return formularFaraValori;
    }

    /**
     * Sets the value of the formularFaraValori property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFormularFaraValori(Integer value) {
        this.formularFaraValori = value;
    }

}
