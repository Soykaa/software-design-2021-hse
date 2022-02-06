package ru.hse.software.design;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class for separating a list of tokens into commands and arguments.
 * Contains main static method 'preProcess'
 * as well as a helper private method 'getEqualityIndex'.
 **/
public class Parser {
    private static int getEqualityIndex(String str) {
        boolean insideSingleQuotes = false;
        boolean insideDoubleQuotes = false;
        for (int i = 0; i < str.length(); i++) {
            char curChar = str.charAt(i);
            if (curChar == '\'') {
                insideSingleQuotes = !insideSingleQuotes;
                continue;
            }
            if (curChar == '\"') {
                insideDoubleQuotes = !insideDoubleQuotes;
                continue;
            }
            if (insideDoubleQuotes | insideSingleQuotes) {
                continue;
            }
            if (curChar == '=') {
                return i;
            }
        }
        return -1;
    }

    private static String checkEquality(String command, List<String> commandArguments) {
        if (command.contains("=")) {
            int eqIndex = getEqualityIndex(command);
            if (eqIndex > 0) {
                commandArguments.add(command.substring(0, eqIndex));
                commandArguments.add(command.substring(eqIndex + 1));
                command = "environment";
            }
        }
        return command;
    }

    /**
     * Receives a list of tokens as input, finds commands among them,
     * and, depending on this, divides into CommandTokens.
     * If the command name contains the '=' sign, then the name of the command
     * is 'environment', and the arguments are obtained from the token with the '=' sign.
     *
     * @param tokens list of tokens
     * @return command tokens
     **/
    public static CommandTokens preProcess_single(List<Token> tokens) {
        String command = tokens.get(0).getToken();
        List<String> commandArgs = new ArrayList<>();
        if (command.contains("=")) {
            int eqIndex = getEqualityIndex(command);
            if (eqIndex > 0) {
                commandArgs.add(command.substring(0, eqIndex));
                commandArgs.add(command.substring(eqIndex + 1));
                command = "environment";
            }
        }
        for (int i = 1; i < tokens.size(); i++) {
            commandArgs.add(tokens.get(i).getToken());
        }
        return new CommandTokens(command, commandArgs);
    }

    public static List<CommandTokens> preProcess(List<Token> tokens) {
        List<CommandTokens> commandTokens = new ArrayList<>();
        List<String> currentCommandArguments = new ArrayList<>();
        String command = tokens.get(0).getToken();
        String lastToken = tokens.get(tokens.size() - 1).getToken();
        if (command.equals("|") | lastToken.equals("|")) {
            System.err.println("'|' must be between commands");
            return Collections.emptyList();
        }
        command = checkEquality(command, currentCommandArguments);

        boolean hasCurrentCommand = true;
        for (int i = 1; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (!hasCurrentCommand) {
                if (token.getToken().equals("|")) {
                    System.err.println("'|' must be between commands");
                    return Collections.emptyList();
                }
                command = checkEquality(token.getToken(), currentCommandArguments);
                hasCurrentCommand = true;
                continue;
            }

            if (token.getToken().equals("|")) {
                commandTokens.add(new CommandTokens(command, currentCommandArguments));
                hasCurrentCommand = false;
                currentCommandArguments = new ArrayList<>();
                continue;
            }

            currentCommandArguments.add(token.getToken());
        }
        commandTokens.add(new CommandTokens(command, currentCommandArguments));
        return commandTokens;
    }
}
