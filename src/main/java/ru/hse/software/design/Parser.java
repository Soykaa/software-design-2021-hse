package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static CommandTokens preProcess(List<Token> tokens) {
        String command = tokens.get(0).getToken();
        List<String> commandArgs = new ArrayList<>();
        for (int i = 1; i < tokens.size(); i++) {
            commandArgs.add(tokens.get(i).getToken());
        }
        return new CommandTokens(command, commandArgs);
    }
}
