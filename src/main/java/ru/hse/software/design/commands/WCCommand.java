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
    }

    @Override
    public int execute() {
        if (commandArgs.size() > 1) {
            errorMessage = "Command wc works with one file " +
                "or with standard input";
            return 1;
        }
        if (commandArgs.isEmpty()) {
            try {
                String proceedingString = inputStream.readAsString();
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
                errorMessage = e.getMessage();
                return 1;
            }
            return 0;
        }
        Path path = Paths.get(commandArgs.get(0));
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
            errorMessage = e.getMessage();
            return 1;
        }
        return 0;
    }
}
