package ru.hse.software.design.commands;

import ru.hse.software.design.InputStream;
import ru.hse.software.design.OutputStream;

public abstract class Command {
    private String command;
    protected String errorMessage;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    public abstract int execute();

    public String getCommand() {
        return command;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
