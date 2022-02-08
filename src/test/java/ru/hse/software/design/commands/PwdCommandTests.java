package ru.hse.software.design.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.Environment;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

public class PwdCommandTests {
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
    public void testWithoutArguments() {
        Command command = new PwdCommand(Collections.emptyList());
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = System.getProperty("user.dir");
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWithArguments() {
        Command command = new PwdCommand(Arrays.asList("one", "two", "three"));
        Assertions.assertEquals(1, command.execute(""));
        String expectedError = "Command Pwd works without arguments" + System.lineSeparator();
        Assertions.assertEquals(expectedError, errContent.toString());
        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }
}