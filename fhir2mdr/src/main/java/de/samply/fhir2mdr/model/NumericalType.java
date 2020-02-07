package de.samply.fhir2mdr.model;

public abstract class NumericalType implements IValidationType {

    private String unitOfMeasure;

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public NumericalType() {
    }

    public NumericalType(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public abstract String getType();
}
