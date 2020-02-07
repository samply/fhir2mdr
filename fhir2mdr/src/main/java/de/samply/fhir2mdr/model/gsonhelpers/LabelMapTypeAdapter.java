package de.samply.fhir2mdr.model.gsonhelpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import de.samply.fhir2mdr.model.*;

public class LabelMapTypeAdapter extends TypeAdapter<Map<String,Label>> {

    private static final String LANG_CODE = "language";
    private static final String DESIGNATION = "designation";
    private static final String DEFINITION = "definition";

    @Override
    public void write(JsonWriter out, Map<String, Label> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
         }
       out.beginArray();
       for(Map.Entry<String,Label> e:value.entrySet()){
            Label l = e.getValue();
            out.beginObject();
            out.name(LANG_CODE);
            out.value(l.getLangCode());
            out.name(DESIGNATION);
            out.value(l.getDesignation());
            out.name(DEFINITION);
            out.value(l.getDefinition());
            out.endObject();
       }
       out.endArray();
    }

    @Override
    public Map<String, Label> read(JsonReader in) throws IOException {
        Map<String,Label> labels = new HashMap<>();
        in.beginArray();
        while(in.peek() == JsonToken.BEGIN_OBJECT){
            in.beginObject();
            Label l = new Label();
            String langCode = null;
            while(in.peek() == JsonToken.NAME){

                String fieldName = in.nextName();

                if(LANG_CODE.equals(fieldName)){
                    if(in.peek() == JsonToken.STRING){
                        langCode = in.nextString();
                        l.setLangCode(langCode);
                    }
                }

                if(DESIGNATION.equals(fieldName)){
                    if(in.peek() == JsonToken.STRING){
                        l.setDesignation(in.nextString());
                    }
                }

                if(DEFINITION.equals(fieldName)){
                    if(in.peek() == JsonToken.STRING){
                        l.setDefinition(in.nextString());
                    }
                }
            }
            in.endObject();
            labels.put(langCode, l);
        }
        in.endArray();

        return labels;
    }



}
