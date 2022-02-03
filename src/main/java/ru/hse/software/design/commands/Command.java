package ru.hse.software.design.commands;

import ru.hse.software.design.IOStream;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PrintStream;
import java.util.Optional;

public abstract class Command {
    protected String command;
    protected Optional<String> errorMessage = Optional.empty();
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected OutputStream errorStream;
    public abstract int execute();

    public String getCommand() {
        return command;
    }

    public Optional<String> getErrorMessage() {
        return errorMessage;
    }

    private void closeStream(IOStream stream) {
        try {
            stream.close();
        } catch (IOException e) {
            appendErrorMessage(e.getMessage());
        }
    }

    protected void closeInputAndOutputStreams() {
        closeStream(inputStream);
        closeStream(outputStream);
    }

    protected void appendErrorMessage(String e) {
        errorMessage = errorMessage.map(s -> e + ", " + s).or(() -> Optional.of(e));
    }
}
