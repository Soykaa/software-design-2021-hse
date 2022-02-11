package ru.hse.software.design;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class ParserTests {
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUp() {
        System.setErr(new PrintStream(errContent));
        Environment.clear();
    }

    @AfterEach
    public void restoreStreams() {
        System.setErr(originalErr);
    }

    @Test
    public void testOneCommandWithoutArguments() {
        var command = new Token("command", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("command", commandTokens.get(0).getCommand());
        Assertions.assertEquals(0, commandTokens.get(0).getCommandArgs().size());
    }

    @Test
    public void testOneCommandWithArguments() {
        var command = new Token("command", Type.FULLY_PROCESSED);
        var arg1 = new Token("arg1", Type.FULLY_PROCESSED);
        var arg2 = new Token("arg2", Type.FULLY_PROCESSED);
        var arg3 = new Token("arg3", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command, arg1, arg2, arg3));
        Assertions.assertEquals("command", commandTokens.get(0).getCommand());
        Assertions.assertEquals(3, commandTokens.get(0).getCommandArgs().size());
        Assertions.assertEquals("arg1", commandTokens.get(0).getCommandArgs().get(0));
        Assertions.assertEquals("arg2", commandTokens.get(0).getCommandArgs().get(1));
        Assertions.assertEquals("arg3", commandTokens.get(0).getCommandArgs().get(2));
    }

    @Test
    public void testEqualityOneCommand() {
        var command = new Token("x=123", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("environment", commandTokens.get(0).getCommand());
        Assertions.assertEquals(2, commandTokens.get(0).getCommandArgs().size());
        Assertions.assertEquals("x", commandTokens.get(0).getCommandArgs().get(0));
        Assertions.assertEquals("123", commandTokens.get(0).getCommandArgs().get(1));
    }

    @Test
    public void testThreeCommands() {
        var command1 = new Token("command1", Type.FULLY_PROCESSED);
        var pipe = new Token("|", Type.FULLY_PROCESSED);
        var command2 = new Token("command2", Type.FULLY_PROCESSED);
        var arg1 = new Token("arg1", Type.FULLY_PROCESSED);
        var arg2 = new Token("arg2", Type.FULLY_PROCESSED);
        var command3 = new Token("command3", Type.FULLY_PROCESSED);
        var arg3 = new Token("arg3", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe, command2, arg1, arg2, pipe, command3, arg3));
        Assertions.assertEquals(3, commandTokens.size());
        Assertions.assertEquals("command1", commandTokens.get(0).getCommand());
        Assertions.assertEquals(0, commandTokens.get(0).getCommandArgs().size());
        Assertions.assertEquals("command2", commandTokens.get(1).getCommand());
        Assertions.assertEquals(2, commandTokens.get(1).getCommandArgs().size());
        Assertions.assertEquals("arg1", commandTokens.get(1).getCommandArgs().get(0));
        Assertions.assertEquals("arg2", commandTokens.get(1).getCommandArgs().get(1));
        Assertions.assertEquals("command3", commandTokens.get(2).getCommand());
        Assertions.assertEquals("arg3", commandTokens.get(2).getCommandArgs().get(0));
    }

    @Test
    public void testTwoPipesTogether() {
        var command1 = new Token("command1", Type.FULLY_PROCESSED);
        var pipe = new Token("|", Type.FULLY_PROCESSED);
        var command2 = new Token("command2", Type.FULLY_PROCESSED);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Parser.preProcess(Arrays.asList(command1, pipe, pipe, command2)));
    }

    @Test
    public void testPipeInTheBegin() {
        var command1 = new Token("command1", Type.FULLY_PROCESSED);
        var pipe = new Token("|", Type.FULLY_PROCESSED);
        var command2 = new Token("command2", Type.FULLY_PROCESSED);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Parser.preProcess(Arrays.asList(pipe, command1, pipe, command2)));
    }

    @Test
    public void testPipeInTheEnd() {
        var command1 = new Token("command1", Type.FULLY_PROCESSED);
        var pipe = new Token("|", Type.FULLY_PROCESSED);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Parser.preProcess(Arrays.asList(command1, pipe)));
    }

    @Test
    public void testEqualityAfterPipe() {
        var command1 = new Token("command1", Type.FULLY_PROCESSED);
        var pipe = new Token("|", Type.FULLY_PROCESSED);
        var equality = new Token("x=123", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe, equality));
        Assertions.assertEquals("environment", commandTokens.get(1).getCommand());
        Assertions.assertEquals(2, commandTokens.get(1).getCommandArgs().size());
        Assertions.assertEquals("x", commandTokens.get(1).getCommandArgs().get(0));
        Assertions.assertEquals("123", commandTokens.get(1).getCommandArgs().get(1));
    }

    @Test
    public void testEqualityWithArgumentsAfterPipe() {
        var command1 = new Token("command1", Type.FULLY_PROCESSED);
        var pipe = new Token("|", Type.FULLY_PROCESSED);
        var equality = new Token("x=123", Type.FULLY_PROCESSED);
        var arg = new Token("arg", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe, equality, arg));
        Assertions.assertEquals("environment", commandTokens.get(1).getCommand());
        Assertions.assertEquals(3, commandTokens.get(1).getCommandArgs().size());
        Assertions.assertEquals("x", commandTokens.get(1).getCommandArgs().get(0));
        Assertions.assertEquals("123", commandTokens.get(1).getCommandArgs().get(1));
        Assertions.assertEquals("arg", commandTokens.get(1).getCommandArgs().get(2));
    }

    @Test
    public void testEqualityWithoutVariableAfterPipe() {
        var command1 = new Token("command1", Type.FULLY_PROCESSED);
        var pipe = new Token("|", Type.FULLY_PROCESSED);
        var equality = new Token("=123", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe, equality));
        Assertions.assertEquals("=123", commandTokens.get(1).getCommand());
        Assertions.assertEquals(0, commandTokens.get(1).getCommandArgs().size());
    }
}
