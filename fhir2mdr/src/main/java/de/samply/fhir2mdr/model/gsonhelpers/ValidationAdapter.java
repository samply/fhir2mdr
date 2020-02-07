package de.samply.fhir2mdr.model.gsonhelpers;

import com.google.gson.*;
import de.samply.fhir2mdr.model.*;

import java.lang.reflect.Type;

public class ValidationAdapter implements JsonDeserializer<IValidationType>,JsonSerializer<IValidationType> {
    private static String TYPE_FIELD = "datatype";

    @Override
    public IValidationType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(TYPE_FIELD);
        String datatype = prim.getAsString();

        //TODO use reflection magic (loop over all implementing classes)
        if(datatype.equals(new BooleanType().getType())){
            return context.deserialize(json, BooleanType.class);
        }
        if(datatype.equals(new CalendarType().getType())){
            return context.deserialize(json, CalendarType.class);
        }
        if(datatype.equals(new EnumeratedType().getType())){
            return context.deserialize(json, EnumeratedType.class);
        }
        if(datatype.equals(new FloatType().getType())){
            return context.deserialize(json, FloatType.class);
        }
        if(datatype.equals(new IntegerType().getType())){
            return context.deserialize(json, IntegerType.class);
        }
        if(datatype.equals(new StringType().getType())){
            return context.deserialize(json, StringType.class);
        }
        return null;
    }

    @Override
    public JsonElement serialize(IValidationType src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement val = context.serialize(src);
        val.getAsJsonObject().addProperty(TYPE_FIELD,src.getType());
        return val;
    }
}
