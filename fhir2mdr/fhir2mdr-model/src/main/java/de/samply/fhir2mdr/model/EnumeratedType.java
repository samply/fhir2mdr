package de.samply.fhir2mdr.model;

import java.util.ArrayList;
import java.util.List;

public class EnumeratedType implements IValidationType {

    private final static String type = "enumerated";

    private List<PermissibleValue> values;

    public EnumeratedType(){
        this.values = new ArrayList<>();
    }

    @Override
    public String getType() {
        return type;
    }

    public List<PermissibleValue> getValues() {
        return values;
    }

    public void setValues(List<PermissibleValue> values) {
        this.values = values;
    }


}
