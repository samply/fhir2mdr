package de.samply.fhir2mdr.model;

import javax.naming.Name;
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

    public void removeEmptyGroups(){
        List<ElementWithSlots> cleanedMembers = new ArrayList<>();
        cleanedMembers.addAll(this.members);
        for(ElementWithSlots elem : this.members){
            if(elem.getType().equals(new Group().getType())){
                Group subgroup = (Group) elem;
                subgroup.removeEmptyGroups();
                if(subgroup.getMembers().isEmpty()){
                    cleanedMembers.remove(subgroup);
                }
            }

        }
        this.members = cleanedMembers;
    }
}
