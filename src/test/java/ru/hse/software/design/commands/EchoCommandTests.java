package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class EchoCommandTests {

    @Test
    public void testOneWord() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new EchoCommand(List.of("cli"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "cli";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSeveralWords() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();

        Command command = new EchoCommand(Arrays.asList("I", "love", "java"),
                                        new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "I love java";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyInput() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new EchoCommand(Collections.emptyList(),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
