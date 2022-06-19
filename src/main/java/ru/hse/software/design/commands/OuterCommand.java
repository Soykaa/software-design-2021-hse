package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Executes outer command in a separate process.
 **/
public class OuterCommand extends Command {
    private final List<String> commandWithArguments = new ArrayList<>();

    private String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    /**
     * Creates outer command with given arguments.
     *
     * @param commandName command name
     * @param commandArgs command arguments
     **/
    public OuterCommand(String commandName, List<String> commandArgs) {
        if (getOperatingSystem().contains("Windows")) {
            commandName = commandName + ".exe";
        }
        this.commandWithArguments.add(commandName);
        this.commandWithArguments.addAll(commandArgs);
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
        String[] commandArray = commandWithArguments.toArray(new String[0]);
        try {
            var processBuilder = new ProcessBuilder(commandArray);
            Map<String, String> environment = processBuilder.environment();
            processBuilder.directory(Environment.getCurrentFolderPath().toFile());
            environment.putAll(Environment.getAll());
            Process process = processBuilder.start();
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
        return 1;
    }
}