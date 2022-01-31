package ru.hse.software.design.commands;

import ru.hse.software.design.CLI;
import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

public class ExitCommand extends Command {
    private final CLI classToExit;

    public ExitCommand(CLI classToExit, InputStream inputStream,
                       OutputStream outputStream) {
        this.classToExit = classToExit;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.command = "exit";
    }

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
