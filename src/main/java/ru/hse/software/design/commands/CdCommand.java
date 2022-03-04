package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;

import java.util.ArrayList;
import java.util.List;

public class CdCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    public CdCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "cd";
    }

    @Override
    public int execute(String input) {
        if (commandArgs.size() < 1) {
            errorStream.println("Command environment needs 0 or 1 argument");
            return 1;
        } else if (commandArgs.size() == 1) {
            Environment.setCurrentFolderPath(commandArgs.get(0));
        } else {
            Environment.setCurrentFolderPath();
        }

        return 0;
    }
}
