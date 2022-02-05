package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

public class PreProcessor {
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
