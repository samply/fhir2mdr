package de.samply.fhir2mdr.model;

public class DataElement extends ElementWithSlots{

    private static final  String type = "dataelement";

    private IValidationType validation;

    public DataElement(){

    }

    @Override
    public String getType() {
        return type;
    }

    public IValidationType getValidation() {
        return validation;
    }

    public void setValidation(IValidationType validation) {
        this.validation = validation;
    }

}
