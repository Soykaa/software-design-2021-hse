package ru.hse.software.design;

/**
 * Class which represents lexer tokens.
 * Contains token and its type as private fields.
 **/
public class Token {
    private String token;
    private Type type;

    /**
     * Makes token and type same as given values.
     *
     * @param token lexer token
     * @param type  token type
     **/
    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    /**
     * Returns lexer token itself.
     *
     * @return token as string
     **/
    public String getToken() {
        return token;
    }

    /**
     * Returns lexer token type.
     *
     * @return Token type
     **/
    public Type getType() {
        return type;
    }

    /**
     * Set token type same as the given value.
     *
     * @param type token type
     **/
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Set token same as the given value.
     *
     * @param token token itself
     **/
    public void setToken(String token) {
        this.token = token;
    }
}

