package ru.hse.software.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.hse.software.design.commands.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandBuilderTests {
    private static final Map<String, Class<?>> commandsClasses = new HashMap<>();
    private final CLI cli = new CLI();

    @BeforeAll
    public static void setup() {
        commandsClasses.put("echo", EchoCommand.class);
        commandsClasses.put("cat", CatCommand.class);
        commandsClasses.put("wc", WCCommand.class);
        commandsClasses.put("exit", ExitCommand.class);
        commandsClasses.put("pwd", PwdCommand.class);
        commandsClasses.put("environment", EnvironmentCommand.class);
    }

    @Test
    public void testInnerCommands() {
        List<String> arguments = Arrays.asList("arg1", "arg2");
        List<CommandTokens> commandTokens = Arrays.asList(new CommandTokens("echo", arguments),
            new CommandTokens("cat", arguments),
            new CommandTokens("wc", arguments),
            new CommandTokens("exit", arguments),
            new CommandTokens("pwd", arguments),
            new CommandTokens("environment", arguments));

        for (CommandTokens token : commandTokens) {
            Command command = CommandBuilder.build(token, cli);
            Assertions.assertEquals(commandsClasses.get(token.getCommand()), command.getClass());
        }
    }

    @Test
    public void testOuterCommand() {
        List<String> arguments = Arrays.asList("arg1", "arg2");
        var commandTokens = new CommandTokens("new_command", arguments);
        Command command = CommandBuilder.build(commandTokens, cli);
        Assertions.assertEquals(OuterCommand.class, command.getClass());
    }
}
