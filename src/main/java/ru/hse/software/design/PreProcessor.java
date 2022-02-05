package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

public class PreProcessor {
    private Token processToken(Token token) {
        StringBuilder processedToken = new StringBuilder();
        StringBuilder variableName = new StringBuilder();
        boolean nameStarted = false;
        for (int i = 0; i < token.getToken().length(); i++) {
            switch (token.getToken().charAt(i)) {
                case '$':
                    nameStarted = true;
                    break;
                case '|':
                case '"':
                case ' ':
                case '\'':
                    if (nameStarted) {
                        // Env call
                        // end of current name
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
                throw new UnsupportedOperationException();
            }
        }
        return result;
    }
}
