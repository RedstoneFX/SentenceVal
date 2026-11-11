package com.github.CompPrehension;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Preprocessor {

    private static final Path CORE_LANG_FILENAME = Paths.get("coreLanguage.loqi");
    private static final Path TEMP_LANG_MODEL_FILENAME = Paths.get("temp/LangModel.loqi");

    private static String readFromFile(Path filename) throws FileNotFoundException {
        File dataFile = filename.toFile();
        Scanner input = new Scanner (dataFile);
        try {
            String text = input.useDelimiter("\\Z").next();
            input.close();
            return text;
        } catch(NoSuchElementException e) {
            input.close();
            return "";
        }
    }

    private static void combineTextFiles(Path[] filenames, Path outFilename) throws IOException {
        try {
            if(outFilename.getParent() != null) Files.createDirectories(outFilename.getParent());
        }catch (FileAlreadyExistsException ignored) {}
        try {
            Files.createFile(outFilename);
        }catch (FileAlreadyExistsException ignored) {};
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFilename.toFile(), false));
        for (Path filename : filenames) {
            writer.append(readFromFile(filename));
            writer.append("\n");
        }
        writer.close();
    }

    public static Path computeExercise(Path langFilename, Path sentenceFilename) throws IOException {
        combineTextFiles(new Path[]{CORE_LANG_FILENAME, langFilename, sentenceFilename}, TEMP_LANG_MODEL_FILENAME);
        return TEMP_LANG_MODEL_FILENAME;
    }
}
