package de.samply.fhir2mdr.model;

public class IntegerType extends NumericalType {

    private final static String type = "integer";

    private Integer rangeFrom;

    private Integer rangeTo;

    public Integer getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(Integer rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public Integer getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(Integer rangeTo) {
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
