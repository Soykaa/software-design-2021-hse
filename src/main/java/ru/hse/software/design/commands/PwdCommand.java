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
    }

    @Override
    public int execute() {
        if (!commandArgs.isEmpty()) {
            errorMessage = "Command Pwd works without arguments";
            return 1;
        }
        String dir = System.getProperty("user.dir");
        try {
            outputStream.writeAsString(dir);
        } catch (IOException e) {
            errorMessage = e.getMessage();
            return 1;
        }
        return 0;
    }
}
