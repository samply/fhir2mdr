package de.samply.fhir2mdr.model;

public class FloatType extends NumericalType{

    private final static String type = "float";

    private float rangeFrom;

    private float rangeTo;

    public float getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(float rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public float getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(float rangeTo) {
        this.rangeTo = rangeTo;
    }

    public FloatType() {
        super();
    }

    public FloatType(float rangeFrom, float rangeTo, String unitOfMeasure) {
        super(unitOfMeasure);
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }

    @Override
    public String getType() {
        return type;
    }
}
