package ru.hse.software.design.commands;

import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents 'pwd' command, extends Command.
 * Contains list of command arguments as a private field.
 * Also overrides 'execute' method.
 **/
public class PwdCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Makes commandArgs, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "pwd".
     *
     * @param commandArgs  command arguments
     **/
    public PwdCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "pwd";
    }

    /**
     * Executes 'pwd' command with the given arguments.
     * In case of error writes an appropriate message to the error stream.
     *
     * @return 0 in case of successful outcome of the command, 1 otherwise
     **/
    @Override
    public int execute(String input) {
        if (!commandArgs.isEmpty()) {
            errorStream.println("Command Pwd works without arguments");
            return 1;
        }
        output = System.getProperty("user.dir");
        return 0;
    }
}
