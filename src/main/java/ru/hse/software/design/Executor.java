package ru.hse.software.design;

import ru.hse.software.design.commands.Command;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Class for calling the main components of the application.
 * Contains paths to directories containing external programs and
 * CLI object as a private fields.
 * Also contains main method 'execute', which throws IOException.
 **/
public class Executor {
    private final Path path;
    private final CLI cli;

    /**
     * Makes cli same as given value, initializes path
     * with the directories listed in the PATH environment variable.
     *
     * @param cli CLI object
     **/
    public Executor(CLI cli) {
        this.path = new Path(System.getenv("PATH").split(":"));
        this.cli = cli;
    }

    /**
     * Takes a user-supplied string as input and calls the required components in the correct order.
     *
     * @param commandString user-supplied string
     * @return Return code
     * @throws IOException          thrown in case of problems with reading bytes from PipedInputStream
     * @throws InterruptedException thrown in case of thread.join()
     **/
    public int execute(String commandString) throws IOException, InterruptedException {
        List<Token> tokens = Lexer.getTokens(commandString);
        List<Token> processedTokens = PreProcessor.preProcess(tokens);
        CommandTokens commandTokens = Parser.preProcess(processedTokens);
        PipedInputStream commandOutput = new PipedInputStream();
        PipedInputStream errorOutput = new PipedInputStream();
        PipedOutputStream commandInput = new PipedOutputStream();
        Command command = CommandBuilder.build(commandTokens, path, cli, commandInput, commandOutput, errorOutput);
        commandInput.close();
        Thread thread = new Thread(() -> {
            try {
                System.out.println(new String(commandOutput.readAllBytes(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        int returnCode = command.execute();
        thread.join();
        if (returnCode != 0) {
            String errorMessage = "Failure while executing command " + command.getCommand();
            if (command.getErrorMessage().isPresent()) {
                errorMessage = errorMessage + " : " + command.getErrorMessage().get();
            }
            System.out.println(errorMessage);
        }
        return returnCode;
    }
}
