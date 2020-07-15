
package com.accounting.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for F1125_sursaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="F1125_sursaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="f1125_indicator" type="{mfp:anaf:dgti:f1125:declaratie:v1}F1125_indicatorType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="cod_sursa" use="required" type="{mfp:anaf:dgti:f1125:declaratie:v1}Str2" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F1125_sursaType", namespace = "mfp:anaf:dgti:f1125:declaratie:v1", propOrder = {
    "f1125Indicator"
})
public class F1125SursaType {

    @XmlElement(name = "f1125_indicator", namespace = "mfp:anaf:dgti:f1125:declaratie:v1", required = true)
    protected List<F1125IndicatorType> f1125Indicator;
    @XmlAttribute(name = "cod_sursa", required = true)
    protected String codSursa;

    /**
     * Gets the value of the f1125Indicator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the f1125Indicator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getF1125Indicator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link F1125IndicatorType }
     * 
     * 
     */
    public List<F1125IndicatorType> getF1125Indicator() {
        if (f1125Indicator == null) {
            f1125Indicator = new ArrayList<>();
        }
        return this.f1125Indicator;
    }

    public void setF1125Indicator(List<F1125IndicatorType> f1125Indicator) {
        this.f1125Indicator = f1125Indicator;
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

}
