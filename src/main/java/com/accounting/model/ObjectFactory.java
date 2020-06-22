
package com.accounting.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private static final QName _F1102_QNAME = new QName("mfp:anaf:dgti:f1102:declaratie:v1", "f1102");

    private static final QName _F1115_QNAME = new QName("mfp:anaf:dgti:f1115:declaratie:v1", "f1115");

    private static final  QName _F1125_QNAME = new QName("mfp:anaf:dgti:f1125:declaratie:v1", "f1125");

    private static final QName _F1127_QNAME = new QName("mfp:anaf:dgti:f1127:declaratie:v1", "f1127");

    public F1102Type createF1102Type() {
        return new F1102Type();
    }

    public F1102ContType createF1102ContType() {
        return new F1102ContType();
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


    public F1125Type createF1125Type() {
        return new F1125Type();
    }

    public F1125IndicatorType createF1125IndicatorType() {
        return new F1125IndicatorType();
    }

    public F1125SursaType createF1125SursaType() {
        return new F1125SursaType();
    }

    public F1127Type createF1127Type() {
        return new F1127Type();
    }

    @XmlElementDecl(namespace = "mfp:anaf:dgti:f1102:declaratie:v1", name = "f1102")
    public JAXBElement<F1102Type> createF1102(F1102Type value) {
        return new JAXBElement<>(_F1102_QNAME, F1102Type.class, null, value);
    }

    @XmlElementDecl(namespace = "mfp:anaf:dgti:f1115:declaratie:v1", name = "f1115")
    public JAXBElement<F1115Type> createF1115(F1115Type value) {
        return new JAXBElement<>(_F1115_QNAME, F1115Type.class, null, value);
    }

    @XmlElementDecl(namespace = "mfp:anaf:dgti:f1125:declaratie:v1", name = "f1125")
    public JAXBElement<F1125Type> createF1125(F1125Type value) {
        return new JAXBElement<>(_F1125_QNAME, F1125Type.class, null, value);
    }

    @XmlElementDecl(namespace = "mfp:anaf:dgti:f1127:declaratie:v1", name = "f1127")
    public JAXBElement<F1127Type> createF1127(F1127Type value) {
        return new JAXBElement<>(_F1127_QNAME, F1127Type.class, null, value);
    }
}
