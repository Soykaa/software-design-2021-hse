package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PreProcessor {
    private static String getFromEnvironment(String variableName) {
        Optional<String> value = Environment.get(variableName);
        if (value.isEmpty()) {
            throw new NoSuchElementException("No such variable");
        }
        return value.get();
    }

    private static Token processToken(Token token) {
        StringBuilder processedToken = new StringBuilder();
        StringBuilder variableName = new StringBuilder();
        boolean nameStarted = false;
        for (int i = 0; i < token.getToken().length(); i++) {
            switch (token.getToken().charAt(i)) {
                case '$':
                    if (nameStarted) {
                        String value = getFromEnvironment(variableName.toString());
                        processedToken.append(value);
                        variableName.setLength(0);
                    }
                    nameStarted = true;
                    break;
                case '|':
                case '"':
                case ' ':
                case '\'':
                    if (nameStarted) {
                        String value = getFromEnvironment(variableName.toString());
                        processedToken.append(value);
                        nameStarted = false;
                        variableName.setLength(0);
                    }
                    processedToken.append(token.getToken().charAt(i));
                    break;
                default:
                    if (nameStarted) {
                        variableName.append(token.getToken().charAt(i));
                    } else {
                        processedToken.append(token.getToken().charAt(i));
                    }
                    break;
            }
        }
        if (nameStarted) {
            processedToken.append(getFromEnvironment(variableName.toString()));
        }
        token.setToken(processedToken.toString());
        token.setType(Type.FULLY_PROCESSED);
        return token;
    }

    public static List<Token> preProcess(List<Token> tokens) {
        List<Token> result = new ArrayList<>();
        for (Token token : tokens) {
            if (token.getType() == Type.FULLY_PROCESSED) {
                result.add(token);
            } else {
                result.add(processToken(token));
            }
        }
        return result;
    }
}
