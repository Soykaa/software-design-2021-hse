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
     * Makes commandArgs, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "environment".
     *
     * @param commandArgs  command arguments
     **/
    public EnvironmentCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "environment";
    }

    /**
     * Executes 'environment' command with the given arguments.
     * In case of error writes an appropriate message to the error stream.
     *
     * @return 0  in case of successful outcome of the command, 1 otherwise
     **/
    @Override
    public int execute(String input) {
        if (commandArgs.size() != 2){
            errorStream.println("Command environment needs 2 arguments");
            return 1;
        }
        Environment.set(commandArgs.get(0), commandArgs.get(1));
        return 0;
    }
}
