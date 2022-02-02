package ru.hse.software.design;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static int getEqualityIndex(String str) {
        boolean insideSingleQuotes = false;
        boolean insideDoubleQuotes = false;
        for (int i = 0; i < str.length(); i++) {
            char curChar = str.charAt(i);
            if (curChar == '\''){
                insideSingleQuotes = !insideSingleQuotes;
                continue;
            }
            if (curChar == '\"'){
                insideDoubleQuotes = !insideDoubleQuotes;
                continue;
            }
            if (insideDoubleQuotes | insideSingleQuotes) {
                continue;
            }
            if (curChar == '=') {
                return i;
            }
        }
        return -1;
    }

    public static CommandTokens preProcess(List<Token> tokens) {
        String command = tokens.get(0).getToken();
        List<String> commandArgs = new ArrayList<>();
        if (command.contains("=")) {
            int eqIndex = getEqualityIndex(command);
            if (eqIndex > 0) {
                commandArgs.add(command.substring(0, eqIndex));
                commandArgs.add(command.substring(eqIndex + 1));
                command = "environment";
            }
        }
        for (int i = 1; i < tokens.size(); i++) {
            commandArgs.add(tokens.get(i).getToken());
        }
        return new CommandTokens(command, commandArgs);
    }

    public static void main(String[] args) {
        String s = "012345678";
//        System.out.println(s.substring(3));
//        System.out.println(s.length());

        String a = "=123";
        System.out.println(getEqualityIndex(a));

    }
}
