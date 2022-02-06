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
 * Reads files providing as arguments sequentially, writing them to the standard
 * output. The file operands are processed in command-line order. If arguments are absent,
 * cat reads from the standard input.
 **/
public class CatCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Creates cat command with given arguments.
     *
     * @param commandArgs  files relative paths
     * @param inputStream  input stream
     * @param outputStream output stream
     * @param errorStream  error stream
     **/
    public CatCommand(List<String> commandArgs,
                      InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.command = "cat";
    }

    /**
     * Executes 'cat' command with the given arguments.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    @Override
    public int execute() {
        try {
            if (commandArgs.size() > 1) {
                appendErrorMessage("Command cat works with one file " +
                    "or with standard input");
                errorStream.writeAsString("Command cat works with one file " +
                    "or with standard input");
                return 1;
            }
            if (commandArgs.isEmpty()) {
                try {
                    outputStream.writeAsString(inputStream.readAsString());
                } catch (IOException e) {
                    appendErrorMessage(e.getMessage());
                    errorStream.writeAsString(e.getMessage());
                    return 1;
                }
                return 0;
            }
            Path path = Paths.get(commandArgs.get(0));
            if (!Files.exists(path)) {
                appendErrorMessage("file " + commandArgs.get(0) + " does not exist");
                errorStream.writeAsString("file " + commandArgs.get(0) + " does not exist");
                return 1;
            }
            List<String> lines;
            try (Stream<String> stream = Files.lines(path)) {
                lines = stream.collect(Collectors.toList());
                for (String line : lines) {
                    line += '\n';
                    outputStream.writeAsString(line);
                }
            } catch (IOException e) {
                appendErrorMessage("problem with writing from file to output stream" + e.getMessage());
                errorStream.writeAsString("problem with writing from file to output stream" + e.getMessage());
                return 1;
            }
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        } finally {
            closeInputAndOutputStreams();
        }
    }
}
