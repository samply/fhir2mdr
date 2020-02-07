package de.samply.fhir2mdr.model;

public class IntegerType extends NumericalType {

    private final static String type = "integer";

    private int rangeFrom;

    private int rangeTo;

    public int getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(int rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public int getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(int rangeTo) {
        this.rangeTo = rangeTo;
    }

    public IntegerType() {
        super();
    }

    public IntegerType(int rangeFrom, int rangeTo, String unitOfMeasure){
        super(unitOfMeasure);
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }

    @Override
    public String getType() {
        return type;
    }
}
