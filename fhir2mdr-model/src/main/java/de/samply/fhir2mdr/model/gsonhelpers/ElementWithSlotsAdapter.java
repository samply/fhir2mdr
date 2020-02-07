package de.samply.fhir2mdr.model.gsonhelpers;

import com.google.gson.*;
import de.samply.fhir2mdr.model.DataElement;
import de.samply.fhir2mdr.model.ElementWithSlots;
import de.samply.fhir2mdr.model.Group;
import de.samply.fhir2mdr.model.IValidationType;

import java.lang.reflect.Type;

public class ElementWithSlotsAdapter implements JsonDeserializer<ElementWithSlots>, JsonSerializer<ElementWithSlots> {
    private static final String TYPE_FIELD = "type";

    @Override
    public ElementWithSlots deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(TYPE_FIELD);
        String type = prim.getAsString();

        if(new DataElement().getType().equals(type)){
            return context.deserialize(json,DataElement.class);
        }
        if(new Group().getType().equals(type)){
            return context.deserialize(json,Group.class);
        }
        return null;
    }

    @Override
    public JsonElement serialize(ElementWithSlots src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement val = context.serialize(src);
        val.getAsJsonObject().addProperty(TYPE_FIELD,src.getType());
        return val;
    }
}
