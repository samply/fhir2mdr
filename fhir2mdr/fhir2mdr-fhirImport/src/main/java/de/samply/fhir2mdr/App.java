package de.samply.fhir2mdr;

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
    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("i", "input", true, "Directory containing FHIR resources").addOption("o", "output", true,
                "Output directory for MDR-XML");

        CommandLineParser parser = new DefaultParser();

        // parse the options passed as command line arguments
        CommandLine cmd = parser.parse(options, args);
        if (!cmd.hasOption("i")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("FHIR2MDR", options);
        }
        if (!cmd.hasOption("o")) {
            System.out.println("No output directory specified, writing to input directory...");
        }
        Path inputDir = null;
        try {
            inputDir = Paths.get(cmd.getOptionValue("i"));
        } catch (InvalidPathException | NullPointerException e) {
            System.out.println("Invalid Input directory " + e.getMessage());
            return;
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

        System.out.println(String.format("Parsed %i conformance resources",  conformanceResourcesByUrl.size()));


        List<StructureDefinition> profiles =  conformanceResourcesByUrl.values().stream()
                .filter(r -> r.getResourceType().equals(ResourceType.StructureDefinition))
                .map(r -> (StructureDefinition) r).filter(r -> r.getKind().equals(StructureDefinitionKind.RESOURCE))
                .filter(r -> !r.getSnapshot().isEmpty()).collect(Collectors.toList());

        System.out.println(String.format("Found %i profiles with snapshots", profiles.size()));

        // Create new Namespace
        // For each attribute
        // If MS
        // Resolve terminologies when possible
        // Map fields
        // Add to Samply List as DE

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
