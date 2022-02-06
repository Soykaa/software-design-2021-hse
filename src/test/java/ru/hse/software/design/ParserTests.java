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
        Token command = new Token("command", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("command", commandTokens.get(0).getCommand());
        Assertions.assertEquals(0, commandTokens.get(0).getCommandArgs().size());
    }

    @Test
    public void testOneCommandWithArguments() {
        Token command = new Token("command", Type.FULLY_PROCESSED);
        Token arg1 = new Token("arg1", Type.FULLY_PROCESSED);
        Token arg2 = new Token("arg2", Type.FULLY_PROCESSED);
        Token arg3 = new Token("arg3", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command, arg1, arg2, arg3));
        Assertions.assertEquals("command", commandTokens.get(0).getCommand());
        Assertions.assertEquals(3, commandTokens.get(0).getCommandArgs().size());
        Assertions.assertEquals("arg1", commandTokens.get(0).getCommandArgs().get(0));
        Assertions.assertEquals("arg2", commandTokens.get(0).getCommandArgs().get(1));
        Assertions.assertEquals("arg3", commandTokens.get(0).getCommandArgs().get(2));
    }

    @Test
    public void testEqualityOneCommand() {
        Token command = new Token("x=123", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("environment", commandTokens.get(0).getCommand());
        Assertions.assertEquals(2, commandTokens.get(0).getCommandArgs().size());
        Assertions.assertEquals("x", commandTokens.get(0).getCommandArgs().get(0));
        Assertions.assertEquals("123", commandTokens.get(0).getCommandArgs().get(1));
    }

    @Test
    public void testThreeCommands() {
        Token command1 = new Token("command1", Type.FULLY_PROCESSED);
        Token pipe = new Token("|", Type.FULLY_PROCESSED);
        Token command2 = new Token("command2", Type.FULLY_PROCESSED);
        Token arg1 = new Token("arg1", Type.FULLY_PROCESSED);
        Token arg2 = new Token("arg2", Type.FULLY_PROCESSED);
        Token command3 = new Token("command3", Type.FULLY_PROCESSED);
        Token arg3 = new Token("arg3", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe,
                                                                            command2, arg1, arg2, pipe,
                                                                            command3, arg3));
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
        Token command1 = new Token("command1", Type.FULLY_PROCESSED);
        Token pipe = new Token("|", Type.FULLY_PROCESSED);
        Token command2 = new Token("command2", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe, pipe, command2));
        Assertions.assertEquals(0, commandTokens.size());
        String expectedError = "'|' must be between commands\n";
        Assertions.assertEquals(expectedError, errContent.toString());
    }

    @Test
    public void testPipeInTheBegin() {
        Token command1 = new Token("command1", Type.FULLY_PROCESSED);
        Token pipe = new Token("|", Type.FULLY_PROCESSED);
        Token command2 = new Token("command2", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(pipe, command1, pipe, command2));
        Assertions.assertEquals(0, commandTokens.size());
        String expectedError = "'|' must be between commands\n";
        Assertions.assertEquals(expectedError, errContent.toString());
    }

    @Test
    public void testPipeInTheEnd() {
        Token command1 = new Token("command1", Type.FULLY_PROCESSED);
        Token pipe = new Token("|", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe));
        Assertions.assertEquals(0, commandTokens.size());
        String expectedError = "'|' must be between commands\n";
        Assertions.assertEquals(expectedError, errContent.toString());
    }

    @Test
    public void testEqualityAfterPipe() {
        Token command1 = new Token("command1", Type.FULLY_PROCESSED);
        Token pipe = new Token("|", Type.FULLY_PROCESSED);
        Token equality = new Token("x=123", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe, equality));
        Assertions.assertEquals("environment", commandTokens.get(1).getCommand());
        Assertions.assertEquals(2, commandTokens.get(1).getCommandArgs().size());
        Assertions.assertEquals("x", commandTokens.get(1).getCommandArgs().get(0));
        Assertions.assertEquals("123", commandTokens.get(1).getCommandArgs().get(1));
    }

    @Test
    public void testEqualityWithArgumentsAfterPipe() {
        Token command1 = new Token("command1", Type.FULLY_PROCESSED);
        Token pipe = new Token("|", Type.FULLY_PROCESSED);
        Token equality = new Token("x=123", Type.FULLY_PROCESSED);
        Token arg = new Token("arg", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe, equality, arg));
        Assertions.assertEquals("environment", commandTokens.get(1).getCommand());
        Assertions.assertEquals(3, commandTokens.get(1).getCommandArgs().size());
        Assertions.assertEquals("x", commandTokens.get(1).getCommandArgs().get(0));
        Assertions.assertEquals("123", commandTokens.get(1).getCommandArgs().get(1));
        Assertions.assertEquals("arg", commandTokens.get(1).getCommandArgs().get(2));
    }

    @Test
    public void testEqualityWithoutVariableAfterPipe() {
        Token command1 = new Token("command1", Type.FULLY_PROCESSED);
        Token pipe = new Token("|", Type.FULLY_PROCESSED);
        Token equality = new Token("=123", Type.FULLY_PROCESSED);
        List<CommandTokens> commandTokens = Parser.preProcess(Arrays.asList(command1, pipe, equality));
        Assertions.assertEquals("=123", commandTokens.get(1).getCommand());
        Assertions.assertEquals(0, commandTokens.get(1).getCommandArgs().size());
    }
}
