package de.samply.fhir2mdr.xml;

import de.samply.fhir2mdr.model.*;
import de.samply.fhir2mdr.model.DataElement;
import de.samply.fhir2mdr.model.Element;
import de.samply.fhir2mdr.model.Namespace;
import de.samply.fhir2mdr.model.PermissibleValue;
import de.samply.mdr.xsd.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public class ModelToXSDObjects {

    private String namespaceUUID;

    private ObjectFactory objectFactory = new ObjectFactory();

    private int runningId = 0;

    public Export convert(Namespace namespace){

        Export export = new Export();
        convert(namespace,export);
        return export;
    }

    private void convert(Namespace namespace, Export export){

        ObjectFactory objectFactory = new ObjectFactory();
        de.samply.mdr.xsd.Namespace xmlNamespace = new de.samply.mdr.xsd.Namespace();

        setNewUUID(xmlNamespace);
        this.namespaceUUID = xmlNamespace.getUuid();

        xmlNamespace.setName(namespace.getName());

        xmlNamespace.setDefinitions(convertDefinitions(namespace,this.namespaceUUID));

        export.getElement().add(objectFactory.createNamespace(xmlNamespace));

        for(ElementWithSlots elem: namespace.getMembers()){
            if(elem.getClass().equals(Group.class)){
                Group group = (Group) elem;
                convert(group,export);
            }
            if(elem.getClass().equals(DataElement.class)){
                DataElement de = (DataElement) elem;
                convert(de,export);
            }
        }
    }

    private String convert(Group group, Export export){

        //Each group consists of the group itself....
        DataElementGroup xmlGroup = new DataElementGroup();
        setNewUUID(xmlGroup);

        export.getElement().add(objectFactory.createDataElementGroup(xmlGroup));

        //And an scopedIdentifier linked by UUIDs
       ScopedIdentifier groupIdentifier = extractScopedIdentifer(group,xmlGroup.getUuid());

        export.getElement().add(objectFactory.createScopedIdentifier(groupIdentifier));

        for(ElementWithSlots elem: group.getMembers()){
            if(elem.getClass().equals(Group.class)){
                Group subGroup = (Group) elem;
                groupIdentifier.getSub().add(convert(subGroup,export));
            }
            if(elem.getClass().equals(DataElement.class)){
                DataElement de = (DataElement) elem;
                groupIdentifier.getSub().add(convert(de,export));
            }
        }

        return xmlGroup.getUuid();

    }

    private String convert(DataElement dataElement,Export export){
        de.samply.mdr.xsd.DataElement xmlElement = new de.samply.mdr.xsd.DataElement();
        setNewUUID(xmlElement);
        xmlElement.setValueDomain(extractValueDomain(dataElement, extractScopedIdentifer(dataElement,xmlElement.getUuid()), export));
        export.getElement().add(objectFactory.createDataElement(xmlElement));


        return xmlElement.getUuid();


    }

    private String extractValueDomain(DataElement de, ScopedIdentifier deIdentifier, Export export){
        IValidationType val = de.getValidation();
        if (val.getType().equals(new EnumeratedType().getType())){
            return extractEnumeratedDomain((EnumeratedType) val, deIdentifier, export);
        }

        DescribedValueDomain xmlVal = new DescribedValueDomain();
        setNewUUID(xmlVal);

        if(val.getType().equals(new BooleanType().getType())){
            //TODO CHECK
            xmlVal.setDatatype("BOOLEAN");
            xmlVal.setFormat("");
            xmlVal.setMaxCharacters(BigInteger.ZERO);
            xmlVal.setDescription("");
            xmlVal.setValidationType("NONE");
            xmlVal.setValidationData("");
        }
        if(val.getType().equals(new CalendarType().getType())){
            CalendarType calVal = (CalendarType) val;
            calVal.getFormat().fillAttributes(xmlVal);

        }
        if(val.getType().equals(new FloatType().getType())){
            FloatType fVal = (FloatType) val;
            //TOD Nullcheck
            String range = fVal.getRangeFrom()+"<=x<="+fVal.getRangeTo();
            xmlVal.setDatatype("FLOAT");
            xmlVal.setFormat(range);
            xmlVal.setMaxCharacters(BigInteger.ZERO);
            xmlVal.setDescription(range);
            xmlVal.setValidationType("FLOATRANGE");
            xmlVal.setValidationData(range);
            if(fVal.getUnitOfMeasure() == null){
                xmlVal.setUnitOfMeasure("");
            }else {
                xmlVal.setUnitOfMeasure(fVal.getUnitOfMeasure());
            }
        }
        if(val.getType().equals(new IntegerType().getType())){
            IntegerType iVal = (IntegerType) val;
            //TOD Nullcheck
            String range = iVal.getRangeFrom()+"<=x<="+iVal.getRangeTo();
            xmlVal.setDatatype("INTEGER");
            xmlVal.setFormat(range);
            xmlVal.setMaxCharacters(BigInteger.ZERO);
            xmlVal.setDescription(range);
            xmlVal.setValidationType("INTEGERRANGE");
            xmlVal.setValidationData(range);
            if(iVal.getUnitOfMeasure() == null){
                xmlVal.setUnitOfMeasure("");
            }else {
                xmlVal.setUnitOfMeasure(iVal.getUnitOfMeasure());
            }

        }
        if(val.getType().equals(new StringType().getType())){
            StringType stringVal = (StringType) val;
            xmlVal.setDatatype("STRING");
            xmlVal.setFormat("");
            xmlVal.setMaxCharacters(BigInteger.valueOf(stringVal.getMaxLength()));
            xmlVal.setDescription("");
            if(stringVal.getRegex().isEmpty()){
                xmlVal.setValidationType("NONE");
                xmlVal.setValidationData("");
            }else{
                xmlVal.setValidationType("REGEX");
                xmlVal.setValidationData(stringVal.getRegex());
            }
        }

        export.getElement().add(objectFactory.createDescribedValueDomain(xmlVal));
        return xmlVal.getUuid();
    }

    private String extractEnumeratedDomain(EnumeratedType val, ScopedIdentifier deIdentifier, Export export){
        EnumeratedValueDomain xmlVal = new EnumeratedValueDomain();
        setNewUUID(xmlVal);
        xmlVal.setFormat("enumerated");
        xmlVal.setDatatype("enumerated");
        xmlVal.setMaxCharacters(BigInteger.ZERO);

        for(PermissibleValue value:val.getValues()){
            de.samply.mdr.xsd.PermissibleValue xmlValue = new de.samply.mdr.xsd.PermissibleValue();
            setNewUUID(xmlValue);
            xmlValue.setValueDomain(xmlVal.getUuid());
            xmlValue.setValue(value.getPermittedValue());
            export.getElement().add(objectFactory.createPermissibleValue(xmlValue));

            //Labels for permissssibleValues need to go into the DataElement scopedIdentifer
            Definitions valueDefinitions = convertDefinitions(value, xmlValue.getUuid());
            deIdentifier.getDefinitions().getDefinition().addAll(valueDefinitions.getDefinition());
        }

        export.getElement().add(objectFactory.createEnumeratedValueDomain(xmlVal));
        return xmlVal.getUuid();

    }



    private ScopedIdentifier extractScopedIdentifer(ElementWithSlots elem, String uuid ){
        ScopedIdentifier identifier = new ScopedIdentifier();
        setNewUUID(identifier);
        identifier.setDefinitions(convertDefinitions(elem, uuid));
        identifier.setSlots(convertSlots(elem));
        identifier.setNamespace(this.namespaceUUID);
        identifier.setIdentifier(getNextIdentifier());
        identifier.setVersion("1");
        identifier.setElement(uuid);
        return identifier;
    }

    private void setNewUUID(de.samply.mdr.xsd.Element element){
        element.setUuid(UUID.randomUUID().toString());
    }

    private Definitions convertDefinitions(Element element, String uuid){
        Definitions definitions = new Definitions();
        for(Map.Entry<String,Label> e:element.getLabels().entrySet()){
            Definition def = new Definition();
            def.setLang(e.getKey());
            def.setDesignation(e.getValue().getDesignation());
            def.setDefinition(e.getValue().getDefinition());
            def.setUuid(uuid);
            definitions.getDefinition().add(def);
        }
        return definitions;
    }

    private Slots convertSlots(ElementWithSlots element){
        Slots slots = new Slots();
        for(Map.Entry<String,String> e:element.getSlots().entrySet()){
            Slot slot = new Slot();
            slot.setKey(e.getKey());
            slot.setValue(e.getValue());
            slots.getSlot().add(slot);
        }
        return slots;
    }

    private String getNextIdentifier(){
        this.runningId = runningId+1;
        return Integer.toString(runningId);
    }

}
