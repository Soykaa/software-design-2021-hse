package ru.hse.software.design.commands;

import ru.hse.software.design.CLI;

/**
 * Exits command-line interpreter.
 **/
public class ExitCommand extends Command {
    private final CLI classToExit;

    /**
     * Created exit command with given arguments.
     *
     * @param classToExit class where to call 'exit' method
     **/
    public ExitCommand(CLI classToExit) {
        this.classToExit = classToExit;
        this.command = "exit";
    }

    /**
     * Executes 'exit' command in a given class.
     *
     * @param input input as string
     * @return 0
     **/
    @Override
    public int execute(String input) {
        classToExit.exit();
        return 0;
    }
}