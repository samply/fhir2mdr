package de.samply.fhir2mdr;

import de.samply.fhir2mdr.model.DataElement;
import de.samply.fhir2mdr.model.*;
import de.samply.schema.mdr.common.Export;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StructureDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FhirParser {

    private Map<String,Resource> conformanceResourcesByUrl;
    private List<ElementDefinition> processedElements;

    public Export convertFhirResources(List<StructureDefinition> profiles, Map<String, Resource> conformanceResourcesByUrl, String name, String language){
        this.conformanceResourcesByUrl = conformanceResourcesByUrl;

        // Create new Namespace
        Namespace namespace = new Namespace();
        namespace.setName(name);
        //For each profile
        for(StructureDefinition profile:profiles){
            namespace.getMembers().add(convertProfile(profile,language));
        }

        return null;
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

        // For each attribute
        for(ElementDefinition attr:profile.getSnapshot().getElement()){
            // If MS
            if(!(attr.hasMustSupport() && attr.getMustSupport() == true)){
                processedElements.add(attr);
                continue;
            }
            if(attr.getTypeFirstRep().getCode().equals("Extension")){
                //TODO
            }
            if(attr.getTypeFirstRep().getCode().equals("BackboneElement")){
                //TODO Subgroup?
            }
            DataElement element = new DataElement();
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
            //Validation
            element.setValidation(getValidation(attr));

            // Add to Samply List as DE
        }

        return null;
    }

    private IValidationType getValidation(ElementDefinition attr){
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
