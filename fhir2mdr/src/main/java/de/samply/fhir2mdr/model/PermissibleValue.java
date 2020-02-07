package de.samply.fhir2mdr.model;

public class PermissibleValue extends Element{

    private String permittedValue;

    public PermissibleValue(String permittedValue){
        this.permittedValue = permittedValue;
    }

    public String getPermittedValue() {
        return permittedValue;
    }

    public void setPermittedValue(String permittedValue) {
        this.permittedValue = permittedValue;
    }

}
