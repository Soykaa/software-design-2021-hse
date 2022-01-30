package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private enum QuotesStatus {
        DEFAULT,
        SINGLE_FIRST,
        DOUBLE_FIRST
    }

    public static List<Token> getTokens(String command) {
        List<Token> result = new ArrayList<>();
        boolean openedSingleQuotes = false;
        boolean openedDoubleQuotes = false;
        QuotesStatus currentQuotesStatus = QuotesStatus.DEFAULT;
        StringBuilder currentToken = new StringBuilder();
        Type currentTokenType = Type.FULLY_PROCESSED;

        for (int i = 0; i < command.length(); i++) {
            switch (command.charAt(i)) {
                case '\'':
                    openedSingleQuotes = !openedSingleQuotes;
                    if (openedSingleQuotes && !openedDoubleQuotes) {
                        currentQuotesStatus = QuotesStatus.SINGLE_FIRST;
                    }
                    if (!openedSingleQuotes && openedDoubleQuotes) {
                        currentQuotesStatus = QuotesStatus.DOUBLE_FIRST;
                    }
                    if (!openedSingleQuotes && !openedDoubleQuotes) {
                        currentQuotesStatus = QuotesStatus.DEFAULT;
                    }
                    break;
                case '\"':
                    openedDoubleQuotes = !openedDoubleQuotes;
                    if (openedSingleQuotes && !openedDoubleQuotes) {
                        currentQuotesStatus = QuotesStatus.SINGLE_FIRST;
                    }
                    if (!openedSingleQuotes && openedDoubleQuotes) {
                        currentQuotesStatus = QuotesStatus.DOUBLE_FIRST;
                    }
                    if (!openedSingleQuotes && !openedDoubleQuotes) {
                        currentQuotesStatus = QuotesStatus.DEFAULT;
                    }
                    break;
                case '$':
                    if (currentQuotesStatus == QuotesStatus.DOUBLE_FIRST || !openedSingleQuotes) {
                        currentTokenType = Type.WEAKLY_PROCESSED;
                    }
                    currentToken.append('$');
                    break;
                default:
                    if (Character.isWhitespace(command.charAt(i))) {
                        if (!(openedDoubleQuotes && openedSingleQuotes)) {
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
        result.add(new Token(currentToken.toString(), currentTokenType));
        if (openedDoubleQuotes || openedSingleQuotes) {
            throw new IllegalArgumentException("Incorrect quotes");
        }
        return result;
    }
}
