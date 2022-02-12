package ru.hse.software.design;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
        assertTrue(errContent.toString().contains("Cannot run program \"non-existent-command"));
    }

    @Test
    public void testSimpleGrepWithPipes() {
        assertEquals(0, executor.execute("echo 123 | grep 123"));
        assertEquals("123" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testSimpleGrepWithPipesAndSingleQuotes() {
        assertEquals(0, executor.execute("echo 123 | grep '123'"));
        assertEquals("123" + System.lineSeparator(), outContent.toString());
    }

    @Test
    public void testDoubleGrepWithPipesAndSingleQuotes() {
        assertEquals(0, executor.execute("echo 123 | grep \"123\""));
        assertEquals("123" + System.lineSeparator(), outContent.toString());
    }
}
