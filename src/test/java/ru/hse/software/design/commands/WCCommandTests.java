package ru.hse.software.design.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.Environment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class WCCommandTests {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUp() {
        System.setErr(new PrintStream(errContent));
        Environment.clear();
    }

    @AfterEach
    public void restoreStreams() {
        System.setErr(originalErr);
    }

    @Test
    public void testNotEmptyFile() throws IOException {
        Command command = new WCCommand(List.of("src/resources/not_empty_file.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "7  40 " + Files.size(Paths.get("src/resources/not_empty_file.txt"));
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEmptyFile() {
        Command command = new WCCommand(List.of("src/resources/empty_file.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "0  0 0";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMoreArguments() {
        Command command = new WCCommand(List.of("src/resources/not_empty_file.txt", "123"));
        Assertions.assertEquals(1, command.execute(""));
        String expectedError = "Command wc works with one file or with standard input" + System.lineSeparator();
        Assertions.assertEquals(expectedError, errContent.toString());
        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testNotExistingFile() {
        Command command = new WCCommand(List.of("src/resources/not_existing_file.txt"));
        Assertions.assertEquals(1, command.execute(""));
        String expectedError = "file src/resources/not_existing_file.txt does not exist" + System.lineSeparator();
        Assertions.assertEquals(expectedError, errContent.toString());
        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFromInputStream() {
        Command command = new WCCommand(Collections.emptyList());
        Assertions.assertEquals(0, command.execute("This is test string input"));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "1  5 25";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFromEmptyInputStream() {
        Command command = new WCCommand(Collections.emptyList());
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "1  0 0";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }
}