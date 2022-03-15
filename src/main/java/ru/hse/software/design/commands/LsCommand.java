package ru.hse.software.design.commands;

import ru.hse.software.design.Environment;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Prints contents of the directory
 **/
public class LsCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    /**
     * Created ls command with given arguments.
     *
     * @param commandArgs command arguments
     **/
    public LsCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "ls";
    }

    /**
     * Executes 'ls' command with the given arguments.
     *
     * @return 0
     **/
    @Override
    public int execute(String input) {
        Path path;
        if (commandArgs.isEmpty()) {
            path = Environment.getCurrentFolderPath();
        } else {
            path = Environment.getRelativePath(commandArgs.get(0));
        }
        var fileName = new File(String.valueOf(path));
        File[] fileList = fileName.listFiles();

        var sb = new StringBuilder();
        if (fileList == null) {
            errorStream.println("Can't find such directory");
            return 1;
        } else {
            for (File file : fileList) {
                sb.append(file.getName()).append("\n");
            }
            output = sb.toString();
            return 0;
        }
    }
}