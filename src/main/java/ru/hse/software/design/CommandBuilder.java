package ru.hse.software.design;

import ru.hse.software.design.commands.CatCommand;
import ru.hse.software.design.commands.CdCommand;
import ru.hse.software.design.commands.Command;
import ru.hse.software.design.commands.EchoCommand;
import ru.hse.software.design.commands.EnvironmentCommand;
import ru.hse.software.design.commands.ExitCommand;
import ru.hse.software.design.commands.GrepCommand;
import ru.hse.software.design.commands.LsCommand;
import ru.hse.software.design.commands.OuterCommand;
import ru.hse.software.design.commands.PwdCommand;
import ru.hse.software.design.commands.WCCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for converting a passed CommandToken to object of type Command.
 * Contains a static method that converts command names to their corresponding class objects.
 **/
public class CommandBuilder {
    private static Command makeCommands(CommandTokens commandToken, CLI cli) {
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
        if (commandToken.getCommand().equals("grep")) {
            return new GrepCommand(commandToken.getCommandArgs());
        }
        if (commandToken.getCommand().equals("cd")) {
            return new CdCommand(commandToken.getCommandArgs());
        }
        if (commandToken.getCommand().equals("ls")) {
            return new LsCommand(commandToken.getCommandArgs());
        }
        return new OuterCommand(commandToken.getCommand(), commandToken.getCommandArgs());
    }

    /**
     * Creates a Command object corresponding to the passed commandToken.
     *
     * @param commandTokens list of command tokens
     * @param cli           CLI object
     * @return Command object
     **/
    public static List<Command> build(List<CommandTokens> commandTokens, CLI cli) {
        List<Command> commands = new ArrayList<>();
        for (CommandTokens commandToken : commandTokens) {
            commands.add(makeCommands(commandToken, cli));
        }
        return commands;
    }
}
