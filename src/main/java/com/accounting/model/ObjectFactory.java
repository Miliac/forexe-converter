
package com.accounting.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the models package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _F1102_QNAME = new QName("mfp:anaf:dgti:f1102:declaratie:v1", "f1102");


    /**
     * Create an instance of {@link F1102Type }
     * 
     */
    public F1102Type createF1102Type() {
        return new F1102Type();
    }

    /**
     * Create an instance of {@link ContType }
     * 
     */
    public ContType createContType() {
        return new ContType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link F1102Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "mfp:anaf:dgti:f1102:declaratie:v1", name = "f1102")
    public JAXBElement<F1102Type> createF1102(F1102Type value) {
        return new JAXBElement<>(_F1102_QNAME, F1102Type.class, null, value);
    }

}
