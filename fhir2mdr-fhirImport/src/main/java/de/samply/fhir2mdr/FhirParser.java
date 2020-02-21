package de.samply.fhir2mdr;

import ca.uhn.fhir.context.FhirContext;
import de.samply.fhir2mdr.model.DataElement;
import de.samply.fhir2mdr.model.*;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.validation.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FhirParser {

    private static final FhirContext fhirContext = FhirContext.forR4();

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

        if(!profile.hasSnapshot()){
            DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport();
            SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(fhirContext, defaultSupport);
            ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);
            StructureDefinition snapshot = chain.generateSnapshot(profile, profile.getUrl(), null, profile.getName());
            profile = snapshot;
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
            group.getMembers().addAll(parseDataElement(attr, language));
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

    /**
     * Creates one DataElement for each allowed data type
     * @param attr
     * @param language
     * @return
     */
    private List<DataElement> parseDataElement(ElementDefinition attr, String language){
        List<DataElement> results = new ArrayList<>();
        for(IValidationType val:parseValidation(attr)){
            DataElement element = new DataElement();
            extractLabel(element,attr, language);
            element.setValidation(val);
            results.add(element);
        }
        return results;
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


    private List<IValidationType> parseValidation(ElementDefinition attr) {
        //if binding, try to parse valuelist, if not possible continue and return string type
        if (attr.hasBinding()) {
            EnumeratedType valueList = resolveBinding(attr);
            if (valueList != null) {
                return Collections.singletonList(valueList);
            } else {
                return Collections.singletonList(new StringType());
            }
        }

        return attr.getType().stream().map( t -> parseSingleTypeValidation(t,attr))
            .collect(Collectors.toList());

    }

    private IValidationType parseSingleTypeValidation(ElementDefinition.TypeRefComponent type,ElementDefinition attr){
        String code = type.getCode();
        //TODO
        switch (code) {
            default:
                return new StringType();
            case "boolean":
                return new BooleanType();
            case "integer":
                IntegerType iVal = new IntegerType();
               if(attr.hasMinValueIntegerType()){
                   iVal.setRangeFrom(attr.getMinValueIntegerType().getValue());
               }
               if(attr.hasMaxValueIntegerType()){
                   iVal.setRangeTo(attr.getMaxValueIntegerType().getValue());
               }
                return iVal;
            case "unsignedInt":
                IntegerType uiVal = new IntegerType();
                if(attr.hasMinValueUnsignedIntType()){
                    uiVal.setRangeFrom(attr.getMinValueUnsignedIntType().getValue());
                }else{
                    uiVal.setRangeFrom(0);
                }
                if(attr.hasMaxValueUnsignedIntType()){
                    uiVal.setRangeTo(attr.getMaxValueUnsignedIntType().getValue());
                }
                return uiVal;
            case "positiveInt":
                IntegerType piVal = new IntegerType();
                if(attr.hasMinValuePositiveIntType()){
                    piVal.setRangeFrom(attr.getMinValuePositiveIntType().getValue());
                }else{
                    piVal.setRangeFrom(1);
                }
                if(attr.hasMaxValuePositiveIntType()){
                    piVal.setRangeTo(attr.getMaxValuePositiveIntType().getValue());
                }
                return piVal;

        }

    }

    private EnumeratedType resolveBinding(ElementDefinition attr){
        //TODO
        return null;
    }

}
