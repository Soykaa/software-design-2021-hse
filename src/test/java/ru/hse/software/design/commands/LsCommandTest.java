package ru.hse.software.design.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class LsCommandTest {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Test
    public void testNoArgs() {
        Command command = new LsCommand(List.of());

        Path path = Environment.getCurrentFolderPath();

        File fileName = new File(String.valueOf(path));
        File[] fileList = fileName.listFiles();

        StringBuilder sb = new StringBuilder();
        assert fileList != null;
        for (File file : fileList) {
            sb.append(file.getName()).append("\n");
        }

        String expectedOutput = sb.toString();
        Assertions.assertEquals(0, command.execute(""));
        String actualOutput = command.output;
        Assertions.assertTrue(errContent.toString().isEmpty());
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWithArg() {
        Command command = new LsCommand(List.of("lol"));


        String expectedOutput;
        Path path = Environment.getCurrentFolderPath();

        File fileName = new File(String.valueOf(path));

        File theDir = new File(fileName, "lol");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        File theDir2 = new File(theDir, "kek");
        if (!theDir2.exists()) {
            theDir2.mkdirs();
        }

        expectedOutput = "kek\n";

        Assertions.assertEquals(0, command.execute(""));
        String actualOutput = command.output;
        Assertions.assertTrue(errContent.toString().isEmpty());
        Assertions.assertEquals(expectedOutput, actualOutput);
        theDir2.delete();
        theDir.delete();
    }
}
