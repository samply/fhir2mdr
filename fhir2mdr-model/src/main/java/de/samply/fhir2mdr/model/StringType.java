package de.samply.fhir2mdr.model;

import com.google.gson.annotations.SerializedName;

public class StringType implements IValidationType {

  private final static String type = "STRING";

  private String regex;

    @SerializedName("maximum_character_quantity")
    private Integer maxLength;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public StringType() {
        this.regex = "";
        this.maxLength = Integer.MAX_VALUE;
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
