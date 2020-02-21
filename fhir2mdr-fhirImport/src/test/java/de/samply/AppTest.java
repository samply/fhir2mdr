package de.samply;

import de.samply.fhir2mdr.FhirParser;
import de.samply.fhir2mdr.xml.ModelToXSDObjects;
import de.samply.schema.mdr.common.Export;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.Test;
import de.samply.fhir2mdr.model.*;

import com.google.gson.Gson;

import javax.xml.bind.JAXB;
import java.util.Collections;

public class AppTest {
    @Test
    public void testXMLfromDe() {
      Group gr = new Group();
      DataElement de = new DataElement();
      de.setLabel("en", "test", "this is a test");
      de.setLabel("de", "Test", "Test-Label");
      StringType val = new StringType();
      val.setMaxLength(100);
      val.setRegex(".*");
      de.setValidation(val);
      de.getSlots().put("test","wert");
      gr.getMembers().add(de);
      gr.setLabel("de","Testgruppe","Gruppe zu Demozwecken");

      Group sub = new Group();
     gr.getMembers().add(sub);
      sub.setLabel("de","Subgroup","test for nesting");

      DataElement de2 = new DataElement();
      de2.setLabel("de","insubgroup","this de is in a subgroup");
      IntegerType intVal = new IntegerType();
      intVal.setRangeTo(100);
      intVal.setRangeFrom(0);
      intVal.setUnitOfMeasure("min");
      de2.setValidation(intVal);

     sub.getMembers().add(de2);

      Namespace namespace = new Namespace();
      namespace.getMembers().add(gr);
      namespace.setName("test");
      namespace.setLabel("de","test","test");

        ModelToXSDObjects conv = new ModelToXSDObjects();
       Export export = conv.convert(namespace);
        JAXB.marshal(export, System.out);

     // Gson gson = Element.gson;
      //System.out.println(gson.toJson(gr));
     // String test = "";
      // Group back = gson.fromJson(test,Group.class);
      //  System.out.println(gson.toJson(back));

    }

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
