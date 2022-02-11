package ru.hse.software.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CLITests {
    @Test
    public void testCLIRunning() throws InterruptedException {
        var cli = new CLI();
        Assertions.assertFalse(cli.isRunning());
        var thread = new Thread(cli::start);
        thread.start();
        Thread.sleep(100);
        Assertions.assertTrue(cli.isRunning());
        cli.exit();
        Assertions.assertFalse(cli.isRunning());
    }
}

