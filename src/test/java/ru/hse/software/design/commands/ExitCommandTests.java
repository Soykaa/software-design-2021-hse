package ru.hse.software.design.commands;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.hse.software.design.CLI;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;


public class ExitCommandTests {
    @Test
    public void testExit() {
        CLI mockCLI = Mockito.mock(CLI.class);
        Command command = new ExitCommand(mockCLI, new InputStream(new PipedOutputStream()),
            new OutputStream(new PipedInputStream()), new OutputStream(new PipedInputStream()));
        command.execute();
        Mockito.verify(mockCLI).exit();
    }
}
