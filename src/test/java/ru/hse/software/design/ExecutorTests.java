package ru.hse.software.design;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
    public void testCommandExecutedOk() throws IOException, InterruptedException {
        assertEquals(0, executor.execute("echo 42"));
        assertEquals("42\n", outContent.toString());
    }

    @Test
    public void testCommandFailed() throws IOException, InterruptedException {
        assertEquals(1, executor.execute("non-existent-command"));
        assertEquals("\nFailure while executing command non-existent-command : Command non-existent-command not found\n",
            outContent.toString());
    }
}
