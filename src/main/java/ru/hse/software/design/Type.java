package ru.hse.software.design;

/**
 * Enum class for the token type.
 **/
public enum Type {
    /**
     * Means that there is no need for substitutions in token
     **/
    FULLY_PROCESSED,
    /**
     * Means that there is a need for substitutions in token
     **/
    WEAKLY_PROCESSED
}
