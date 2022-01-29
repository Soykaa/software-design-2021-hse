package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

public class CommandTokens {
    public String getCommand() {
        return command;
    }

    public List<String> getCommandArgs() {
        return commandArgs;
    }

    private final String command;
    private final List<String> commandArgs = new ArrayList<>();

    public CommandTokens(String command, List<String> commandArgs) {
        this.command = command;
        this.commandArgs.addAll(commandArgs);
    }

}
