package ru.hse.software.design.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.Environment;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class EnvironmentCommandTest {
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
    public void testNormal() {
        Command command = new EnvironmentCommand(Arrays.asList("x", "123"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        Assertions.assertTrue(Environment.get("x").isPresent());
        Assertions.assertEquals("123", Environment.get("x").get());
        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFewerArguments() {
        Command command = new EnvironmentCommand(List.of("x"));
        Assertions.assertEquals(1, command.execute(""));
        Assertions.assertTrue(Environment.get("x").isEmpty());
        String expectedError = "Command environment needs 2 arguments" + System.lineSeparator();
        Assertions.assertEquals(expectedError, errContent.toString());

        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMoreArguments() {
        Command command = new EnvironmentCommand(List.of("x", "y", "123"));
        Assertions.assertEquals(1, command.execute(""));
        Assertions.assertTrue(Environment.get("x").isEmpty());
        Assertions.assertTrue(Environment.get("y").isEmpty());
        String expectedError = "Command environment needs 2 arguments" + System.lineSeparator();
        Assertions.assertEquals(expectedError, errContent.toString());

        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }
}