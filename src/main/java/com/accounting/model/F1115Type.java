
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
 * <p>Java class for F1115Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="F1115Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="f1115_tabel" type="{mfp:anaf:dgti:f1115:declaratie:v1}F1115_tabelType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="suma_control" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}IntPoz17SType" />
 *       &lt;attribute name="d_rec" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}IntPoz1SType" />
 *       &lt;attribute name="nume_ip" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str100" />
 *       &lt;attribute name="cui_ip" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}CuiSType" />
 *       &lt;attribute name="an" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}IntPoz4SType" />
 *       &lt;attribute name="luna_r" type="{mfp:anaf:dgti:f1115:declaratie:v1}IntPoz2SType" />
 *       &lt;attribute name="data_intocmire" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}DateSType" />
 *       &lt;attribute name="cod_sec_bug" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="formular_fara_valori" type="{mfp:anaf:dgti:f1115:declaratie:v1}IntPoz1SType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F1115Type", namespace = "mfp:anaf:dgti:f1115:declaratie:v1", propOrder = {
    "f1115Tabel"
})
public class F1115Type {

    @XmlElement(name = "f1115_tabel", namespace = "mfp:anaf:dgti:f1115:declaratie:v1")
    protected List<F1115TabelType> f1115Tabel;
    @XmlAttribute(name = "suma_control", required = true)
    protected long sumaControl;
    @XmlAttribute(name = "d_rec", required = true)
    protected int dRec;
    @XmlAttribute(name = "nume_ip", required = true)
    protected String numeIp;
    @XmlAttribute(name = "cui_ip", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String cuiIp;
    @XmlAttribute(name = "an", required = true)
    protected int an;
    @XmlAttribute(name = "luna_r")
    protected Integer lunaR;
    @XmlAttribute(name = "data_intocmire", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dataIntocmire;
    @XmlAttribute(name = "cod_sec_bug", required = true)
    protected String codSecBug;
    @XmlAttribute(name = "formular_fara_valori")
    protected Integer formularFaraValori;

    /**
     * Gets the value of the f1115Tabel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the f1115Tabel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getF1115Tabel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link F1115TabelType }
     * 
     * 
     */
    public List<F1115TabelType> getF1115Tabel() {
        if (f1115Tabel == null) {
            f1115Tabel = new ArrayList<F1115TabelType>();
        }
        return this.f1115Tabel;
    }

    public void setF1115Tabel(List<F1115TabelType> f1115Tabel) {
        this.f1115Tabel = f1115Tabel;
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
     * Gets the value of the lunaR property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLunaR() {
        return lunaR;
    }

    /**
     * Sets the value of the lunaR property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLunaR(Integer value) {
        this.lunaR = value;
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
