package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents 'environment' command, extends Command.
 * Contains list of command arguments as a private field.
 * Also overrides 'execute' method.
 **/
public class EnvironmentCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Constructor.
     * Makes commandArgs, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "environment".
     *
     * @param commandArgs  command arguments
     * @param inputStream  input stream
     * @param outputStream output stream
     * @param errorStream  error stream
     **/
    public EnvironmentCommand(List<String> commandArgs,
                              InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.command = "environment";
    }

    /**
     * Executes 'environment' command with the given arguments.
     * In case of error writes an appropriate message to the error stream.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    @Override
    public int execute() {
        try {
            if (commandArgs.size() != 2) {
                appendErrorMessage("Command environment needs 2 arguments");
                errorStream.writeAsString("Command environment needs 2 arguments");
                return 1;
            }
            Environment.set(commandArgs.get(0), commandArgs.get(1));
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        } finally {
            closeInputAndOutputStreams();
        }
    }
}
