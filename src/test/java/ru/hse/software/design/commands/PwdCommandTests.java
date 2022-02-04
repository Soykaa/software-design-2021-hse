package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

public class PwdCommandTests {
    @Test
    public void testWithoutArguments() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new PwdCommand(Collections.emptyList(),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(0, command.execute());
        Assertions.assertTrue(command.getErrorMessage().isEmpty());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = System.getProperty("user.dir");
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithArguments() {
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        Command command = new PwdCommand(Arrays.asList("one", "two", "three"),
            new InputStream(new PipedOutputStream()), new OutputStream(commandOutput), new OutputStream(errorOutput));
        Assertions.assertEquals(1, command.execute());
        String expectedError = "Command Pwd works without arguments";
        Assertions.assertTrue(command.getErrorMessage().isPresent());
        Assertions.assertEquals(expectedError, command.getErrorMessage().get());
        try {
            String actualOutput = new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8);
            String expectedOutput = "";
            Assertions.assertEquals(expectedOutput, actualOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
