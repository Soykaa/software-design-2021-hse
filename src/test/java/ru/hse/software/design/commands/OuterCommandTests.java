package ru.hse.software.design.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.OS;
import ru.hse.software.design.Environment;
import ru.hse.software.design.Path;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

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
    public void testEchoCommand() {
        Command command;
        if (OS.WINDOWS.isCurrentOs()) {
            command = new OuterCommand("cmd", Arrays.asList("/C", "echo", "123"),
                new Path(System.getenv("PATH").split(System.getProperty("path.separator"))));
        } else {
            command = new OuterCommand("echo", List.of("123"),
                new Path(System.getenv("PATH").split(System.getProperty("path.separator"))));
        }
        command.execute("");
        Assertions.assertTrue(errContent.toString().isEmpty());
        Assertions.assertEquals("123" + System.lineSeparator(), command.output);
    }
}