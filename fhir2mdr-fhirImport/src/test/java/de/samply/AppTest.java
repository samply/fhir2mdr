package de.samply;

import de.samply.fhir2mdr.xml.ModelToXSDObjects;
import de.samply.schema.mdr.common.Export;
import org.junit.Test;
import de.samply.fhir2mdr.model.*;

import com.google.gson.Gson;

import javax.xml.bind.JAXB;

public class AppTest {
    @Test
    public void testApp() {
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
}
