package ru.hse.software.design.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.Environment;
import ru.hse.software.design.Path;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

public class OuterCommandTests {
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
    public void testManyArguments() {
        Command command = new OuterCommand("echo", Arrays.asList("hello", "world"),
            new Path(System.getenv("PATH").split(":")));
        command.execute("");
        Assertions.assertTrue(errContent.toString().isEmpty());
        Assertions.assertEquals("hello world\n", command.output);
    }

    @Test
    public void testReadFromStdin() {
        Command command = new CatCommand(Collections.emptyList());
        command.execute("hello world");
        Assertions.assertTrue(errContent.toString().isEmpty());
        Assertions.assertEquals("hello world", command.output);
    }
}
