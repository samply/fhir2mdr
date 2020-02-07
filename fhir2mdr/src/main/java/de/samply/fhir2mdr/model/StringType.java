package de.samply.fhir2mdr.model;

import com.google.gson.annotations.SerializedName;

public class StringType implements IValidationType {

  private final static String type = "STRING";

  private String regex;

    @SerializedName("maximum_character_quantity")
    private int maxLength;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public StringType() {
    }

    public StringType(String regex, int maxLength) {
        this.regex = regex;
        this.maxLength = maxLength;
    }

    @Override
    public String getType() {
        return type;
    }
}
