package ru.hse.software.design.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class CatCommandTests {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setErr(originalErr);
    }

    @Test
    public void testNotEmptyFile() {
        Command command = new CatCommand(List.of("src/resources/not_empty_file.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        try {
            String actualOutput = command.output.replaceAll("\n", System.lineSeparator());
            String expectedOutput = Files.readString(Path.of("src/resources/not_empty_file.txt"), StandardCharsets.UTF_8).replaceAll("\n", System.lineSeparator()) + System.lineSeparator();
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyFile() {
        Command command = new CatCommand(List.of("src/resources/empty_file.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        try {
            String actualOutput = command.output;
            String expectedOutput = Files.readString(Path.of("src/resources/empty_file.txt"), StandardCharsets.UTF_8);
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNotExistingFile() {
        Command command = new CatCommand(List.of("src/resources/not_existing_file.txt"));
        Assertions.assertEquals(1, command.execute(""));
        String expectedError = "file src/resources/not_existing_file.txt does not exist" + System.lineSeparator();
        Assertions.assertEquals(expectedError, errContent.toString());
        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMoreArguments() {
        Command command = new CatCommand(List.of("src/resources/not_empty_file.txt", "123"));
        Assertions.assertEquals(1, command.execute(""));
        String expectedError = "Command cat works with one file or with standard input" + System.lineSeparator();
        Assertions.assertEquals(expectedError, errContent.toString());
        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFromInputStream() {
        Command command = new CatCommand(Collections.emptyList());
        Assertions.assertEquals(0, command.execute("This is test string input.\nI love testing.\nI love java"));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "This is test string input.\nI love testing.\nI love java";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFromEmptyInputStream() {
        Command command = new CatCommand(Collections.emptyList());
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }
}