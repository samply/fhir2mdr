package de.samply.fhir2mdr.model;

public class CalendarType implements IValidationType {

    private final static String type = "DATETIME";

    private DateTimeFormatEnum format;

    public DateTimeFormatEnum getFormat() {
        return format;
    }

    public void setFormat(DateTimeFormatEnum format) {
        this.format = format;
    }

    public CalendarType() {
    }

    public CalendarType(DateTimeFormatEnum format) {
        this.format = format;
    }

    @Override
    public String getType() {
        return type;
    }
}
