//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.02.07 um 03:01:29 PM CET 
//


package de.samply.schema.mdr.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schema.samply.de/mdr/common}element" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "element"
})
@XmlRootElement(name = "export")
public class Export
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElementRef(name = "element", namespace = "http://schema.samply.de/mdr/common", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends Element>> element;

    /**
     * Gets the value of the element property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the element property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ScopedIdentifier }{@code >}
     * {@link JAXBElement }{@code <}{@link EnumeratedValueDomain }{@code >}
     * {@link JAXBElement }{@code <}{@link Catalog }{@code >}
     * {@link JAXBElement }{@code <}{@link Element }{@code >}
     * {@link JAXBElement }{@code <}{@link DataElementGroup }{@code >}
     * {@link JAXBElement }{@code <}{@link Code }{@code >}
     * {@link JAXBElement }{@code <}{@link PermissibleValue }{@code >}
     * {@link JAXBElement }{@code <}{@link DataElement }{@code >}
     * {@link JAXBElement }{@code <}{@link DescribedValueDomain }{@code >}
     * {@link JAXBElement }{@code <}{@link Record }{@code >}
     * {@link JAXBElement }{@code <}{@link Namespace }{@code >}
     * {@link JAXBElement }{@code <}{@link CatalogValueDomain }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends Element>> getElement() {
        if (element == null) {
            element = new ArrayList<JAXBElement<? extends Element>>();
        }
        return this.element;
    }

}
