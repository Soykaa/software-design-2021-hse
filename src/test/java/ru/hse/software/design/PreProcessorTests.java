package ru.hse.software.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PreProcessorTests {
    @Test
    public void testTokenWithOneSubst() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("$x", Type.WEAKLY_PROCESSED));
        Environment.set("x", "123");
        List<Token> res = PreProcessor.preProcess(tokens);
        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(0).getType().name());
        Assertions.assertEquals("123", res.get(0).getToken());
    }

    @Test
    public void testOneTokenWithTwoSubstWithWhitespacesAndQuotes() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("\"$x    $y\"", Type.WEAKLY_PROCESSED));
        Environment.set("x", "123");
        Environment.set("y", "456");
        List<Token> res = PreProcessor.preProcess(tokens);
        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(0).getType().name());
        Assertions.assertEquals("123    456", res.get(0).getToken());
    }

    @Test
    public void testOneTokenWithTwoSubstWithoutWhitespaces() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("$x$y", Type.WEAKLY_PROCESSED));
        Environment.set("x", "123");
        Environment.set("y", "456");
        List<Token> res = PreProcessor.preProcess(tokens);
        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(0).getType().name());
        Assertions.assertEquals("123456", res.get(0).getToken());
    }

    @Test
    public void testOneTokenWithTwoSubstAndOtherSymbols() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("hello \"$x  \" my $y friend", Type.WEAKLY_PROCESSED));
        Environment.set("x", "darkness");
        Environment.set("y", "old");
        List<Token> res = PreProcessor.preProcess(tokens);
        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(0).getType().name());
        Assertions.assertEquals("hello darkness   my old friend", res.get(0).getToken());
    }

    @Test
    public void testSeveralDifferentTokens() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("hello \"$x  \" my $y friend", Type.WEAKLY_PROCESSED));
        tokens.add(new Token("he he", Type.FULLY_PROCESSED));
        tokens.add(new Token("$a$b", Type.WEAKLY_PROCESSED));
        tokens.add(new Token("he", Type.FULLY_PROCESSED));
        tokens.add(new Token("\"$a    $b\"", Type.WEAKLY_PROCESSED));
        Environment.set("x", "darkness");
        Environment.set("y", "old");
        Environment.set("a", "123");
        Environment.set("b", "456");
        List<Token> res = PreProcessor.preProcess(tokens);
        Assertions.assertEquals(5, res.size());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(0).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(1).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(2).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(3).getType().name());
        Assertions.assertEquals("FULLY_PROCESSED", res.get(4).getType().name());
        Assertions.assertEquals("hello darkness   my old friend", res.get(0).getToken());
        Assertions.assertEquals("he he", res.get(1).getToken());
        Assertions.assertEquals("123456", res.get(2).getToken());
        Assertions.assertEquals("he", res.get(3).getToken());
        Assertions.assertEquals("123    456", res.get(4).getToken());
    }
}
