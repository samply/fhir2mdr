package de.samply.fhir2mdr.model;

import java.util.HashMap;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.samply.fhir2mdr.model.gsonhelpers.*;


public abstract class Element {


    public static Gson gson;
    static
    {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(new TypeToken<Map<String,Label>>() {}.getType(), new LabelMapTypeAdapter());
       // builder.registerTypeAdapter(new TypeToken<Map<String,String>>() {}.getType(), new SlotMapTypeAdapter());
        builder.registerTypeAdapter(IValidationType.class , new ValidationAdapter());
        builder.registerTypeAdapter(ElementWithSlots.class, new ElementWithSlotsAdapter());
        gson = builder.create();
    }

    //@SerializedName("designations")
    private Map<String,Label> labels;

    public Element() {
        this.labels = new HashMap<>();
    }

    public boolean hasLabel(String langCode){
        return labels.containsKey(langCode);
    }

    public Label getLabel(String langCode){
        return labels.get(langCode);
    }

    public void setLabel(String langCode, Label label){
        if(label.getLangCode().equalsIgnoreCase(langCode)){
            this.labels.put(langCode, label);
        }else{
            throw new IllegalArgumentException("LanguageCode must match the code given in the Label");
        }

    }

    public void setLabel(String langCode, String designation, String definition){
        Label label = new Label(langCode);
        label.setDefinition(definition);
        label.setDesignation(designation);
        this.labels.put(langCode, label);
    }

    public Map<String,Label> getLabels(){
        return this.labels;
    }


}
