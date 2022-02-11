package ru.hse.software.design.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reads files providing as arguments sequentially, writing them to the standard
 * output. The file operands are processed in command-line order. If arguments are absent,
 * cat reads from the standard input.
 **/
public class CatCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Creates cat command with given arguments.
     *
     * @param commandArgs command arguments
     **/
    public CatCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "cat";
    }

    /**
     * Executes 'cat' command with the given arguments.
     *
     * @param input input as string
     * @return 0 in case of successful outcome of the command, 1 otherwise
     **/
    @Override
    public int execute(String input) {
        if (commandArgs.size() > 1) {
            errorStream.println("Command cat works with one file or with standard input");
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
            var stringBuilder = new StringBuilder();
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
