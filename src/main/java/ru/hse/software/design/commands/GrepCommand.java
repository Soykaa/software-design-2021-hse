package ru.hse.software.design.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ru.hse.software.design.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Searches for a pattern in the passed file or standard input.
 **/
public class GrepCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();
    private static final Options options = new Options();

    static {
        options.addOption(new Option("w", "w", false,
            "The expression is searched for as a word"));
        options.addOption(new Option("A", "A", true,
            "Print num lines of trailing context after each match."));
        options.addOption(new Option("i", "i", false,
            "Perform case insensitive matching. By default, grep is case sensitive."));
    }

    /**
     * Creates grep command with given arguments.
     *
     * @param commandArgs command arguments
     **/
    public GrepCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "grep";
    }

    /**
     * Executes 'grep' command with the given arguments.
     *
     * @param input input as string
     * @return 0 in case of successful outcome of the command, 1 otherwise
     **/
    @Override
    public int execute(String input) {
        final CommandLineParser cliParser = new DefaultParser();
        final CommandLine command;
        try {
            command = cliParser.parse(options, commandArgs.toArray(new String[0]),
                false);
        } catch (ParseException e) {
            errorStream.println(e.getMessage());
            return 1;
        }
        List<String> arguments = command.getArgList();
        if (arguments.isEmpty()) {
            errorStream.println("Regular expression should be passed");
            return 1;
        }
        String regularExpression = arguments.get(0);
        List<String> inputLines;
        if (arguments.size() == 1) {
            inputLines = List.of(input.split("\n"));
        } else {
            String file = arguments.get(1);
            Path path = Environment.getRelativePath(file);
            if (!Files.exists(path)) {
                errorStream.println("File " + file + " does not exist");
                return 1;
            }
            try (Stream<String> stream = Files.lines(path)) {
                inputLines = stream.collect(Collectors.toList());
            } catch (IOException e) {
                errorStream.println("Problem with reading from file " + e.getMessage());
                return 1;
            }
        }
        if (command.hasOption('w')) {
            regularExpression = "\\b" + regularExpression + "\\b";
        }
        if (command.hasOption('i')) {
            regularExpression = regularExpression.toLowerCase();
        }
        if (command.hasOption('A')) {
            long numberLines;
            try {
                numberLines = Long.parseLong(command.getOptionValue('A'));
            } catch (Exception e) {
                errorStream.println("Option 'A' requires number after it");
                return 1;
            }
            if (numberLines < 0) {
                errorStream.println("Option 'A' argument should be non-negative value");
                return 1;
            }
        }
        Pattern pattern = Pattern.compile(regularExpression);
        long numberLinesToPrint = 0;
        var stringBuilder = new StringBuilder();
        for (String line : inputLines) {
            Matcher matcher;
            if (command.hasOption('i')) {
                String lineInLowercase = line;
                lineInLowercase = lineInLowercase.toLowerCase();
                matcher = pattern.matcher(lineInLowercase);
            } else {
                matcher = pattern.matcher(line);
            }
            if (matcher.find()) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append(System.lineSeparator());
                }
                stringBuilder.append(line);
                numberLinesToPrint = Long.parseLong(command.getOptionValue('A', "0"));
                continue;
            }
            if (numberLinesToPrint > 0) {
                stringBuilder.append(System.lineSeparator());
                stringBuilder.append(line);
                numberLinesToPrint--;
            }
        }
        output = stringBuilder.toString();
        return 0;
    }
}
