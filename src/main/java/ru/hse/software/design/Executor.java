package ru.hse.software.design;

import ru.hse.software.design.commands.Command;

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
     * @return Return code
     **/
    public int execute(String commandString) {
        try {
            List<Token> tokens = Lexer.getTokens(commandString);
            List<Token> preProcessedTokens = PreProcessor.preProcess(tokens);
            List<CommandTokens> commandTokens = Parser.preProcess(preProcessedTokens);
            List<Command> commands = CommandBuilder.build(commandTokens, cli);
            String prevCommandOutput = "";
            int returnCode = 0;
            for (Command command : commands) {
                returnCode = command.execute(prevCommandOutput);
                if (command.getCommand().equals("exit")) {
                    return returnCode;
                }
                if (returnCode != 0) {
                    return returnCode;
                }
                prevCommandOutput = command.getOutput();
            }
            if (!prevCommandOutput.isEmpty()) {
                System.out.println(prevCommandOutput);
            }
            return returnCode;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
    }
}
