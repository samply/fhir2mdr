//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.02.07 um 03:01:29 PM CET 
//


package de.samply.schema.mdr.common;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.samply.schema.mdr.common package. 
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

    private final static QName _PermissibleValue_QNAME = new QName("http://schema.samply.de/mdr/common", "permissibleValue");
    private final static QName _DataElement_QNAME = new QName("http://schema.samply.de/mdr/common", "dataElement");
    private final static QName _DescribedValueDomain_QNAME = new QName("http://schema.samply.de/mdr/common", "describedValueDomain");
    private final static QName _Catalog_QNAME = new QName("http://schema.samply.de/mdr/common", "catalog");
    private final static QName _CatalogValueDomain_QNAME = new QName("http://schema.samply.de/mdr/common", "catalogValueDomain");
    private final static QName _Code_QNAME = new QName("http://schema.samply.de/mdr/common", "code");
    private final static QName _ScopedIdentifier_QNAME = new QName("http://schema.samply.de/mdr/common", "scopedIdentifier");
    private final static QName _DataElementGroup_QNAME = new QName("http://schema.samply.de/mdr/common", "dataElementGroup");
    private final static QName _Definitions_QNAME = new QName("http://schema.samply.de/mdr/common", "definitions");
    private final static QName _Element_QNAME = new QName("http://schema.samply.de/mdr/common", "element");
    private final static QName _Namespace_QNAME = new QName("http://schema.samply.de/mdr/common", "namespace");
    private final static QName _EnumeratedValueDomain_QNAME = new QName("http://schema.samply.de/mdr/common", "enumeratedValueDomain");
    private final static QName _Record_QNAME = new QName("http://schema.samply.de/mdr/common", "record");
    private final static QName _Slots_QNAME = new QName("http://schema.samply.de/mdr/common", "slots");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.samply.schema.mdr.common
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Code }
     * 
     */
    public Code createCode() {
        return new Code();
    }

    /**
     * Create an instance of {@link CatalogValueDomain }
     * 
     */
    public CatalogValueDomain createCatalogValueDomain() {
        return new CatalogValueDomain();
    }

    /**
     * Create an instance of {@link DescribedValueDomain }
     * 
     */
    public DescribedValueDomain createDescribedValueDomain() {
        return new DescribedValueDomain();
    }

    /**
     * Create an instance of {@link Catalog }
     * 
     */
    public Catalog createCatalog() {
        return new Catalog();
    }

    /**
     * Create an instance of {@link PermissibleValue }
     * 
     */
    public PermissibleValue createPermissibleValue() {
        return new PermissibleValue();
    }

    /**
     * Create an instance of {@link DataElement }
     * 
     */
    public DataElement createDataElement() {
        return new DataElement();
    }

    /**
     * Create an instance of {@link Slots }
     * 
     */
    public Slots createSlots() {
        return new Slots();
    }

    /**
     * Create an instance of {@link Record }
     * 
     */
    public Record createRecord() {
        return new Record();
    }

    /**
     * Create an instance of {@link Namespace }
     * 
     */
    public Namespace createNamespace() {
        return new Namespace();
    }

    /**
     * Create an instance of {@link EnumeratedValueDomain }
     * 
     */
    public EnumeratedValueDomain createEnumeratedValueDomain() {
        return new EnumeratedValueDomain();
    }

    /**
     * Create an instance of {@link Export }
     * 
     */
    public Export createExport() {
        return new Export();
    }

    /**
     * Create an instance of {@link ScopedIdentifier }
     * 
     */
    public ScopedIdentifier createScopedIdentifier() {
        return new ScopedIdentifier();
    }

    /**
     * Create an instance of {@link DataElementGroup }
     * 
     */
    public DataElementGroup createDataElementGroup() {
        return new DataElementGroup();
    }

    /**
     * Create an instance of {@link Definitions }
     * 
     */
    public Definitions createDefinitions() {
        return new Definitions();
    }

    /**
     * Create an instance of {@link Slot }
     * 
     */
    public Slot createSlot() {
        return new Slot();
    }

    /**
     * Create an instance of {@link Definition }
     * 
     */
    public Definition createDefinition() {
        return new Definition();
    }

    /**
     * Create an instance of {@link Code.SubCode }
     * 
     */
    public Code.SubCode createCodeSubCode() {
        return new Code.SubCode();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PermissibleValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "permissibleValue", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<PermissibleValue> createPermissibleValue(PermissibleValue value) {
        return new JAXBElement<PermissibleValue>(_PermissibleValue_QNAME, PermissibleValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "dataElement", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<DataElement> createDataElement(DataElement value) {
        return new JAXBElement<DataElement>(_DataElement_QNAME, DataElement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescribedValueDomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "describedValueDomain", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<DescribedValueDomain> createDescribedValueDomain(DescribedValueDomain value) {
        return new JAXBElement<DescribedValueDomain>(_DescribedValueDomain_QNAME, DescribedValueDomain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Catalog }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "catalog", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<Catalog> createCatalog(Catalog value) {
        return new JAXBElement<Catalog>(_Catalog_QNAME, Catalog.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CatalogValueDomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "catalogValueDomain", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<CatalogValueDomain> createCatalogValueDomain(CatalogValueDomain value) {
        return new JAXBElement<CatalogValueDomain>(_CatalogValueDomain_QNAME, CatalogValueDomain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Code }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "code", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<Code> createCode(Code value) {
        return new JAXBElement<Code>(_Code_QNAME, Code.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScopedIdentifier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "scopedIdentifier", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<ScopedIdentifier> createScopedIdentifier(ScopedIdentifier value) {
        return new JAXBElement<ScopedIdentifier>(_ScopedIdentifier_QNAME, ScopedIdentifier.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataElementGroup }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "dataElementGroup", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<DataElementGroup> createDataElementGroup(DataElementGroup value) {
        return new JAXBElement<DataElementGroup>(_DataElementGroup_QNAME, DataElementGroup.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Definitions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "definitions")
    public JAXBElement<Definitions> createDefinitions(Definitions value) {
        return new JAXBElement<Definitions>(_Definitions_QNAME, Definitions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Element }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "element")
    public JAXBElement<Element> createElement(Element value) {
        return new JAXBElement<Element>(_Element_QNAME, Element.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Namespace }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "namespace", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<Namespace> createNamespace(Namespace value) {
        return new JAXBElement<Namespace>(_Namespace_QNAME, Namespace.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnumeratedValueDomain }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "enumeratedValueDomain", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<EnumeratedValueDomain> createEnumeratedValueDomain(EnumeratedValueDomain value) {
        return new JAXBElement<EnumeratedValueDomain>(_EnumeratedValueDomain_QNAME, EnumeratedValueDomain.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Record }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "record", substitutionHeadNamespace = "http://schema.samply.de/mdr/common", substitutionHeadName = "element")
    public JAXBElement<Record> createRecord(Record value) {
        return new JAXBElement<Record>(_Record_QNAME, Record.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Slots }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schema.samply.de/mdr/common", name = "slots")
    public JAXBElement<Slots> createSlots(Slots value) {
        return new JAXBElement<Slots>(_Slots_QNAME, Slots.class, null, value);
    }

}
