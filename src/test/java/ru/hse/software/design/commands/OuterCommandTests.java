package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.OS;
import ru.hse.software.design.Path;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OuterCommandTests {
    @Test
    public void testArguments() throws IOException {
        Command command;
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        if (OS.WINDOWS.isCurrentOs()) {
            command = new OuterCommand("cmd.exe", Arrays.asList("/c", "echo", "123"),
                new Path(System.getenv("PATH").split(System.getProperty("path.separator"))),
                new InputStream(commandInput),
                new OutputStream(commandOutput), new OutputStream(errorOutput));
        } else {
            command = new OuterCommand("echo", List.of("123"),
                new Path(System.getenv("PATH").split(System.getProperty("path.separator"))),
                new InputStream(commandInput),
                new OutputStream(commandOutput), new OutputStream(errorOutput));
        }
        commandInput.close();
        command.execute();
        Assertions.assertEquals("123" + System.lineSeparator(), new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8));
    }
}
