package de.samply.fhir2mdr.model;

import java.util.ArrayList;
import java.util.List;

public class Namespace extends Element {

    public List<ElementWithSlots> getMembers() {
        return members;
    }

    public void setMembers(List<ElementWithSlots> members) {
        this.members = members;
    }

    private List<ElementWithSlots> members;

    public Namespace(){
        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
