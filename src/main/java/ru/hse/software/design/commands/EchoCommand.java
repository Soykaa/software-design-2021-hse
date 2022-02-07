package ru.hse.software.design.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Writes provided arguments separated by single blank (' ') characters and followed by a newline (`\n') character
 * to the standard output.
 **/
public class EchoCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Created echo command with given arguments.
     *
     * @param commandArgs command arguments
     **/
    public EchoCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "echo";
    }

    /**
     * Executes 'echo' command with the given arguments.
     *
     * @return 0
     **/
    @Override
    public int execute(String input) {
        output = String.join(" ", commandArgs);
        return 0;
    }
}
