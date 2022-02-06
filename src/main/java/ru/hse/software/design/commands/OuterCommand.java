package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;
import ru.hse.software.design.Path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes outer command in a separate process.
 **/
public class OuterCommand extends Command {
    private final List<String> commandWithArguments = new ArrayList<>();
    private final Path path;

    /**
     * Creates outer command with given arguments.
     *
     * @param commandName  command name
     * @param commandArgs  command arguments
     * @param path         paths to directories where search for given outer command should happen
     * @param inputStream  input stream
     * @param outputStream output stream
     * @param errorStream  error stream
     **/
    public OuterCommand(String commandName, List<String> commandArgs, Path path,
                        InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.commandWithArguments.add(commandName);
        this.commandWithArguments.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.path = path;
        this.command = commandName;
    }

    /**
     * Executes the given outer command with the given arguments.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    @Override
    public int execute() {
        try {
            String[] cmdarray = commandWithArguments.toArray(new String[0]);
            String[] envp = Environment.getAll();
            String commandDirectory = null;
            for (var directory : path.getPaths()) {
                if (new File(directory, command).exists()) {
                    commandDirectory = directory;
                }
            }
            if (commandDirectory == null) {
                appendErrorMessage("Command " + command + " not found");
                errorStream.writeAsString("Command " + command + " not found");
                return 1;
            }
            try {
                Process process = Runtime.getRuntime().exec(cmdarray, envp, new File(commandDirectory));
                process.getOutputStream().write(inputStream.readAsBytesArray());
                outputStream.writeAsBytesArray(process.getInputStream().readAllBytes());
                return process.waitFor();
            } catch (IOException | InterruptedException e) {
                appendErrorMessage(e.getMessage());
                errorStream.writeAsString(e.getMessage());
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
