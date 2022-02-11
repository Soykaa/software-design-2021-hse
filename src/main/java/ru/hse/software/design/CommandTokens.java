package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores the command name and its arguments list.
 **/
public class CommandTokens {
    private final String command;
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Created CommandTokens from given command name and arguments.
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
     * @return Command name as string
     **/
    public String getCommand() {
        return command;
    }

    /**
     * Returns command arguments.
     *
     * @return Command arguments as list
     **/
    public List<String> getCommandArgs() {
        return commandArgs;
    }
}
