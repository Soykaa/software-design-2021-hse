package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class WCCommandTests {
    @Test
    public void testNotEmptyFile() {
        PipedInputStream commandOutput = new PipedInputStream();
        OutputStream outputStream = new OutputStream(commandOutput);
        Command command = new WCCommand(List.of("src/resources/not_empty_file.txt"),
            new InputStream(new PipedOutputStream()), outputStream);
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "7  40 217";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyFile() {
        PipedInputStream commandOutput = new PipedInputStream();
        OutputStream outputStream = new OutputStream(commandOutput);
        Command command = new WCCommand(List.of("src/resources/empty_file.txt"),
            new InputStream(new PipedOutputStream()), outputStream);
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "0  0 0";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMoreArguments() {
        PipedInputStream commandOutput = new PipedInputStream();
        OutputStream outputStream = new OutputStream(commandOutput);
        Command command = new WCCommand(List.of("src/resources/not_empty_file.txt", "123"),
            new InputStream(new PipedOutputStream()), outputStream);
        Assertions.assertEquals(1, command.execute());
        String expectedError = "Command wc works with one file or with standard input";
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
    public void testNotExistingFile() {
        PipedInputStream commandOutput = new PipedInputStream();
        OutputStream outputStream = new OutputStream(commandOutput);
        Command command = new WCCommand(List.of("src/resources/not_existing_file.txt"),
            new InputStream(new PipedOutputStream()), outputStream);
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
    public void testFromInputStream() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        OutputStream outputStream = new OutputStream(commandOutput);
        Command command = new WCCommand(Collections.emptyList(),
            new InputStream(commandInput), outputStream);

        try {
            byte[] input = "This is test string input".getBytes();
            commandInput.write(input);
            commandInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "1  5 25";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFromEmptyInputStream() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        OutputStream outputStream = new OutputStream(commandOutput);
        Command command = new WCCommand(Collections.emptyList(),
            new InputStream(commandInput), outputStream);

        try {
            commandInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "1  0 0";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
