package ru.hse.software.design;

import ru.hse.software.design.commands.Command;

import java.io.IOException;
import java.util.List;

/**
 * Class for executing user commands.
 **/
public class Executor {
    private final CLI cli;

    /**
     * Creates new Executor instance with given CLI object.
     *
     * @param cli CLI object
     **/
    public Executor(CLI cli) {
        this.cli = cli;
    }

    /**
     * Takes a user-supplied string as input and calls the required components in the correct order.
     *
     * @param commandString user-supplied string
     * @return 0 if execution was successful or error code otherwise
     * @throws IOException          thrown in case of problems with reading bytes from PipedInputStream
     * @throws InterruptedException thrown in case of thread.join()
     **/
    public int execute(String commandString) throws IOException, InterruptedException {
        List<Token> tokens = Lexer.getTokens(commandString);
        CommandTokens commandTokens = Parser.preProcess(tokens);
        Command command = CommandBuilder.build(commandTokens, cli);
        int returnCode = command.execute("");
        System.out.println(command.getOutput());
        return returnCode;
    }
}
