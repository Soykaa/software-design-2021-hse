package ru.hse.software.design;

import ru.hse.software.design.commands.Command;

import java.io.IOException;
import java.io.PipedInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Executor {
    private final Path path;
    private final CLI cli;

    public Executor(CLI cli) {
        this.path = new Path(System.getenv("PATH").split(":"));
        this.cli = cli;
    }

    public int execute(String commandString) throws IOException {
        List<Token> tokens = Lexer.getTokens(commandString);
        CommandTokens commandTokens = Parser.preProcess(tokens);
        PipedInputStream commandOutput = new PipedInputStream();
        Command command = CommandBuilder.build(commandTokens, path, cli, commandOutput);
        int returnCode = command.execute();
        if (returnCode != 0) {
            String errorMessage = "Failure while executing command " + command.getCommand();
            if (command.getErrorMessage().isPresent()) {
                errorMessage = errorMessage + " : " + command.getErrorMessage().get();
            }
            System.out.println(errorMessage);
        }
        System.out.println(new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8));
        return 0;
    }
}
