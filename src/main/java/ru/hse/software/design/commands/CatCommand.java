package ru.hse.software.design.commands;

import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class which represents 'cat' command, extends Command.
 * Contains list of command arguments as a private field.
 * Also overrides 'execute' method.
 **/
public class CatCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Makes commandArgs, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "cat".
     *
     * @param commandArgs  command arguments
     **/
    public CatCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "cat";
    }

    /**
     * Executes 'cat' command with the given arguments.
     * In case of error writes an appropriate message to the error stream.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    @Override
    public int execute(String input) {
        if (commandArgs.size() > 1) {
            errorStream.println("Command cat works with one file " +
                "or with standard input");
            return 1;
        }
        if (commandArgs.isEmpty()) {
            output = input;
            return 0;
        }
        Path path = Paths.get(commandArgs.get(0));
        if (!Files.exists(path)) {
            errorStream.println("file " + commandArgs.get(0) + " does not exist");
            return 1;
        }
        List<String> lines;
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.collect(Collectors.toList());
            StringBuilder stringBuilder = new StringBuilder();
            for (String line : lines) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
            output = stringBuilder.toString();
        } catch (IOException e) {
            errorStream.println("problem with reading from file" + e.getMessage());
            return 1;
        }
        return 0;
    }
}
