package ru.hse.software.design.commands;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.hse.software.design.CLI;


public class ExitCommandTests {
    @Test
    public void testExit() {
        CLI mockCLI = Mockito.mock(CLI.class);
        Command command = new ExitCommand(mockCLI);
        command.execute("");
        Mockito.verify(mockCLI).exit();
    }
}
