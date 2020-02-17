package de.samply.fhir2mdr.model;

public class FloatType extends NumericalType{

    private final static String type = "float";

    private Float rangeFrom;

    private Float rangeTo;

    public Float getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(Float rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public Float getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(Float rangeTo) {
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
