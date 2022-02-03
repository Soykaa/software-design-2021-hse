package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    public EnvironmentCommand(List<String> commandArgs,
                              InputStream inputStream, OutputStream outputStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.command = "environment";
    }

    @Override
    public int execute() {
        try {
            if (commandArgs.size() != 2) {
                appendErrorMessage("Command environment needs 2 arguments");
                errorStream.println("Command environment needs 2 arguments");
                return 1;
            }
            Environment.set(commandArgs.get(0), commandArgs.get(1));
            return 0;
        } finally {
            closeInputAndOutputStreams();
        }
    }
}
