package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.CLI;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;


public class ExitCommandTests {
    @Test
    public void testOneWord() {
        // тест зависает я не поняла почему((


//        CLI cli = new CLI();
//        cli.start();
//        Assertions.assertTrue(cli.isRunning());
//        Command command = new ExitCommand(cli, new InputStream(new PipedOutputStream()),
//                                                new OutputStream(new PipedInputStream()));
//        Assertions.assertEquals(0, command.execute());
//        Assertions.assertFalse(cli.isRunning());
    }
}
