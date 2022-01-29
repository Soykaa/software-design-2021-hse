package ru.hse.software.design;

import ru.hse.software.design.commands.CatCommand;
import ru.hse.software.design.commands.Command;


public class CommandBuilder {
    public Command build(CommandTokens commandToken) {
        if (commandToken.getCommand().equals("cat")) {
            Command cat = new CatCommand(commandToken.getCommand(), new InputStream(), new OutputStream());
        }
    }
}
