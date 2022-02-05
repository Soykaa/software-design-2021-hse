package ru.hse.software.design.commands;

import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class which represents 'wc' command, extends Command.
 * Contains list of command arguments as a private field.
 * Also overrides 'execute' method.
 **/
public class WCCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Makes commandArgs, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "wc".
     *
     * @param commandArgs  command arguments
     **/
    public WCCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "wc";
    }

    /**
     * Executes 'wc' command with the given arguments.
     * In case of error writes an appropriate message to the error stream.
     *
     * @return 0 in case of successful outcome of the command, 1 otherwise
     **/
    @Override
    public int execute(String input) {
        if (commandArgs.size() > 1) {
            errorStream.println("Command wc works with one file " + "or with standard input");
            return 1;
        }
        if (commandArgs.isEmpty()) {
            if (input.equals("")) {
                output = "1  0 0";
                return 0;
            }
            String[] lines = input.split(System.lineSeparator());
            long numLines = lines.length;
            String linesToSpaces = input.replace(System.lineSeparator(), " ");
            String[] words = linesToSpaces.split(" ");
            long numWords = words.length;
            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            long numBytes = bytes.length;
            output = numLines + " " + " " + numWords + " " + numBytes;
            return 0;
        }
        Path path = Paths.get(commandArgs.get(0));
        if (!Files.exists(path)) {
            errorStream.println("file " + commandArgs.get(0) + " does not exist");
            return 1;
        }
        try (Stream<String> stream = Files.lines(path)) {
            List<String> lines = stream.collect(Collectors.toList());
            long numBytes = Files.size(path);
            long numLines = lines.size();
            long numWords = 0;
            for (String line : lines) {
                String[] words = line.split(" ");
                numWords += words.length;
            }
            output = numLines + " " + " " + numWords + " " + numBytes;
        } catch (IOException e) {
            errorStream.println("problem with reading from file " + e.getMessage());
            return 1;
        }
        return 0;
    }
}
