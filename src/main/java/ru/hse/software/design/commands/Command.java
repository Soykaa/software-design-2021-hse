package ru.hse.software.design.commands;

import ru.hse.software.design.streams.IOStream;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.util.Optional;

/**
 * Abstract class for executing commands.
 * The class of each command will be inherited from it.
 **/
public abstract class Command {
    protected String command;
    protected Optional<String> errorMessage = Optional.empty();
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected OutputStream errorStream;

    /**
     * Executes command.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    public abstract int execute();

    /**
     * Returns command name.
     *
     * @return Command name as string
     **/
    public String getCommand() {
        return command;
    }

    /**
     * Returns an error message.
     *
     * @return Error message as Optional
     **/
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
