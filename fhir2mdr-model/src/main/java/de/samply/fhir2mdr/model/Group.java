package de.samply.fhir2mdr.model;

import java.util.ArrayList;
import java.util.List;

public class Group extends ElementWithSlots{

    private static final  String type = "group";

    private List<ElementWithSlots> members;

    public Group(){
        this.members = new ArrayList<>();
    }

    @Override
    public String getType() {
        return type;
    }

    public List<ElementWithSlots> getMembers() {
        return members;
    }

    public void setMembers(List<ElementWithSlots> members) {
        this.members = members;
    }
}
