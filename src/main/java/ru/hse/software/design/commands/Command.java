package ru.hse.software.design.commands;

import ru.hse.software.design.streams.IOStream;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.util.Optional;

/**
 * Abstract class for executing commands.
 * The class of each command will be inherited from it.
 * Contains command name, error message and input, output and error streams as a protected fields.
 * Also contains main abstract method 'execute' and several public, protected and private methods
 * as the helping ones.
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
     * @return an int, which represents the command outcome
     **/
    public abstract int execute();

    /**
     * Returns command name.
     *
     * @return command name as string
     **/
    public String getCommand() {
        return command;
    }

    /**
     * Returns an error message.
     *
     * @return error message as Optional
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
