package ru.hse.software.design.commands;

import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PwdCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    public PwdCommand(List<String> commandArgs,
                      InputStream inputStream, OutputStream outputStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.command = "pwd";
    }

    @Override
    public int execute() {
        try {
            if (!commandArgs.isEmpty()) {
                appendErrorMessage("Command Pwd works without arguments");
                return 1;
            }
            String dir = System.getProperty("user.dir");
            try {
                outputStream.writeAsString(dir);
            } catch (IOException e) {
                appendErrorMessage(e.getMessage());
                return 1;
            }
            return 0;
        } finally {
            closeInputAndOutputStreams();
        }
    }
}
