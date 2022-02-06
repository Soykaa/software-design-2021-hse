package ru.hse.software.design.commands;

import ru.hse.software.design.streams.IOStream;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

import java.io.IOException;
import java.io.PrintStream;
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
    protected PrintStream errorStream = System.err;
    protected String output = "";

    /**
     * Executes command.
     * @param input input stream
     * @return An int, which represents the command outcome
     **/
    public abstract int execute(String input);

    /**
     * Returns command name.
     *
     * @return Command name as string
     **/
    public String getCommand() {
        return command;
    }

    public String getOutput() {
        return output;
    }

}
