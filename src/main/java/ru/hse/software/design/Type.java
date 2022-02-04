package ru.hse.software.design;

/**
 * Enum class for the token type.
 **/
public enum Type {
    /**
     * No need for substitutions in token.
     **/
    FULLY_PROCESSED,
    /**
     * There is a need for substitutions in token.
     **/
    WEAKLY_PROCESSED
}
