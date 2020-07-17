package de.samply.fhir2mdr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.samply.fhir2mdr.model.Element;
import de.samply.fhir2mdr.model.Namespace;
import de.samply.fhir2mdr.xml.ModelToXSDObjects;
import de.samply.schema.mdr.common.Export;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;

import ca.uhn.fhir.context.FhirContext;

import javax.xml.bind.JAXB;

public final class App {

    private static FhirContext fhir = FhirContext.forR4();

    private static Function<Resource, Optional<String>> GET_URL_IF_STRUCTURE_OR_TERMINOLOGY = r -> {
        ResourceType type = r.getResourceType();

        switch (type) {
        case StructureDefinition: {
            StructureDefinition def = (StructureDefinition) r;
            return Optional.of(def.getUrl());
        }
        case ValueSet: {
            ValueSet vs = (ValueSet) r;
            return Optional.of(vs.getUrl());
        }
        case CodeSystem: {
            CodeSystem cs = (CodeSystem) r;
            return Optional.of(cs.getUrl());
        }

        default:
            return Optional.empty();
        }
    };

    private App() {
    }

    /**
     * @param args The arguments of the program.
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Directory containing FHIR resources")
            .addOption("l","language",true,"Fallback language code for text fields, e.g. \"de\" or \"en\" (default: \"en\"")
            .addOption("n","name",true," (optional) Name for the generated namespace")
            .addOption("o", "output", true, " (optional) Output file for MDR-XML");

        CommandLineParser parser = new DefaultParser();

        // parse the options passed as command line arguments
        CommandLine cmd = parser.parse(options, args);
        if (!cmd.hasOption("i")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("FHIR2MDR", options);
            return;
        }
        String name;
        if (!cmd.hasOption("n")) {
            System.out.println("No name specified, namespace will be called \"fhir\" ");
            name = "fhir";
        }else{
            name = cmd.getOptionValue("n");
        }
        String language;
        if (!cmd.hasOption("l")) {
            language = "en";
        }else{
            language = cmd.getOptionValue("l");
        }

        Path inputDir = null;
        try {
            inputDir = Paths.get(cmd.getOptionValue("i"));
        } catch (InvalidPathException | NullPointerException e) {
            System.out.println("Invalid Input directory " + e.getMessage());
            return;
        }
        String outputFile;
        if (!cmd.hasOption("o")) {
            System.out.println("No output file specified, writing to fhir.xml...");
            outputFile = "fhir.xml";
        }else{
            outputFile = cmd.getOptionValue("o");
        }

        List<Resource> fhirResources = null;
        try{
            fhirResources = readFhirResources(inputDir);
        }catch(IOException e){
            System.out.println("Error reading from Input directory " + e.getMessage());
            return;
        }


        Map<String, Resource> conformanceResourcesByUrl = fhirResources.stream()
                .filter(t -> GET_URL_IF_STRUCTURE_OR_TERMINOLOGY.apply(t).isPresent())
                .collect(Collectors.toMap(t -> GET_URL_IF_STRUCTURE_OR_TERMINOLOGY.apply(t).get(), Function.identity()));

        System.out.println(String.format("Parsed %d conformance resources",  conformanceResourcesByUrl.size()));


        List<StructureDefinition> profiles =  conformanceResourcesByUrl.values().stream()
                .filter(r -> r.getResourceType().equals(ResourceType.StructureDefinition))
                .map(r -> (StructureDefinition) r).filter(r -> r.getKind().equals(StructureDefinitionKind.RESOURCE))
                .collect(Collectors.toList());

        System.out.println(String.format("Found %d profiles", profiles.size()));

        FhirParser fhirParser = new FhirParser();
        Namespace namespace = fhirParser.convertFhirResources(profiles,conformanceResourcesByUrl,name,language);

        ModelToXSDObjects toXml = new ModelToXSDObjects();
        Export export = toXml.convert(namespace);
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        JAXB.marshal(export,writer);

    }

    private static List<Resource> readFhirResources(Path directory) throws IOException {
        DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory, "*.json");

        List<Resource> fhirResources = new ArrayList<>();
        for (Path filePath : dirStream) {
            try {
                String content = new String(Files.readAllBytes(filePath));
                fhirResources.add((Resource) fhir.newJsonParser().parseResource(content));
            } catch (Exception e) {
                System.out.println(String.format(
                        "No FHIR Resource could be loaded from File: %s , %s , continuing with next file...",
                        filePath.toString(), e.getMessage()));
                continue;
            }

        }
        return fhirResources;
    }
}
