
package com.accounting.model;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for F1102Type complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="F1102Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cont" type="{mfp:anaf:dgti:f1102:declaratie:v1}ContType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="suma_control" use="required" type="{mfp:anaf:dgti:f1102:declaratie:v1}IntPoz17SType" />
 *       &lt;attribute name="d_rec" use="required" type="{mfp:anaf:dgti:f1102:declaratie:v1}IntPoz1SType" />
 *       &lt;attribute name="nume_ip" use="required" type="{mfp:anaf:dgti:f1102:declaratie:v1}Str100" />
 *       &lt;attribute name="cui_ip" use="required" type="{mfp:anaf:dgti:f1102:declaratie:v1}CuiSType" />
 *       &lt;attribute name="an" use="required" type="{mfp:anaf:dgti:f1102:declaratie:v1}IntPoz4SType" />
 *       &lt;attribute name="luna_r" use="required" type="{mfp:anaf:dgti:f1102:declaratie:v1}IntPoz2SType" />
 *       &lt;attribute name="data_document" use="required" type="{mfp:anaf:dgti:f1102:declaratie:v1}DateSType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F1102Type", namespace = "mfp:anaf:dgti:f1102:declaratie:v1", propOrder = {
        "cont"
})
public class F1102Type {

    @XmlElement(namespace = "mfp:anaf:dgti:f1102:declaratie:v1", required = true)
    private List<F1102ContType> cont;
    @XmlAttribute(name = "suma_control", required = true)
    private BigInteger sumaControl;
    @XmlAttribute(name = "luna_r", required = true)
    private int lunaR;
    @XmlAttribute(name = "an", required = true)
    private int an;
    @XmlAttribute(name = "data_document", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    private String dataDocument;
    @XmlAttribute(name = "d_rec", required = true)
    private int dRec;
    @XmlAttribute(name = "nume_ip", required = true)
    private String numeIp;
    @XmlAttribute(name = "cui_ip", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    private String cuiIp;
    @XmlAttribute(name = "formular_fara_valori", required = true)
    private int formularFaraValori;

    /**
     * Gets the value of the cont property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cont property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCont().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link F1102ContType }
     */
    public List<F1102ContType> getCont() {
        if (cont == null) {
            cont = new ArrayList<>();
        }
        return this.cont;
    }

    public void setCont(List<F1102ContType> contTypes) {
        this.cont = contTypes;
    }

    public int getFormularFaraValori() {
        return formularFaraValori;
    }

    public void setFormularFaraValori(int formularFaraValori) {
        this.formularFaraValori = formularFaraValori;
    }

    /**
     * Gets the value of the sumaControl property.
     */
    public BigInteger getSumaControl() {
        return sumaControl;
    }

    /**
     * Sets the value of the sumaControl property.
     */
    public void setSumaControl(BigInteger value) {
        this.sumaControl = value;
    }

    /**
     * Gets the value of the dRec property.
     */
    public int getDRec() {
        return dRec;
    }

    /**
     * Sets the value of the dRec property.
     */
    public void setDRec(int value) {
        this.dRec = value;
    }

    /**
     * Gets the value of the numeIp property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNumeIp() {
        return numeIp;
    }

    /**
     * Sets the value of the numeIp property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNumeIp(String value) {
        this.numeIp = value;
    }

    /**
     * Gets the value of the cuiIp property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCuiIp() {
        return cuiIp;
    }

    /**
     * Sets the value of the cuiIp property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCuiIp(String value) {
        this.cuiIp = value;
    }

    /**
     * Gets the value of the an property.
     */
    public int getAn() {
        return an;
    }

    /**
     * Sets the value of the an property.
     */
    public void setAn(int value) {
        this.an = value;
    }

    /**
     * Gets the value of the lunaR property.
     */
    public int getLunaR() {
        return lunaR;
    }

    /**
     * Sets the value of the lunaR property.
     */
    public void setLunaR(int value) {
        this.lunaR = value;
    }

    /**
     * Gets the value of the dataDocument property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDataDocument() {
        return dataDocument;
    }

    /**
     * Sets the value of the dataDocument property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDataDocument(String value) {
        this.dataDocument = value;
    }

}
