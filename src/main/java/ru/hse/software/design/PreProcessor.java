package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Class for handling the necessary substitutions in tokens.
 **/
public class PreProcessor {
    private enum QuotesStatus {
        DEFAULT,
        INSIDE_SINGLE,
        INSIDE_DOUBLE
    }

    private static String getFromEnvironment(String variableName) {
        Optional<String> value = Environment.get(variableName);
        if (value.isEmpty()) {
            throw new NoSuchElementException("No such variable");
        }
        return value.get();
    }

    private static Token removeQuotes(Token token) {
        QuotesStatus currentStatus = QuotesStatus.DEFAULT;
        var newToken = new StringBuilder();
        String tokenString = token.getToken();
        for (int i = 0; i < tokenString.length(); i++) {
            char currentSymbol = tokenString.charAt(i);
            switch (currentSymbol) {
                case '\'':
                    if (currentStatus == QuotesStatus.INSIDE_DOUBLE) {
                        newToken.append('\'');
                        break;
                    }
                    if (currentStatus == QuotesStatus.INSIDE_SINGLE) {
                        currentStatus = QuotesStatus.DEFAULT;
                        break;
                    }
                    currentStatus = QuotesStatus.INSIDE_SINGLE;
                    break;
                case '"':
                    if (currentStatus == QuotesStatus.INSIDE_SINGLE) {
                        newToken.append('\"');
                        break;
                    }
                    if (currentStatus == QuotesStatus.INSIDE_DOUBLE) {
                        currentStatus = QuotesStatus.DEFAULT;
                        break;
                    }
                    currentStatus = QuotesStatus.INSIDE_DOUBLE;
                    break;
                default:
                    newToken.append(currentSymbol);
            }
        }
        token.setToken(newToken.toString());
        return token;
    }

    private static Token preProcessToken(Token token) {
        var preProcessedToken = new StringBuilder();
        var variableName = new StringBuilder();
        QuotesStatus currentStatus = QuotesStatus.DEFAULT;
        boolean variableNameStarted = false;
        for (int i = 0; i < token.getToken().length(); i++) {
            char currentSymbol = token.getToken().charAt(i);
            if (currentSymbol == '$') {
                if (currentStatus == QuotesStatus.INSIDE_SINGLE) {
                    preProcessedToken.append(currentSymbol);
                    continue;
                }
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

                if (currentSymbol == '\'') {
                    if (currentStatus == QuotesStatus.INSIDE_SINGLE) {
                        currentStatus = QuotesStatus.DEFAULT;
                    } else if (currentStatus == QuotesStatus.DEFAULT) {
                        currentStatus = QuotesStatus.INSIDE_SINGLE;
                    }
                }

                if (currentSymbol == '"') {
                    if (currentStatus == QuotesStatus.INSIDE_DOUBLE) {
                        currentStatus = QuotesStatus.DEFAULT;
                    } else if (currentStatus == QuotesStatus.DEFAULT) {
                        currentStatus = QuotesStatus.INSIDE_DOUBLE;
                    }
                }
                preProcessedToken.append(currentSymbol);
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
                result.add(removeQuotes(token));
            } else {
                result.add(removeQuotes(preProcessToken(token)));
            }
        }
        return result;
    }
}
