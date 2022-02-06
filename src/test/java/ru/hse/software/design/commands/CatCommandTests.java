package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class CatCommandTests {
    @Test
    public void testNotEmptyFile() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new CatCommand(List.of("src/resources/not_empty_file.txt"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = Files.readString(Path.of("src/resources/not_empty_file.txt"), StandardCharsets.UTF_8) + System.lineSeparator();
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyFile() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new CatCommand(List.of("src/resources/empty_file.txt"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = Files.readString(Path.of("src/resources/empty_file.txt"), StandardCharsets.UTF_8);
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNotExistingFile() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new CatCommand(List.of("src/resources/not_existing_file.txt"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(1, command.execute());
        String expectedError = "file src/resources/not_existing_file.txt does not exist";
        Assertions.assertTrue(command.getErrorMessage().isPresent());
        Assertions.assertEquals(expectedError, command.getErrorMessage().get());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMoreArguments() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new CatCommand(List.of("src/resources/not_empty_file.txt", "123"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(1, command.execute());
        String expectedError = "Command cat works with one file or with standard input";
        Assertions.assertTrue(command.getErrorMessage().isPresent());
        Assertions.assertEquals(expectedError, command.getErrorMessage().get());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFromInputStream() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new CatCommand(Collections.emptyList(),
            new InputStream(commandInput), new OutputStream(commandOutput), new OutputStream(errorOutput));

        try {
            byte[] input = ("This is test string input" + System.lineSeparator() + "I love testing." +
                System.lineSeparator() + "I love java").getBytes();
            commandInput.write(input);
            commandInput.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput =  ("This is test string input" + System.lineSeparator() + "I love testing." +
                System.lineSeparator() + "I love java");
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFromEmptyInputStream() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new CatCommand(Collections.emptyList(),
            new InputStream(commandInput), new OutputStream(commandOutput), new OutputStream(errorOutput));

        try {
            commandInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
