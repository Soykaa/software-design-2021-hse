package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores the command name and its arguments list
 * as a private fields.
 **/
public class CommandTokens {
    private final String command;
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Constructor.
     * Makes command and commandArgs same as given values.
     *
     * @param command     command name
     * @param commandArgs command arguments names
     **/
    public CommandTokens(String command, List<String> commandArgs) {
        this.command = command;
        this.commandArgs.addAll(commandArgs);
    }

    /**
     * Returns command name.
     *
     * @return command name as string
     **/
    public String getCommand() {
        return command;
    }

    /**
     * Returns command arguments names.
     *
     * @return command arguments names as list
     **/
    public List<String> getCommandArgs() {
        return commandArgs;
    }
}
