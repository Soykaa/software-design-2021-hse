package ru.hse.software.design;

public class Token {
    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }

    private final String token;

    public void setType(Type type) {
        this.type = type;
    }

    private Type type;

    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }
}
