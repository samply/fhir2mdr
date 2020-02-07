package de.samply.fhir2mdr.model;

import de.samply.mdr.xsd.DescribedValueDomain;

import java.math.BigInteger;

public enum DateTimeFormatEnum {
    MONTH_YEAR, DAY_MONTH_YEAR, DATE_HOURS_MINUTES, DATE_HOURS_MINUTES_SECONDS;

    public DescribedValueDomain fillAttributes(DescribedValueDomain toBeFilled){

        //TODO Test
        switch(this){
            case MONTH_YEAR:
                toBeFilled.setFormat("YYYY-MM");
                toBeFilled.setDatatype("DATE");
                toBeFilled.setMaxCharacters(BigInteger.valueOf(7));
                toBeFilled.setDescription("YYYY-MM");
                toBeFilled.setValidationType("DATE");
                toBeFilled.setValidationData("ISO_8601");
                return toBeFilled;
            case DAY_MONTH_YEAR:
                toBeFilled.setFormat("YYYY-MM-DD");
                toBeFilled.setDatatype("DATE");
                toBeFilled.setMaxCharacters(BigInteger.valueOf(10));
                toBeFilled.setDescription("YYYY-MM-DD");
                toBeFilled.setValidationType("DATE");
                toBeFilled.setValidationData("ISO_8601");
                return toBeFilled;
            case DATE_HOURS_MINUTES:
                toBeFilled.setFormat("YYYY-MM-DD hh:mm");
                toBeFilled.setDatatype("DATETIME");
                toBeFilled.setMaxCharacters(BigInteger.valueOf(16));
                toBeFilled.setDescription("YYYY-MM-DD hh:mm");
                toBeFilled.setValidationType("DATETIME");
                toBeFilled.setValidationData("ISO_8601; HOURS_24");
                return toBeFilled;
            case DATE_HOURS_MINUTES_SECONDS:
                toBeFilled.setFormat("YYYY-MM-DD hh:mm:ss");
                toBeFilled.setDatatype("DATETIME");
                toBeFilled.setMaxCharacters(BigInteger.valueOf(19));
                toBeFilled.setDescription("YYYY-MM-DD hh:mm:ss");
                toBeFilled.setValidationType("DATETIME");
                toBeFilled.setValidationData("ISO_8601; HOURS_24_WITH_SECONDS");
                return toBeFilled;
            default: return toBeFilled;
        }


    }
}
