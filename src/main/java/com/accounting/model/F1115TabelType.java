
package com.accounting.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for F1115_tabelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="F1115_tabelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="f1115_rand" type="{mfp:anaf:dgti:f1115:declaratie:v1}F1115_randType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="cod_prog_bug" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="cod_sfin" use="required" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str2" />
 *       &lt;attribute name="sect" type="{mfp:anaf:dgti:f1115:declaratie:v1}Str1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F1115_tabelType", namespace = "mfp:anaf:dgti:f1115:declaratie:v1", propOrder = {
    "f1115Rand"
})
public class F1115TabelType {

    @XmlElement(name = "f1115_rand", namespace = "mfp:anaf:dgti:f1115:declaratie:v1", required = true)
    protected List<F1115RandType> f1115Rand;
    @XmlAttribute(name = "cod_prog_bug", required = true)
    protected String codProgBug;
    @XmlAttribute(name = "cod_sfin", required = true)
    protected String codSfin;
    @XmlAttribute(name = "sect")
    protected String sect;

    /**
     * Gets the value of the f1115Rand property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the f1115Rand property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getF1115Rand().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link F1115RandType }
     * 
     * 
     */
    public List<F1115RandType> getF1115Rand() {
        if (f1115Rand == null) {
            f1115Rand = new ArrayList<F1115RandType>();
        }
        return this.f1115Rand;
    }

    /**
     * Gets the value of the codProgBug property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodProgBug() {
        return codProgBug;
    }

    /**
     * Sets the value of the codProgBug property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodProgBug(String value) {
        this.codProgBug = value;
    }

    /**
     * Gets the value of the codSfin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSfin() {
        return codSfin;
    }

    /**
     * Sets the value of the codSfin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSfin(String value) {
        this.codSfin = value;
    }

    /**
     * Gets the value of the sect property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSect() {
        return sect;
    }

    /**
     * Sets the value of the sect property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSect(String value) {
        this.sect = value;
    }

}
