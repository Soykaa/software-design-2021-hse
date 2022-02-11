package ru.hse.software.design.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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

public class GrepCommand extends Command {
    private final List<String> commandArgs = new ArrayList<>();

    public GrepCommand(List<String> commandArgs) {
        this.commandArgs.addAll(commandArgs);
        this.command = "grep";
    }

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
        String regexp = arguments.get(0);
        String file = arguments.get(1);
        if (command.hasOption('w')) {
            regexp = "\\b" + regexp + "\\b";
        }
        if (command.hasOption('i')) {
            regexp = regexp.toLowerCase();
        }
        if (command.hasOption('A')) {
            long numberLines = Long.parseLong(command.getOptionValue('A'));
            if (numberLines < 0) {
                errorStream.println("Option A argument should be non-negative value");
                return 1;
            }
        }
        Path path = Paths.get(file);
        if (!Files.exists(path)) {
            errorStream.println("file " + file + " does not exist");
            return 1;
        }
        Pattern pattern = Pattern.compile(regexp);
        long numberLinesToPrint = 0;
        try (Stream<String> stream = Files.lines(path)) {
            List<String> lines = stream.collect(Collectors.toList());
            var stringBuilder = new StringBuilder();
            for (String line : lines) {
                Matcher matcher;
                if (command.hasOption('i')) {
                    String lower = line;
                    lower = lower.toLowerCase();
                    matcher = pattern.matcher(lower);
                } else {
                    matcher = pattern.matcher(line);
                }
                if (matcher.find()) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append(System.lineSeparator());
                    }
                    stringBuilder.append(line);
                    numberLinesToPrint = Long.parseLong(command.getOptionValue('A'));
                    continue;
                }
                if (numberLinesToPrint > 0) {
                    stringBuilder.append(System.lineSeparator());
                    stringBuilder.append(line);
                    numberLinesToPrint--;
                }
            }
            output = stringBuilder.toString();
        } catch (IOException e) {
            errorStream.println("problem with reading from file " + e.getMessage());
            return 1;
        }
        return 0;
    }

    private static final Options options = new Options();

    static {
        options.addOption(new Option("w", "w", false, "The expression is searched for as a word"));
        options.addOption(new Option("A", "A", true, "Print num lines of trailing context after each match."));
        options.addOption(new Option("i", "i", false, "Perform case insensitive matching.  By default," +
            " grep is case sensitive."));
    }
}
