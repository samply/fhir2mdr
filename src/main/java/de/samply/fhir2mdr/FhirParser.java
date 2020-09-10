package de.samply.fhir2mdr;

import ca.uhn.fhir.context.FhirContext;
import de.samply.MDRtools.model.BooleanType;
import de.samply.MDRtools.model.DataElement;
import de.samply.MDRtools.model.*;
import de.samply.MDRtools.model.Element;
import de.samply.MDRtools.model.Group;
import de.samply.MDRtools.model.IntegerType;
import de.samply.MDRtools.model.StringType;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.validation.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FhirParser {

    private static final FhirContext fhirContext = FhirContext.forR4();

    private Map<String,Resource> conformanceResourcesByUrl;
    private String defaultLanguage;
    private List<ElementDefinition> processedElements;

    public Namespace convertFhirResources(List<StructureDefinition> profiles, Map<String, Resource> conformanceResourcesByUrl, String name, String defaultLanguage){
        this.conformanceResourcesByUrl = conformanceResourcesByUrl;
        this.defaultLanguage = defaultLanguage;

        // Create new Namespace
        Namespace namespace = new Namespace();
        namespace.setName(name);
        namespace.setLabel("en","FHIR","Namespace generated from FHIR resources");
        //For each profile
        for(StructureDefinition profile:profiles){
            namespace.getMembers().add(convertProfile(profile));
        }

        return namespace;
    }

    private Group convertProfile(StructureDefinition profile){
        this.processedElements = new ArrayList<ElementDefinition>();
        Group group = new Group();
        String language;
        if(profile.hasLanguage()){
            language = profile.getLanguage();
        }else {
            language = defaultLanguage;
        }
        String designation ;
        if(profile.getTitle() == null){
            designation = profile.getName();
        }else{
            designation = profile.getTitle();
        }
        if(profile.getDescription() != null){
            group.setLabel(language,designation,profile.getDescription());
        }else {
            group.setLabel(language,designation,"");
        }

        if(!profile.hasSnapshot()){
            DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport();
            SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(fhirContext, defaultSupport);
            ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);
            profile = chain.generateSnapshot(profile, profile.getUrl(), null, profile.getName());
        }
        elementsIntoGroup(profile.getSnapshot().getElement(),group,language);

        return group;
    }

    private void elementsIntoGroup(List<ElementDefinition> elements, Group group, String language) {
        // For each attribute
        for (ElementDefinition attr : elements) {
            //If not already processed
            if (processedElements.contains(attr)) {
                continue;
            }
            // If not MS, skip
            if (!(attr.hasMustSupport() && attr.getMustSupport())) {
                processedElements.add(attr);
                continue;
            }
            if (!attr.getType().isEmpty()) {
                if (attr.getTypeFirstRep().getCode().equals("Extension")) {
                    processedElements.add(attr);
                    group.getMembers().add(getAndParseExtensionStructure(attr,language));
                    continue;
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
    }

    private ElementWithSlots getAndParseExtensionStructure(ElementDefinition attr,String language){
        //TODO return from attr may have version appended with |
        StructureDefinition extension = (StructureDefinition) this.conformanceResourcesByUrl.get(attr.getTypeFirstRep().getProfile().get(0).asStringValue());

        if(!extension.hasSnapshot()){
            DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport();
            SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(fhirContext, defaultSupport);
            ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);
            extension = chain.generateSnapshot(extension, extension.getUrl(), null, extension.getName());
        }
        if(extension.getLanguage() != null){
            language = extension.getLanguage();
        }
        Label extensionLabel = new Label(language,extension.getName(),extension.getDescription());

        return parseExtensionElements(extension.getSnapshot().getElement(),extensionLabel,"Extension",language);

    }

    private ElementWithSlots parseExtensionElements(List<ElementDefinition> extensionElements, Label extensionLabel, String rootId, String language){
        List<ElementWithSlots> elements = new ArrayList<>();
        ElementDefinition valueElement = getElementById(rootId+".value[x]",extensionElements);
        ElementDefinition rootElement = getElementById(rootId,extensionElements);
        if(valueElement.getMax().equals("0")){
            //Complex Extension
            List<String> slices = getAllSliceNames(rootId+".extension",extensionElements);
            for(String sliceName:slices){
                String sliceId = rootId+".extension:"+sliceName;
                ElementDefinition complexRootElement = getElementById(sliceId,extensionElements);
                Label rootLabel = new Label(language, complexRootElement.getShort(), complexRootElement.getDefinition());
                elements.add(parseExtensionElements(getAllChildrenSliceSensitive(sliceId,extensionElements),rootLabel,sliceId,language));
            }
        }else{
            //If simple Extension (max value not 0), only parse valueElement
            elements.addAll(parseDataElement(valueElement,language));
        }

        if(elements.size() == 1){
            elements.get(0).setLabel(language, extensionLabel);
            return elements.get(0);
        }
        //Build Group
        Group extensionGroup = new Group();
        extensionGroup.setMembers(elements);
        extensionGroup.setLabel(language,rootElement.getShort(),rootElement.getDefinition());
        return extensionGroup;
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
        processedElements.add(attr);
        return results;
    }

    private void extractLabel(Element element, ElementDefinition attr, String language){
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
    }


    private List<IValidationType> parseValidation(ElementDefinition attr) {
        //if (not example) binding, try to parse valuelist, if not possible continue and return string type
        //TODO Maybe also create StringType for extensible/preferred with String Values?
        if (attr.hasBinding() && !attr.getBinding().getStrength().equals(Enumerations.BindingStrength.EXAMPLE)) {
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
        //TODO Custom parsing for more datatypes
        switch (code) {
            default:
                return new StringType();
            case "boolean":
                return new BooleanType();
            case "Count" :
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
        ValueSet values = (ValueSet) this.conformanceResourcesByUrl.get(attr.getBinding().getValueSet());
        if(values == null || values.getExpansion().isEmpty()){
            return null;
        }
        String language;
        if(values.hasLanguage()){
            language = values.getLanguage();
        }else{
            language = defaultLanguage;
        }
        EnumeratedType enumVal = new EnumeratedType();
        for(ValueSet.ValueSetExpansionContainsComponent value : values.getExpansion().getContains()){
            if(!value.getAbstract()){
                String fullName = value.getSystem()+"#"+value.getCode();
                PermissibleValue val = new PermissibleValue(fullName);
                String designation;
                if(value.hasDisplay()){
                    designation = value.getDisplay();
                }else {
                    designation = value.getCode();
                }
                val.getLabels().put(language,new Label(language,designation,fullName));
                enumVal.getValues().add(val);
            }
        }

        return enumVal;
    }

    private List<ElementDefinition> getAllChildren(String path, List<ElementDefinition> searchset){
        return searchset.stream()
            .filter(attr -> attr.getPath().startsWith(path))
            //Filter out Root Element
            .filter(attr -> !(attr.getPath().equals(path)))
            .collect(Collectors.toList());
    }

    private List<ElementDefinition> getAllChildrenSliceSensitive(String id, List<ElementDefinition> searchset){
        return searchset.stream()
            .filter(attr -> attr.getId().startsWith(id))
            //Filter out Root Element
            .filter(attr -> !(attr.getId().equals(id)))
            .collect(Collectors.toList());
    }

    private ElementDefinition getElementById(String id, List<ElementDefinition> searchset){

        for(ElementDefinition elem:searchset){
            if(elem.getId().equals(id)){
                return elem;
            }
        }
        return null;
    }

    private List<String> getAllSliceNames(String rootId, List<ElementDefinition> searchset){

        return searchset.stream()
            .filter(attr -> attr.getId().startsWith(rootId))
            .map(attr -> attr.getId().substring(rootId.length()+1).split("\\.")[0])
            .distinct().collect(Collectors.toList());

    }

}
