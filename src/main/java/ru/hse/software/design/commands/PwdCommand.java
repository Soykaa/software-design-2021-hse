package ru.hse.software.design.commands;

import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

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
     * Constructor.
     * Makes commandArgs, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "pwd".
     *
     * @param commandArgs  command arguments
     * @param inputStream  input stream
     * @param outputStream output stream
     * @param errorStream  error stream
     **/
    public PwdCommand(List<String> commandArgs,
                      InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.command = "pwd";
    }

    /**
     * Executes 'pwd' command with the given arguments.
     * In case of error writes an appropriate message to the error stream.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    @Override
    public int execute() {
        try {
            if (!commandArgs.isEmpty()) {
                appendErrorMessage("Command Pwd works without arguments");
                errorStream.writeAsString("Command Pwd works without arguments");
                return 1;
            }
            String dir = System.getProperty("user.dir");
            try {
                outputStream.writeAsString(dir);
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
