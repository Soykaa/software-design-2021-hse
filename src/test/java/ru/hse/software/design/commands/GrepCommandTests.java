package ru.hse.software.design.commands;

import org.junit.jupiter.api.*;
import ru.hse.software.design.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class GrepCommandTests {
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
    public void testOneWordWithoutFlags() {
        Command command = new GrepCommand(List.of("he", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "Not hehe" + System.lineSeparator() + "he  He" + System.lineSeparator() +
            "hell" + System.lineSeparator() + "he@he";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testNoSuchWordWithoutFlags() {
        Command command = new GrepCommand(List.of("spring", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        Assertions.assertEquals("", actualOutput);
    }

    @Test
    public void testSearchOnePhraseWithoutFlags() {
        Command command = new GrepCommand(List.of("Hello darkness", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "Hello darkness my old friend";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSimpleRegexesWithoutFlags() {
        List<String> listOfArgs1 = List.of(new String[]{"a?b", "src/resources/random.txt"});
        List<String> listOfArgs2 = List.of(new String[]{"a+b+", "src/resources/random.txt"});
        List<String> listOfArgs3 = List.of(new String[]{"a*b?c+", "src/resources/random.txt"});
        List<String> listOfArgs4 = List.of(new String[]{"[^b]c", "src/resources/random.txt"});
        List<String> listOfArgs5 = List.of(new String[]{"a(k|b)c", "src/resources/random.txt"});
        List<String> listOfArgs6 = List.of(new String[]{"(ee)+s", "src/resources/random.txt"});
        List<String> listOfArgs7 = List.of(new String[]{"^h", "src/resources/random.txt"});
        List<String> listOfArgs8 = List.of(new String[]{"bc$", "src/resources/random.txt"});
        Command command = new GrepCommand(listOfArgs1);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "aaabbbc" + System.lineSeparator() + "abc" + System.lineSeparator() + "dbc";
        Assertions.assertEquals(expectedOutput, actualOutput);

        command = new GrepCommand(listOfArgs2);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        actualOutput = command.output;
        expectedOutput = "aaabbbc" + System.lineSeparator() + "abc";
        Assertions.assertEquals(expectedOutput, actualOutput);

        command = new GrepCommand(listOfArgs3);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        actualOutput = command.output;
        expectedOutput = "aaabbbc" + System.lineSeparator() + "abc" + System.lineSeparator() +
            "dc" + System.lineSeparator() + "aaaac" + System.lineSeparator() + "dbc" + System.lineSeparator() +
            "akc" + System.lineSeparator() + "alc";
        Assertions.assertEquals(expectedOutput, actualOutput);

        command = new GrepCommand(listOfArgs4);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        actualOutput = command.output;
        expectedOutput = "dc" + System.lineSeparator() + "aaaac" + System.lineSeparator() + "akc" +
            System.lineSeparator() + "alc";
        Assertions.assertEquals(expectedOutput, actualOutput);

        command = new GrepCommand(listOfArgs5);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        actualOutput = command.output;
        expectedOutput = "abc" + System.lineSeparator() + "akc";
        Assertions.assertEquals(expectedOutput, actualOutput);

        command = new GrepCommand(listOfArgs6);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        actualOutput = command.output;
        Assertions.assertEquals("", actualOutput);

        command = new GrepCommand(listOfArgs7);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        actualOutput = command.output;
        expectedOutput = "he  He" + System.lineSeparator() + "hell" +
            System.lineSeparator() + "he@he" + System.lineSeparator() + "hELLo@HeLl";
        Assertions.assertEquals(expectedOutput, actualOutput);

        command = new GrepCommand(listOfArgs8);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        actualOutput = command.output;
        expectedOutput = "aaabbbc" + System.lineSeparator() + "abc" + System.lineSeparator() + "dbc";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWOptionSimpleWord() {
        Command command = new GrepCommand(List.of("-w", "he", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "he  He" + System.lineSeparator() + "he@he";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWOptionDifficultWord() {
        Command command = new GrepCommand(List.of("-w", "1_w", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "1_w 2_w" + System.lineSeparator() + "1_w" + System.lineSeparator() + "2_w 1_w";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWOptionEmptyResult() {
        Command command = new GrepCommand(List.of("-w", "paradise", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        Assertions.assertEquals("", actualOutput);
    }

    @Test
    public void testIOptionSimpleWord() {
        Command command = new GrepCommand(List.of("-i", "Hel", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "Hello darkness my old friend" + System.lineSeparator() +
            "hell" + System.lineSeparator() + "hELLo@HeLl";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIOptionEmptyResult() {
        Command command = new GrepCommand(List.of("-i", "ABCD", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        Assertions.assertEquals("", actualOutput);
    }

    @Test
    public void testAOptionPositiveNumberUpToEOF() {
        Command command = new GrepCommand(List.of("-A", "1", "2_", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "1_w 2_w" + System.lineSeparator() + "1_w" +
            System.lineSeparator() + "2_w 1_w" + System.lineSeparator() + "he@he";
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAOptionPositiveNumberMoreThanEOF() {
        Command command = new GrepCommand(List.of("-A", "3", "hE", "src/resources/random.txt"));
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "hELLo@HeLl" + System.lineSeparator() + System.lineSeparator();
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAOptionZeroNumber() {
        List<String> listOfArgs1 = List.of(new String[]{"-A", "0", "1_w", "src/resources/random.txt"});
        List<String> listOfArgs2 = List.of(new String[]{"-A", "0", "3", "src/resources/random.txt"});
        Command command = new GrepCommand(listOfArgs1);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        String actualOutput = command.output;
        String expectedOutput = "word1_word2" + System.lineSeparator() + "1_w 2_w" +
            System.lineSeparator() + "1_w" + System.lineSeparator() + "2_w 1_w";
        Assertions.assertEquals(expectedOutput, actualOutput);

        command = new GrepCommand(listOfArgs2);
        Assertions.assertEquals(0, command.execute(""));
        Assertions.assertTrue(errContent.toString().isEmpty());
        actualOutput = command.output;
        Assertions.assertEquals("3", actualOutput);
    }

    @Test
    public void testAOptionNegativeNumber() {
        Command command = new GrepCommand(List.of("-A", "-3", "he", "src/resources/random.txt"));
        Assertions.assertEquals(1, command.execute(""));
        Assertions.assertEquals("Option 'A' argument should be non-negative value" + System.lineSeparator(),
            errContent.toString());
        String actualOutput = command.output;
        Assertions.assertEquals("", actualOutput);
    }

    @Test
    public void testNoSuchFile() {
        Command command = new GrepCommand(List.of("-i", "h", "src/resources/hehe.txt"));
        Assertions.assertEquals(1, command.execute(""));
        Assertions.assertEquals("File src/resources/hehe.txt does not exist" + System.lineSeparator(),
            errContent.toString());
        String actualOutput = command.output;
        Assertions.assertEquals("", actualOutput);
    }

    @Test
    public void testNoRegularExpression() {
        Command command = new GrepCommand(List.of());
        Assertions.assertEquals(1, command.execute(""));
        Assertions.assertEquals("Regular expression should be passed" + System.lineSeparator(),
            errContent.toString());
        String actualOutput = command.output;
        Assertions.assertEquals("", actualOutput);
    }
}
