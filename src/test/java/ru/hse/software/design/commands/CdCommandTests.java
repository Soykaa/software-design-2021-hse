package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CdCommandTests {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Test
    public void testGoBack() {
        Command pwd1 = new PwdCommand(List.of());
        pwd1.execute("");

        Command cd1 = new CdCommand(List.of("src"));
        cd1.execute("");

        Command cd2 = new CdCommand(List.of(".."));
        cd2.execute("");

        Command pwd2 = new PwdCommand(List.of());
        pwd2.execute("");

        Assertions.assertEquals(pwd1.output, pwd2.output);
    }

    @Test
    public void testWithoutParams() {
        Command cd = new CdCommand(List.of());
        cd.execute("");

        Command pwd = new PwdCommand(List.of());
        pwd.execute("");

        Assertions.assertEquals(pwd.output, System.getProperty("user.home"));
    }

    @Test
    public void testWithTooManyParams() {
        Command cd = new CdCommand(List.of("..", ".."));
        Assertions.assertEquals(1, cd.execute(""));
    }
}
