package ru.hse.software.design;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for separating a list of tokens into commands and arguments.
 * Contains main static method 'preProcess'
 * as well as a couple of helper private methods.
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
