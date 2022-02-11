package ru.hse.software.design;

/**
 * Class which represents lexer tokens.
 **/
public class Token {
    private final String token;
    private final Type type;

    /**
     * Accepts token and type its type.
     *
     * @param token lexer token
     * @param type  token type
     **/
    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    /**
     * Returns token contents.
     *
     * @return token contents as string
     **/
    public String getToken() {
        return token;
    }

    /**
     * Returns token type.
     *
     * @return Token type
     **/
    public Type getType() {
        return type;
    }
}

