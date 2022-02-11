package ru.hse.software.design.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Writes the absolute pathname of the current working directory to the standard output.
 **/
public class PwdCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Creates pwd command with given arguments.
     *
     * @param commandArgs command arguments
     **/
    public PwdCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "pwd";
    }

    /**
     * Executes 'pwd' command with the given arguments.
     *
     * @param input input as string
     * @return 0 in case of successful outcome of the command, 1 otherwise
     **/
    @Override
    public int execute(String input) {
        if (!commandArgs.isEmpty()) {
            errorStream.println("Command Pwd works without arguments");
            return 1;
        }
        output = System.getProperty("user.dir");
        return 0;
    }
}