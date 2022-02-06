package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Class for handling the necessary substitutions in tokens.
 * Contains main static method 'preProcess'
 * and a couple of helper private methods.
 **/
public class PreProcessor {
    private static String getFromEnvironment(String variableName) {
        Optional<String> value = Environment.get(variableName);
        if (value.isEmpty()) {
            throw new NoSuchElementException("No such variable");
        }
        return value.get();
    }

    private static Token preProcessToken(Token token) {
        StringBuilder preProcessedToken = new StringBuilder();
        StringBuilder variableName = new StringBuilder();
        boolean variableNameStarted = false;
        for (int i = 0; i < token.getToken().length(); i++) {
            char currentSymbol = token.getToken().charAt(i);
            if (currentSymbol == '$') {
                if (variableNameStarted) {
                    String value = getFromEnvironment(variableName.toString());
                    preProcessedToken.append(value);
                    variableName.setLength(0);
                }
                variableNameStarted = true;
                continue;
            }
            if (currentSymbol == '|' || currentSymbol == '"' || currentSymbol == ' ' || currentSymbol == '\'') {
                if (variableNameStarted) {
                    String value = getFromEnvironment(variableName.toString());
                    preProcessedToken.append(value);
                    variableNameStarted = false;
                    variableName.setLength(0);
                }
                if (currentSymbol != '"' && currentSymbol != '\'') {
                    preProcessedToken.append(currentSymbol);
                }
                continue;
            }
            if (variableNameStarted) {
                variableName.append(currentSymbol);
            } else {
                preProcessedToken.append(currentSymbol);
            }
        }
        if (variableNameStarted) {
            preProcessedToken.append(getFromEnvironment(variableName.toString()));
        }
        token.setToken(preProcessedToken.toString());
        token.setType(Type.FULLY_PROCESSED);
        return token;
    }

    /**
     * Processes the input sequence of tokens for substitutions.
     * If the token is FullyProcessed, then nothing happens to it.
     * If the token is WeaklyProcessed, then this method finds variables,
     * where the substitution is required and then does it.
     *
     * @param tokens input tokens
     * @return Preprocessed tokens
     **/
    public static List<Token> preProcess(List<Token> tokens) {
        List<Token> result = new ArrayList<>();
        for (Token token : tokens) {
            if (token.getType() == Type.FULLY_PROCESSED) {
                result.add(token);
            } else {
                result.add(preProcessToken(token));
            }
        }
        return result;
    }
}
