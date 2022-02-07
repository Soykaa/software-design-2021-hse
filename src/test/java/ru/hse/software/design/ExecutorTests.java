package ru.hse.software.design;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ExecutorTests {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final Executor executor = new Executor(new CLI());

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testCommandExecutedOk() {
        assertEquals(0, executor.execute("echo 42"));
        assertEquals("42" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testCommandFailed() {
        assertEquals(1, executor.execute("non-existent-command"));
        assertEquals("Command non-existent-command not found" + System.lineSeparator(), errContent.toString());
    }

    @Test
    public void testEchoCatExecutedOk() {
        assertEquals(0, executor.execute("echo 42 | cat"));
        assertEquals("42" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testEchoWcExecutedOk() {
        assertEquals(0, executor.execute("echo 42 | wc"));
        assertEquals("1  1 2" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testWcFileEchoOk() {
        assertEquals(0, executor.execute("cat src/resources/not_empty_file.txt | echo 42"));
        assertEquals("42" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testSeveralPipesOK() {
        assertEquals(0, executor.execute("pwd | echo 123 | wc"));
        assertEquals("1  1 3" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testWrongPipesFailed() {
        assertEquals(1, executor.execute("echo 123 | | wc"));
        assertEquals("'|' must be between commands" + System.lineSeparator(), errContent.toString());
    }
}