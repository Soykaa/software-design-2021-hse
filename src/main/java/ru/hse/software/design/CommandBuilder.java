package ru.hse.software.design;

import ru.hse.software.design.commands.CatCommand;
import ru.hse.software.design.commands.Command;
import ru.hse.software.design.commands.EchoCommand;
import ru.hse.software.design.commands.EnvironmentCommand;
import ru.hse.software.design.commands.ExitCommand;
import ru.hse.software.design.commands.OuterCommand;
import ru.hse.software.design.commands.PwdCommand;
import ru.hse.software.design.commands.WCCommand;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Class for converting a passed CommandToken to object of type Command.
 * Contains a static method that converts command names to their corresponding class objects.
 **/
public class CommandBuilder {
    /**
     * Creates a Command object corresponding to the passed commandToken.
     *
     * @param commandToken  command token
     * @param path          paths to directories containing external programs
     * @param cli           CLI object
     * @param commandOutput stream for command output
     * @param errorOutput   stream for errors
     * @return Command object
     **/
    public static Command build(CommandTokens commandToken, Path path, CLI cli,
                                PipedOutputStream commandInput, PipedInputStream commandOutput, PipedInputStream errorOutput) {
        InputStream inputStream = new InputStream(commandInput);
        OutputStream outputStream = new OutputStream(commandOutput);
        OutputStream errorStream = new OutputStream(errorOutput);
        if (commandToken.getCommand().equals("cat")) {
            return new CatCommand(commandToken.getCommandArgs(), inputStream, outputStream, errorStream);
        }
        if (commandToken.getCommand().equals("echo")) {
            return new EchoCommand(commandToken.getCommandArgs(), inputStream, outputStream, errorStream);
        }
        if (commandToken.getCommand().equals("exit")) {
            return new ExitCommand(cli, inputStream, outputStream, errorStream);
        }
        if (commandToken.getCommand().equals("pwd")) {
            return new PwdCommand(commandToken.getCommandArgs(), inputStream, outputStream, errorStream);
        }
        if (commandToken.getCommand().equals("wc")) {
            return new WCCommand(commandToken.getCommandArgs(), inputStream, outputStream, errorStream);
        }
        if (commandToken.getCommand().equals("environment")) {
            return new EnvironmentCommand(commandToken.getCommandArgs(), inputStream, outputStream, errorStream);
        }
        return new OuterCommand(commandToken.getCommand(), commandToken.getCommandArgs(), path,
            inputStream, outputStream, errorStream);
    }
}
