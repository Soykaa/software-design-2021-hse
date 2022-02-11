package ru.hse.software.design;

import ru.hse.software.design.commands.CatCommand;
import ru.hse.software.design.commands.Command;
import ru.hse.software.design.commands.EchoCommand;
import ru.hse.software.design.commands.EnvironmentCommand;
import ru.hse.software.design.commands.ExitCommand;
import ru.hse.software.design.commands.OuterCommand;
import ru.hse.software.design.commands.PwdCommand;
import ru.hse.software.design.commands.WCCommand;

/**
 * Class for converting a passed CommandToken to object of type Command.
 * Contains a static method that converts command names to their corresponding class objects.
 **/
public class CommandBuilder {
    /**
     * Creates a Command object corresponding to the passed commandToken.
     *
     * @param commandToken command token
     * @param cli          CLI object
     * @return Command object
     **/
    public static Command build(CommandTokens commandToken, CLI cli) {
        if (commandToken.getCommand().equals("cat")) {
            return new CatCommand(commandToken.getCommandArgs());
        }
        if (commandToken.getCommand().equals("echo")) {
            return new EchoCommand(commandToken.getCommandArgs());
        }
        if (commandToken.getCommand().equals("exit")) {
            return new ExitCommand(cli);
        }
        if (commandToken.getCommand().equals("pwd")) {
            return new PwdCommand(commandToken.getCommandArgs());
        }
        if (commandToken.getCommand().equals("wc")) {
            return new WCCommand(commandToken.getCommandArgs());
        }
        if (commandToken.getCommand().equals("environment")) {
            return new EnvironmentCommand(commandToken.getCommandArgs());
        }
        return new OuterCommand(commandToken.getCommand(), commandToken.getCommandArgs());
    }
}
