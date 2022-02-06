package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses a list of tokens into a command and its arguments.
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

    /**
     * Accepts a list of tokens as input. First token is treated as a command name with the rest being its arguments.
     * If first token contains the '=' sign, then a reserved key-word 'environment' is used as a command name with
     * arguments being words before and after '=' as well as other tokens.
     *
     * @param tokens list of tokens
     * @return parsed CommandTokens
     **/
    public static CommandTokens preProcess(List<Token> tokens) {
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
}
