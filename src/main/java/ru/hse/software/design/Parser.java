package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses a list of tokens into a command and its arguments.
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

    /**
     * Accepts a list of tokens as input. First token is treated as a command name with the rest being its arguments.
     * If first token contains the '=' sign, then a reserved key-word 'environment' is used as a command name with
     * arguments being words before and after '=' as well as other tokens.
     *
     * @param tokens list of tokens
     * @return parsed CommandTokens
     **/
    public static List<CommandTokens> preProcess(List<Token> tokens) {
        List<CommandTokens> commandTokens = new ArrayList<>();
        List<String> currentCommandArguments = new ArrayList<>();
        String command = tokens.get(0).getToken();
        String lastToken = tokens.get(tokens.size() - 1).getToken();
        if (command.equals("|") || lastToken.equals("|")) {
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
