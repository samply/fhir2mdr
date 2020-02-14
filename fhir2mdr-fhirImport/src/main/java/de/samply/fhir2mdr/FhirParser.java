package de.samply.fhir2mdr;

import de.samply.fhir2mdr.model.DataElement;
import de.samply.fhir2mdr.model.*;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StructureDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FhirParser {

    private Map<String,Resource> conformanceResourcesByUrl;
    private List<ElementDefinition> processedElements;

    public Namespace convertFhirResources(List<StructureDefinition> profiles, Map<String, Resource> conformanceResourcesByUrl, String name, String language){
        this.conformanceResourcesByUrl = conformanceResourcesByUrl;

        // Create new Namespace
        Namespace namespace = new Namespace();
        namespace.setName(name);
        //For each profile
        for(StructureDefinition profile:profiles){
            namespace.getMembers().add(convertProfile(profile,language));
        }

        return namespace;
    }

    private Group convertProfile(StructureDefinition profile,String language){
        this.processedElements = new ArrayList<ElementDefinition>();
        Group group = new Group();
        if(profile.hasLanguage()){
            language = profile.getLanguage();
        }
        if(profile.getTitle() != null && profile.getDescription() != null){
            group.setLabel(language,profile.getTitle(),profile.getDescription());
        }

        elementsIntoGroup(profile.getSnapshot().getElement(),group,language);

        return group;
    }

    private Group elementsIntoGroup(List<ElementDefinition> elements, Group group, String language) {
        // For each attribute
        for (ElementDefinition attr : elements) {
            //If not already processed
            if (processedElements.contains(attr)) {
                continue;
            }
            // If MS
            if (!(attr.hasMustSupport() && attr.getMustSupport() == true)) {
                processedElements.add(attr);
                continue;
            }
            if (!attr.getType().isEmpty()) {
                if (attr.getTypeFirstRep().getCode().equals("Extension")) {
                    //TODO
                }
                if (attr.getTypeFirstRep().getCode().equals("BackboneElement")) {
                    List<ElementDefinition> children = getAllChildren(attr.getPath(), elements);
                    Group subGroup = new Group();
                    extractLabel(subGroup,attr,language);
                    processedElements.add(attr);
                    elementsIntoGroup(children,subGroup,language);
                    group.getMembers().add(subGroup);
                    continue;
                }
            }

            // Add to Samply List as DE
            group.getMembers().add(parseDataElement(attr, language));
        }
        return group;
    }

    private List<ElementDefinition> getAllChildren(String path, List<ElementDefinition> searchset){
        return searchset.stream()
            .filter(attr -> attr.getPath().startsWith(path))
            //Filter out Root Element
            .filter(attr -> !(attr.getPath().equals(path)))
            .collect(Collectors.toList());
    }

    private DataElement parseDataElement(ElementDefinition attr, String language){
        DataElement element = new DataElement();
        extractLabel(element,attr, language);
        //Validation
        element.setValidation(parseValidation(attr));
        return element;
    }

    private Element extractLabel(Element element, ElementDefinition attr, String language){
        //Definition
        String designation;
        if(attr.hasLabel()){
            designation = attr.getLabel();
        }else{
            designation = attr.getId();
        }
        String definition = "";
        if(attr.hasDefinition()){
            definition = attr.getDefinition();
        }else{
            if(attr.hasShort()){
                definition = attr.getShort();
            }
        }
        element.setLabel(language,designation,definition);
        return element;
    }


    private IValidationType parseValidation(ElementDefinition attr){
        //if binding, try to parse valuelist, if not possible continue and later parse as coding datatype
        if(attr.hasBinding()){
            EnumeratedType valueList = resolveBinding(attr);
            if(valueList != null){
                return valueList;
            }
        }

        List<IValidationType> vals = new ArrayList<>();
        for (ElementDefinition.TypeRefComponent type: attr.getType()) {
            if(type.getCode() == null){
                continue;
            }
            String code = type.getCode();
            switch (code){
                default: vals.add(new StringType());

            }

        }


        return new StringType();
        //TODO
       // return validation;

    }

    private EnumeratedType resolveBinding(ElementDefinition attr){
        //TODO
        return null;
    }

}
