package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.Environment;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class EnvironmentCommandTest {
    @BeforeEach
    public void setUp() {
        Environment.clear();
    }

    @Test
    public void testNormal() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new EnvironmentCommand(Arrays.asList("x", "123"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        Assertions.assertTrue(Environment.get("x").isPresent());
        Assertions.assertEquals("123", Environment.get("x").get());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFewerArguments() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new EnvironmentCommand(List.of("x"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(1, command.execute());
        Assertions.assertTrue(Environment.get("x").isEmpty());
        String expectedError = "Command environment needs 2 arguments";
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
        Command command = new EnvironmentCommand(List.of("x", "y", "123"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(1, command.execute());
        Assertions.assertTrue(Environment.get("x").isEmpty());
        Assertions.assertTrue(Environment.get("y").isEmpty());
        String expectedError = "Command environment needs 2 arguments";
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
}
