
package com.accounting.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private static final QName _F1102_QNAME = new QName("mfp:anaf:dgti:f1102:declaratie:v1", "f1102");

    private static final QName _F1115_QNAME = new QName("mfp:anaf:dgti:f1115:declaratie:v1", "f1115");

    public F1102Type createF1102Type() {
        return new F1102Type();
    }

    public ContType createContType() {
        return new ContType();
    }

    public F1115Type createF1115Type() {
        return new F1115Type();
    }

    public F1115TabelType createF1115TabelType() {
        return new F1115TabelType();
    }


    public F1115RandType createF1115RandType() {
        return new F1115RandType();
    }

    public F1115IndicatorType createF1115IndicatorType() {
        return new F1115IndicatorType();
    }

    @XmlElementDecl(namespace = "mfp:anaf:dgti:f1102:declaratie:v1", name = "f1102")
    public JAXBElement<F1102Type> createF1102(F1102Type value) {
        return new JAXBElement<>(_F1102_QNAME, F1102Type.class, null, value);
    }

    @XmlElementDecl(namespace = "mfp:anaf:dgti:f1115:declaratie:v1", name = "f1115")
    public JAXBElement<F1115Type> createF1115(F1115Type value) {
        return new JAXBElement<>(_F1115_QNAME, F1115Type.class, null, value);
    }
}
