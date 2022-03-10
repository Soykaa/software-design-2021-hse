package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CdCommandTests {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Test
    void testGoBack() {
        Command pwd1 = new PwdCommand(List.of());
        pwd1.execute("");

        Command ls1 = new CdCommand(List.of("src"));
        ls1.execute("");

        Command ls2 = new CdCommand(List.of(".."));
        ls2.execute("");

        Command pwd2 = new PwdCommand(List.of());
        pwd2.execute("");

        Assertions.assertEquals(pwd1.output, pwd2.output);
    }
}
