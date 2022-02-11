package ru.hse.software.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTests {
    @Test
    public void testWithoutArguments() {
        var command = new Token("command", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("command", commandTokens.getCommand());
        Assertions.assertEquals(0, commandTokens.getCommandArgs().size());
    }

    @Test
    public void testOneArgument() {
        var command = new Token("command", Type.FULLY_PROCESSED);
        var arg = new Token("arg", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(Arrays.asList(command, arg));
        Assertions.assertEquals("command", commandTokens.getCommand());
        Assertions.assertEquals(1, commandTokens.getCommandArgs().size());
        Assertions.assertEquals("arg", commandTokens.getCommandArgs().get(0));
    }

    @Test
    public void testMoreArguments() {
        var command = new Token("command", Type.FULLY_PROCESSED);
        var arg1 = new Token("arg1", Type.FULLY_PROCESSED);
        var arg2 = new Token("arg2", Type.FULLY_PROCESSED);
        var arg3 = new Token("arg3", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(Arrays.asList(command, arg1, arg2, arg3));
        Assertions.assertEquals("command", commandTokens.getCommand());
        Assertions.assertEquals(3, commandTokens.getCommandArgs().size());
        Assertions.assertEquals("arg1", commandTokens.getCommandArgs().get(0));
        Assertions.assertEquals("arg2", commandTokens.getCommandArgs().get(1));
        Assertions.assertEquals("arg3", commandTokens.getCommandArgs().get(2));
    }

    @Test
    public void testEqualitySimple() {
        var command = new Token("x=123", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("environment", commandTokens.getCommand());
        Assertions.assertEquals(2, commandTokens.getCommandArgs().size());
        Assertions.assertEquals("x", commandTokens.getCommandArgs().get(0));
        Assertions.assertEquals("123", commandTokens.getCommandArgs().get(1));
    }

    @Test
    public void testEqualityWithoutVariable() {
        var command = new Token("=123", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("=123", commandTokens.getCommand());
        Assertions.assertEquals(0, commandTokens.getCommandArgs().size());
    }

    @Test
    public void testEqualityInQuotes1() {
        var command = new Token("x'=123'", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("x'=123'", commandTokens.getCommand());
        Assertions.assertEquals(0, commandTokens.getCommandArgs().size());
    }

    @Test
    public void testEqualityInQuotes2() {
        var command = new Token("x'yz\"=123\"'", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("x'yz\"=123\"'", commandTokens.getCommand());
        Assertions.assertEquals(0, commandTokens.getCommandArgs().size());
    }

    @Test
    public void testDoubleEquality1() {
        var command = new Token("\"x=123\"=test", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("environment", commandTokens.getCommand());
        Assertions.assertEquals(2, commandTokens.getCommandArgs().size());
        Assertions.assertEquals("\"x=123\"", commandTokens.getCommandArgs().get(0));
        Assertions.assertEquals("test", commandTokens.getCommandArgs().get(1));
    }

    @Test
    public void testDoubleEquality2() {
        var command = new Token("x=\"a=2\"", Type.FULLY_PROCESSED);
        CommandTokens commandTokens = Parser.preProcess(List.of(command));
        Assertions.assertEquals("environment", commandTokens.getCommand());
        Assertions.assertEquals(2, commandTokens.getCommandArgs().size());
        Assertions.assertEquals("x", commandTokens.getCommandArgs().get(0));
        Assertions.assertEquals("\"a=2\"", commandTokens.getCommandArgs().get(1));
    }
}
