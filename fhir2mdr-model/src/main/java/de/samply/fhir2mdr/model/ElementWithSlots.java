package de.samply.fhir2mdr.model;

import java.util.HashMap;
import java.util.Map;

public abstract class ElementWithSlots extends Element {

    private Map<String,String> slots;

    public ElementWithSlots(){
        super();
        this.slots = new HashMap<>();
    }

    public abstract String getType();

    public Map<String, String> getSlots() {
        return slots;
    }

    public void setSlots(Map<String, String> slots) {
        this.slots = slots;
    }

}
