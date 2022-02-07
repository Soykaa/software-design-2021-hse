package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;
import ru.hse.software.design.Path;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
     * @param commandName command name
     * @param commandArgs command arguments
     * @param path        path to command
     **/
    public OuterCommand(String commandName, List<String> commandArgs, Path path) {
        this.commandWithArguments.add(commandName);
        this.commandWithArguments.addAll(commandArgs);
        this.path = path;
        this.command = commandName;
    }

    /**
     * Executes the given outer command with the given arguments.
     *
     * @param input input as string
     * @return 0 in case of successful outcome of the command, 1 otherwise
     **/
    @Override
    public int execute(String input) {
        String[] cmdarray = commandWithArguments.toArray(new String[0]);
        String[] envp = Environment.getAll();
        String commandDirectory = null;
        for (var directory : path.getPaths()) {
            if (new File(directory, command).exists()) {
                commandDirectory = directory;
            }
        }
        if (commandDirectory == null) {
            errorStream.println("Command " + command + " not found");
            return 1;
        }
        try {
            Process process = Runtime.getRuntime().exec(cmdarray, envp, new File(commandDirectory));
            process.getOutputStream().write(input.getBytes(StandardCharsets.UTF_8));
            int returnCode = process.waitFor();
            output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String errorMessage = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            if (!errorMessage.isEmpty()) {
                errorStream.println(errorMessage);
            }
            return returnCode;
        } catch (IOException | InterruptedException e) {
            errorStream.println(e.getMessage());
        }
        return 0;
    }
}
