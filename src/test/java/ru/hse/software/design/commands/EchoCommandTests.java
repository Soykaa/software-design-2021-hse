package ru.hse.software.design.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class EchoCommandTests {
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
    public void testOneWord() {
        Command command = new EchoCommand(List.of("cli"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "cli";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSeveralWords() {
        Command command = new EchoCommand(Arrays.asList("I", "love", "java"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "I love java";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEmptyInput() {
        Command command = new EchoCommand(Collections.emptyList());
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }
}