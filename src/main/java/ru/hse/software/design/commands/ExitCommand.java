package ru.hse.software.design.commands;

import ru.hse.software.design.CLI;
import ru.hse.software.design.streams.InputStream;
import ru.hse.software.design.streams.OutputStream;

/**
 * Class which represents 'exit' command, extends Command.
 * Contains CLI class object as the object in which the exit method is called.
 * Also overrides 'execute' method.
 **/
public class ExitCommand extends Command {
    private final CLI classToExit;

    /**
     * Makes classToExit, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "exit".
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
     * @return 0
     **/
    @Override
    public int execute(String input) {
        classToExit.exit();
        return 0;
    }
}
