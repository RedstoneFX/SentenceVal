package com.github.CompPrehension;

import its.model.definition.*;
import its.model.definition.loqi.DomainLoqiBuilder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main {

    private static final Path ACTIVE_LANGUAGE_FILENAME = Paths.get("exampleEnglish.loqi");
    private static final Path TARGET_SENTENCE_FILENAME = Paths.get("exampleSentence.loqi");

    public static void main(String[] args) throws IOException {
        Path languageModelFilename = Preprocessor.computeExercise(ACTIVE_LANGUAGE_FILENAME, TARGET_SENTENCE_FILENAME);

        DomainModel languageModel = DomainLoqiBuilder.buildDomain(new FileReader(languageModelFilename.toFile()));

        ObjectContainer dependencyTypes = languageModel.getObjects();

        for (ObjectDef object : dependencyTypes) {
            if(object.getClassName().equals("DependencyType")) {
                System.out.println(object.getName());
            } else if(object.getClassName().equals("DependencyTypeUnsafe")) {
                System.out.println(object.getName() + " [unsafe]");
            }
        }
    }
}