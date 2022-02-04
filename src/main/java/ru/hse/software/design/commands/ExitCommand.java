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
     * Constructor.
     * Makes classToExit, inputStream, outputStream and errorStream same as given values.
     * Also initialize command with "exit".
     *
     * @param classToExit  class where to call 'exit' method
     * @param inputStream  input stream
     * @param outputStream output stream
     * @param errorStream  error stream
     **/
    public ExitCommand(CLI classToExit, InputStream inputStream,
                       OutputStream outputStream, OutputStream errorStream) {
        this.classToExit = classToExit;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.command = "exit";
    }

    /**
     * Executes 'exit' command in a given class.
     *
     * @return 1 in case of successful outcome of the command, 0 otherwise
     **/
    @Override
    public int execute() {
        try {
            if (errorMessage.isPresent()) {
                return 1;
            }
            classToExit.exit();
            return 0;
        } finally {
            closeInputAndOutputStreams();
        }
    }
}
