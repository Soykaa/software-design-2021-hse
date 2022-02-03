package ru.hse.software.design.commands;

import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WCCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    public WCCommand(List<String> commandArgs,
                     InputStream inputStream, OutputStream outputStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.command = "wc";
    }

    @Override
    public int execute() {
        try {
            if (commandArgs.size() > 1) {
                appendErrorMessage("Command wc works with one file " +
                    "or with standard input");
                errorStream.println("Command wc works with one file " +
                    "or with standard input");
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
                    String linesToSpaces = proceedingString.replace(System.lineSeparator(),
                        " ");
                    String[] words = linesToSpaces.split(" ");
                    long numWords = words.length;
                    byte[] bytes = proceedingString.getBytes(StandardCharsets.UTF_8);
                    long numBytes = bytes.length;
                    String result = numLines + " " + " " + numWords + " " + numBytes;
                    outputStream.writeAsString(result);
                } catch (IOException e) {
                    appendErrorMessage(e.getMessage());
                    errorStream.println(e.getMessage());
                    return 1;
                }
                return 0;
            }
            Path path = Paths.get(commandArgs.get(0));
            if (!Files.exists(path)) {
                appendErrorMessage("file " + commandArgs.get(0) + " does not exist");
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
                String result = numLines + " " + " " + numWords + " " + numBytes;
                outputStream.writeAsString(result);
            } catch (IOException e) {
                appendErrorMessage("problem with writing from file to output stream" + e.getMessage());
                errorStream.println("problem with writing from file to output stream" + e.getMessage());
                return 1;
            }

            return 0;
        } finally {
            closeInputAndOutputStreams();
        }
    }
}
