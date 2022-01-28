package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;
import ru.hse.software.design.Path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OuterCommand extends Command {
    private final List<String> commandWithArguments = new ArrayList<>();
    private final String commandName;
    private final Path path;

    public OuterCommand(String commandName, List<String> commandArgs,
                        InputStream inputStream, OutputStream outputStream, Path path) {
        this.commandWithArguments.add(commandName);
        this.commandWithArguments.addAll(commandArgs);
        this.commandName = commandName;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.path = path;
    }

    @Override
    public int execute() {
        String[] cmdarray = commandWithArguments.toArray(new String[0]);
        String[] envp = Environment.getAll();
        String commandDirectory = null;
        for (var directory : path.getPaths()) {
            if (new File(directory, commandName).exists()) {
                commandDirectory = directory;
            }
        }
        if (commandDirectory == null) {
            errorMessage = "Command " + commandName + " not found";
            return 1;
        }
        try {
            Process process = Runtime.getRuntime().exec(cmdarray, envp, new File(commandDirectory, commandName));
            process.getOutputStream().write(inputStream.readAsBytesArray());
            outputStream.writeAsBytesArray(process.getInputStream().readAllBytes());
            return process.waitFor();
        } catch (IOException | InterruptedException e) {
            errorMessage = e.getMessage();
        }
        return 0;
    }
}
