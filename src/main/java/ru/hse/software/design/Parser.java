package ru.hse.software.design;

import java.util.ArrayList;
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

    /**
     * Receives a list of tokens as input, finds commands among them,
     * and, depending on this, divides into CommandTokens.
     * If the command name contains the '=' sign, then the name of the command
     * is 'environment', and the arguments are obtained from the token with the '=' sign.
     *
     * @param tokens list of tokens
     * @return Command tokens
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
