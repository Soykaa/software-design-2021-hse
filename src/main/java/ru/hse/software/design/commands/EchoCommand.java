package ru.hse.software.design.commands;

import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EchoCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    public EchoCommand(List<String> commandArgs,
                       InputStream inputStream, OutputStream outputStream) {
        this.commandArgs.addAll(commandArgs);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.command = "echo";
    }

    @Override
    public int execute() {
        try {
            String result = String.join(" ", commandArgs);
            try {
                outputStream.writeAsString(result);
            } catch (IOException e) {
                appendErrorMessage(e.getMessage());
                errorStream.println(e.getMessage());
                return 1;
            }
            return 0;
        } finally {
            closeInputAndOutputStreams();
        }
    }
}
