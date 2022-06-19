package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Change working directory according to arguments
 */
public class CdCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Created cd command with given arguments.
     *
     * @param commandArgs command arguments
     **/
    public CdCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "cd";
    }

    /**
     * Executes 'cd' command with the given arguments
     *
     * @param input input as string
     * @return 0 in case of successful outcome of the command, 1 otherwise
     */
    @Override
    public int execute(String input) {
        if (commandArgs.size() > 1) {
            errorStream.println("Command environment needs 0 or 1 argument");
            return 1;
        } else if (commandArgs.size() == 1) {
            if (!Environment.getRelativePath(commandArgs.get(0)).toFile().isDirectory()) {
                errorStream.println("Can't find such directory");
                return 1;
            }
            Environment.setCurrentFolderPath(commandArgs.get(0));
        } else {
            Environment.setCurrentFolderPath();
        }

        return 0;
    }
}
