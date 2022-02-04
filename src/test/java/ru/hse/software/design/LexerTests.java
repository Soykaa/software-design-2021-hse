package ru.hse.software.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LexerTests {
    @Test
    public void testOneSimpleFullyProcessedToken() {
        String command = "echo";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(1, tokens.size());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("echo", tokens.get(0).getToken());
    }

    @Test
    public void testSeveralFullyProcessedTokens() {
        String command = "hello   lovely          world";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(3, tokens.size());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(1).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(2).getType().name());
        Assertions.assertEquals("hello", tokens.get(0).getToken());
        Assertions.assertEquals("lovely", tokens.get(1).getToken());
        Assertions.assertEquals("world", tokens.get(2).getToken());
    }

    @Test
    public void testTokensWithSingleQuotesAndSpaces() {
        String command = "hello \"lovely beautiful gorgeous\" world";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(3, tokens.size());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(1).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(2).getType().name());
        Assertions.assertEquals("hello", tokens.get(0).getToken());
        Assertions.assertEquals("lovely beautiful gorgeous", tokens.get(1).getToken());
        Assertions.assertEquals("world", tokens.get(2).getToken());
    }

    @Test
    public void testTokensWithDoubleQuotesAndSpaces() {
        String command = "I love 'software design'";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(3, tokens.size());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(1).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(2).getType().name());
        Assertions.assertEquals("I", tokens.get(0).getToken());
        Assertions.assertEquals("love", tokens.get(1).getToken());
        Assertions.assertEquals("software design", tokens.get(2).getToken());
    }

    @Test
    public void testOneSimpleWeaklyProcessedToken() {
        String command = "$x";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(1, tokens.size());
        Assertions.assertEquals("WEAKLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("$x", tokens.get(0).getToken());
    }

    @Test
    public void testTwoSimpleWeaklyProcessedTokens() {
        String command = "$a $b";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(2, tokens.size());
        Assertions.assertEquals("WEAKLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("WEAKLY_PROCESSED", tokens.get(1).getType().name());
        Assertions.assertEquals("$a", tokens.get(0).getToken());
        Assertions.assertEquals("$b", tokens.get(1).getToken());
    }

    @Test
    public void testSubstTokenSingleQuotes() {
        String command = "ab '$cd'";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(2, tokens.size());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(1).getType().name());
        Assertions.assertEquals("ab", tokens.get(0).getToken());
        Assertions.assertEquals("$cd", tokens.get(1).getToken());
    }

    @Test
    public void testSubstTokenDoubleQuotes() {
        String command = "ab \"$cd\"";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(2, tokens.size());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("WEAKLY_PROCESSED", tokens.get(1).getType().name());
        Assertions.assertEquals("ab", tokens.get(0).getToken());
        Assertions.assertEquals("$cd", tokens.get(1).getToken());
    }

    @Test
    public void testSubstTokenSingleDoubleQuotes() {
        String command = "ab '\"$cd\"'";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(2, tokens.size());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(1).getType().name());
        Assertions.assertEquals("ab", tokens.get(0).getToken());
        Assertions.assertEquals("$cd", tokens.get(1).getToken());
    }

    @Test
    public void testSubstTokenDoubleSingleQuotes() {
        String command = "ab \"'$cd'\"";
        List<Token> tokens = Lexer.getTokens(command);
        Assertions.assertEquals(2, tokens.size());
        Assertions.assertEquals("FULLY_PROCESSED", tokens.get(0).getType().name());
        Assertions.assertEquals("WEAKLY_PROCESSED", tokens.get(1).getType().name());
        Assertions.assertEquals("ab", tokens.get(0).getToken());
        Assertions.assertEquals("$cd", tokens.get(1).getToken());
    }
}
