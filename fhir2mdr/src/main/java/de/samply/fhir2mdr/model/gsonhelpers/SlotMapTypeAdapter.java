package de.samply.fhir2mdr.model.gsonhelpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class SlotMapTypeAdapter extends TypeAdapter<Map<String,String>> {

    private static final String NAME = "slot_name";
    private static final String VALUE = "slot_value";

    @Override
    public void write(JsonWriter out, Map<String, String> value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        for(Map.Entry<String,String> e:value.entrySet()){
            out.beginObject();
            out.name(NAME);
            out.value(e.getKey());
            out.name(VALUE);
            out.value(e.getValue());
            out.endObject();
        }
        out.endArray();
    }

    @Override
    public Map<String, String> read(JsonReader in) throws IOException {
        Map<String,String> slots = new HashMap<>();
        in.beginArray();
        while(in.peek() == JsonToken.BEGIN_OBJECT){
            in.beginObject();
            String name = null;
            String value = null;
            while(in.peek() == JsonToken.NAME){

                String fieldName = in.nextName();

                if(NAME.equals(fieldName)){
                    if(in.peek() == JsonToken.STRING){
                        name = in.nextString();
                    }
                }

                if(VALUE.equals(fieldName)){
                    if(in.peek() == JsonToken.STRING){
                        value = in.nextString();
                    }
                }

            }
            in.endObject();
            slots.put(name,value);
        }
        in.endArray();

        return slots;
    }



}
