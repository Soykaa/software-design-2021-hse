package ru.hse.software.design.commands;

import java.io.PrintStream;


/**
 * Abstract class for executing commands.
 * The class of each command will be inherited from it.
 **/
public abstract class Command {
    protected String command;
    protected PrintStream errorStream = System.err;
    protected String output = "";

    /**
     * Executes command.
     *
     * @param input input as string
     * @return 0 in case of successful outcome of the command, 1 otherwise
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

    /**
     * Returns command output.
     *
     * @return Command output as string
     **/
    public String getOutput() {
        return output;
    }
}
