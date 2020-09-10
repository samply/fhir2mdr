package de.samply.mdr2csv;

import com.google.gson.Gson;
import de.samply.MDRtools.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MDR2CSV {

    private String langCode;
    //                                                  0           1              2           3           4        5
    private static String[] header = new String[]{"GroupPath","Designation","Definition","DataType","Validation","Slots"};

    public List<String[]> transformMDR(Namespace ns, String langCode){
        this.langCode = langCode;
        List<String[]> table = new ArrayList<>();

        for(ElementWithSlots elem:ns.getMembers()){
            if(elem instanceof Group){
                table.addAll(transformGroup((Group) elem, ns.getName()));
            }
            if(elem instanceof DataElement){
                table.add(transformElement((DataElement) elem, ns.getName()));
            }
        }

        return table;
    }

    private List<String[]> transformGroup(Group gr, String groupPrefix){
        List<String[]> grSection = new ArrayList<>();
        groupPrefix = groupPrefix + "." + gr.getLabel(langCode).getDesignation();

        for(ElementWithSlots elem:gr.getMembers()){
            if(elem instanceof Group){
                grSection.addAll(transformGroup((Group) elem, groupPrefix));
            }
            if(elem instanceof DataElement){
                grSection.add(transformElement((DataElement) elem, groupPrefix));
            }
        }

        return grSection;
    }

    private String[] transformElement(DataElement de, String groupPrefix){
        String[] row = new String[6];

        row[0] = groupPrefix;
        row[1] = de.getLabel(langCode).getDesignation();
        row[2] = de.getLabel(langCode).getDefinition();
        row[3] = de.getValidation().getType();

        IValidationType val = de.getValidation();

        row[4] = "";
        if( val instanceof StringType){
            StringType stringVal = (StringType) val;
            row[4] = stringVal.getRegex() + " (maxChars: " + stringVal.getMaxLength() +")";
        }
        if( val instanceof FloatType){
            FloatType flVal = (FloatType) val;
            row[4] = flVal.getUnitOfMeasure();
            if(flVal.getRangeFrom() != null || flVal.getRangeTo() != null){
                row[4] = row[4] + " (";
                if(flVal.getRangeFrom() != null){
                    row[4] = row[4] +flVal.getRangeFrom()+"<=";
                }
                row[4] = row[4]+"x";
                if(flVal.getRangeTo() != null){
                    row[4] = row[4] +"<="+flVal.getRangeTo();
                }
                row[4] = row[4] +")";
            }
        }
        if( val instanceof IntegerType){
            IntegerType iVal = (IntegerType) val;
            row[4] = iVal.getUnitOfMeasure();
            if(iVal.getRangeFrom() != null || iVal.getRangeTo() != null){
                row[4] = row[4] + " (";
                if(iVal.getRangeFrom() != null){
                    row[4] = row[4] +iVal.getRangeFrom()+"<=";
                }
                row[4] = row[4]+"x";
                if(iVal.getRangeTo() != null){
                    row[4] = row[4] +"<="+iVal.getRangeTo();
                }
                row[4] = row[4] +")";
            }
        }
        if( val instanceof CalendarType){
            CalendarType calVal = (CalendarType) val;
            row[4] = calVal.getFormat().toString();
        }
        if( val instanceof EnumeratedType){
            EnumeratedType enumVal = (EnumeratedType) val;
            for(PermissibleValue pVal:enumVal.getValues()){
                row[4] = row[4] + pVal.getPermittedValue()+": "+pVal.getLabel(langCode).getDesignation()+"; ";
            }
        }


        Gson gson = new Gson();
        row[5] = gson.toJson(de.getSlots());

        return row;
    }

    public void writeCSV(List<String[]> table, BufferedWriter writer) throws IOException {
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(header));
        csvPrinter.printRecords(table);
        csvPrinter.flush();
        return;
    }

}
