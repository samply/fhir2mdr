package de.samply;

import de.samply.MDRtools.model.Element;
import de.samply.MDRtools.model.Namespace;
import de.samply.fhir2mdr.FhirParser;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.Test;

import java.util.Collections;

public class AppTest {

    @Test
    public void testReadFHIR(){

        StructureDefinition profile = new StructureDefinition();
        profile.setLanguage("de");
        profile.setTitle("Test");
        profile.setDescription("test des konverters");
        ElementDefinition testAttr = new ElementDefinition();
        testAttr.setMustSupport(true);
        testAttr.setLabel("test attr");
        testAttr.setDefinition("test attribut");

        profile.getSnapshot().addElement(testAttr);

        FhirParser parser = new FhirParser();
        Namespace test = parser.convertFhirResources(Collections.singletonList(profile),Collections.singletonMap("testProfile",profile),"test","no");

        System.out.println(Element.gson.toJson(test));
    }
}
