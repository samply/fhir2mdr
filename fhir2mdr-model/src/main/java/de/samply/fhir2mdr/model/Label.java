package de.samply.fhir2mdr.model;


public class Label {

    private String langCode;

    private String designation;

    private String definition;

    public Label(){
    }
    public Label(String langCode,String designation, String definition){
        this.langCode = langCode;
        this.designation = designation;
        this.definition = definition;
    }

    public Label(String langCode){
        this.langCode = langCode;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
