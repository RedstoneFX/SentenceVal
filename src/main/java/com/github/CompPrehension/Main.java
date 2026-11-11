package com.github.CompPrehension;

import its.model.definition.*;
import its.model.definition.loqi.DomainLoqiBuilder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Path ACTIVE_LANGUAGE_FILENAME = Paths.get("exampleEnglish.loqi");

    public static void main(String[] args) throws IOException {
        Path languageModelFilename = Preprocessor.computeLanguageDomain(ACTIVE_LANGUAGE_FILENAME);

        DomainModel languageModel = DomainLoqiBuilder.buildDomain(new FileReader(languageModelFilename.toFile()));

        ObjectContainer dependencyTypes = languageModel.getObjects();

        for (ObjectDef dependencyType : dependencyTypes) {
            System.out.println(dependencyType.getName());
        }
    }
}