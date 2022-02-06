package ru.hse.software.design.commands;

import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes provided arguments separated by single blank (' ') characters and followed by a newline (`\n') character
 * to the standard output.
 **/
public class EchoCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Created echo command with given arguments.
     *
     * @param commandArgs  command arguments
     * @param inputStream  input stream
     * @param outputStream output stream
     * @param errorStream  error stream
     **/
    public EchoCommand(List<String> commandArgs,
                       InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.command = "echo";
    }

    /**
     * Executes 'echo' command with the given arguments.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    @Override
    public int execute() {
        try {
            String result = String.join(" ", commandArgs);
            try {
                outputStream.writeAsString(result);
            } catch (IOException e) {
                appendErrorMessage(e.getMessage());
                errorStream.writeAsString(e.getMessage());
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
