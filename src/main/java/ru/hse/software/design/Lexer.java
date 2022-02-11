package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for splitting the input string into tokens.
 **/
public class Lexer {
    private enum QuotesStatus {
        DEFAULT,
        INSIDE_SINGLE,
        INSIDE_DOUBLE
    }

    /**
     * Takes the entire string entered by the user as input,
     * and then divides it into tokens depending on the location of spaces, quotes, '$' and '|'.
     *
     * @param command input string
     * @return List of Token objects
     **/
    public static List<Token> getTokens(String command) {
        List<Token> result = new ArrayList<>();
        QuotesStatus currentStatus = QuotesStatus.DEFAULT;
        var currentToken = new StringBuilder();
        Type currentTokenType = Type.FULLY_PROCESSED;

        for (int i = 0; i < command.length(); i++) {
            switch (command.charAt(i)) {
                case '\'':
                    if (currentStatus == QuotesStatus.INSIDE_DOUBLE) {
                        currentToken.append('\'');
                        break;
                    }
                    if (currentStatus == QuotesStatus.INSIDE_SINGLE) {
                        currentToken.append('\'');
                        currentStatus = QuotesStatus.DEFAULT;
                        break;
                    }
                    currentStatus = QuotesStatus.INSIDE_SINGLE;
                    currentToken.append('\'');
                    break;
                case '"':
                    if (currentStatus == QuotesStatus.INSIDE_SINGLE) {
                        currentToken.append('\"');
                        break;
                    }
                    if (currentStatus == QuotesStatus.INSIDE_DOUBLE) {
                        currentToken.append('\"');
                        currentStatus = QuotesStatus.DEFAULT;
                        break;
                    }
                    currentStatus = QuotesStatus.INSIDE_DOUBLE;
                    currentToken.append('\"');
                    break;
                case '$':
                    if (currentStatus != QuotesStatus.INSIDE_SINGLE) {
                        currentTokenType = Type.WEAKLY_PROCESSED;
                    }
                    currentToken.append('$');
                    break;
                case '|':
                    if (currentStatus == QuotesStatus.DEFAULT) {
                        if (currentToken.length() != 0) {
                            result.add(new Token(currentToken.toString(), currentTokenType));
                        }
                        result.add(new Token("|", Type.FULLY_PROCESSED));
                        currentToken.setLength(0);
                        currentTokenType = Type.FULLY_PROCESSED;
                    } else {
                        currentToken.append(command.charAt(i));
                    }
                    break;
                default:
                    if (Character.isWhitespace(command.charAt(i))) {
                        if (currentStatus == QuotesStatus.DEFAULT) {
                            if (currentToken.length() != 0) {
                                result.add(new Token(currentToken.toString(), currentTokenType));
                                currentToken.setLength(0);
                                currentTokenType = Type.FULLY_PROCESSED;
                            }
                            continue;
                        }
                        currentToken.append(' ');
                    } else {
                        currentToken.append(command.charAt(i));
                    }
                    break;
            }
        }
        if (!currentToken.toString().isEmpty()) {
            result.add(new Token(currentToken.toString(), currentTokenType));
        }
        if (currentStatus != QuotesStatus.DEFAULT) {
            throw new IllegalArgumentException("Incorrect quotes");
        }
        return result;
    }
}