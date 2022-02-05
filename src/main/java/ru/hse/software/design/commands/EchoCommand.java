package ru.hse.software.design.commands;

import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents 'echo' command, extends Command.
 * Contains list of command arguments as a private field.
 * Also overrides 'execute' method.
 **/
public class EchoCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Makes commandArgs, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "echo".
     *
     * @param commandArgs  command arguments
     **/
    public EchoCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "echo";
    }

    /**
     * Executes 'echo' command with the given arguments.
     * In case of error writes an appropriate message to the error stream.
     *
     * @return 0
     **/
    @Override
    public int execute(String input) {
        output = String.join(" ", commandArgs);
        return 0;
    }
}
