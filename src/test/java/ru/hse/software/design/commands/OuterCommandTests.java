package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OuterCommandTests {
    @Test
    public void testArguments() throws IOException {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        Command command = new CatCommand(List.of("src/resources/not_empty_file.txt"), new InputStream(commandInput),
            new OutputStream(commandOutput), new OutputStream(errorOutput));
        String expectedOutput = Files.readString(Path.of("src/resources/not_empty_file.txt"), StandardCharsets.UTF_8) + System.lineSeparator();
        commandInput.close();
        command.execute();
        Assertions.assertEquals( expectedOutput, new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8));
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
