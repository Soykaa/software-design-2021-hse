package ru.hse.software.design;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for separating a list of tokens into commands and arguments.
 * Contains main static method 'preProcess'
 * as well as a couple of helper private methods.
 **/
public class Parser {
    private static String checkEquality(String command, List<String> commandArguments) {
        if (command.contains("=")) {
            int eqIndex = command.indexOf('=');
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
            throw new IllegalArgumentException("'|' must be between commands");
        }
        command = checkEquality(command, currentCommandArguments);

        boolean hasCurrentCommand = true;
        for (int i = 1; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (!hasCurrentCommand) {
                if (token.getToken().equals("|")) {
                    throw new IllegalArgumentException("'|' must be between commands");
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
