package ru.hse.software.design.commands;

import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CatCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    public CatCommand(List<String> commandArgs,
                      InputStream inputStream, OutputStream outputStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.command = "cat";
    }

    @Override
    public int execute() {
        try {
            if (commandArgs.size() > 1) {
                appendErrorMessage("Command cat works with one file " +
                    "or with standard input");
                return 1;
            }
            if (commandArgs.isEmpty()) {
                try {
                    outputStream.writeAsString(inputStream.readAsString());
                } catch (IOException e) {
                    appendErrorMessage(e.getMessage());
                    return 1;
                }
                return 0;
            }
            try (Stream<String> stream = Files.lines(Paths.get(commandArgs.get(0)))) {
                List<String> lines = stream.collect(Collectors.toList());
                for (String line : lines) {
                    line += '\n';
                    outputStream.writeAsString(line);
                }
            } catch (IOException e) {
                appendErrorMessage(e.getMessage());
                return 1;
            }
            return 0;
        } finally {
            closeInputAndOutputStreams();
        }
    }
}
