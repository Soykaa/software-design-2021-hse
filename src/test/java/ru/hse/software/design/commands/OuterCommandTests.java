package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;
import ru.hse.software.design.Path;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

public class OuterCommandTests {
    @Test
    public void testManyArguments() throws IOException {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        Command command = new OuterCommand("echo", Arrays.asList("hello", "world"),
            new Path(System.getenv("PATH").split(":")), new InputStream(commandInput),
            new OutputStream(commandOutput), new OutputStream(errorOutput));
        commandInput.close();
        command.execute();
        Assertions.assertEquals("hello world\n", new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8));
    }

    @Test
    public void testReadFromStdin() throws IOException {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        Command command = new CatCommand(Collections.emptyList(), new InputStream(commandInput),
            new OutputStream(commandOutput), new OutputStream(errorOutput));
        commandInput.write("hello world".getBytes(StandardCharsets.UTF_8));
        commandInput.close();
        command.execute();
        Assertions.assertEquals("hello world", new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8));
    }
}
