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
 * Displays the number of lines, words, and bytes contained in each input file, or standard input
 * (if no file is specified) to the standard output.
 **/
public class WCCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Creates pwd command with given arguments.
     *
     * @param commandArgs  command arguments
     * @param inputStream  input stream
     * @param outputStream output stream
     * @param errorStream  error stream
     **/
    public WCCommand(List<String> commandArgs, InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.command = "wc";
    }

    /**
     * Executes 'wc' command with the given arguments.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    @Override
    public int execute() {
        try {
            if (commandArgs.size() > 1) {
                appendErrorMessage("Command wc works with one file " + "or with standard input");
                errorStream.writeAsString("Command wc works with one file " + "or with standard input");
                return 1;
            }
            if (commandArgs.isEmpty()) {
                try {
                    String proceedingString = inputStream.readAsString();
                    if (proceedingString.equals("")) {
                        outputStream.writeAsString("1  0 0");
                        return 0;
                    }
                    String[] lines = proceedingString.split(System.lineSeparator());
                    long numLines = lines.length;
                    String linesToSpaces = proceedingString.replace(System.lineSeparator(), " ");
                    String[] words = linesToSpaces.split(" ");
                    long numWords = words.length;
                    byte[] bytes = proceedingString.getBytes(StandardCharsets.UTF_8);
                    long numBytes = bytes.length;
                    String result = numLines + " " + " " + numWords + " " + numBytes;
                    outputStream.writeAsString(result);
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

            try (Stream<String> stream = Files.lines(path)) {
                List<String> lines = stream.collect(Collectors.toList());
                long numBytes = Files.size(path);
                long numLines = lines.size();
                long numWords = 0;
                for (String line : lines) {
                    String[] words = line.split(" ");
                    numWords += words.length;
                }
                String result = numLines + " " + " " + numWords + " " + numBytes;
                outputStream.writeAsString(result);
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
