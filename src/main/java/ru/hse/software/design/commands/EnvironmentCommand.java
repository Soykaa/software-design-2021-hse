package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    public EnvironmentCommand(List<String> commandArgs,
                              InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.command = "environment";
    }

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
