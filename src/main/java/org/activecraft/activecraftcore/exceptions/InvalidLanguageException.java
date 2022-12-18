package org.activecraft.activecraftcore.exceptions;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.messages.Language;
import lombok.Getter;
import org.activecraft.activecraftcore.ActiveCraftPlugin;

@Getter
public class InvalidLanguageException extends ActiveCraftException{

    private Language language;
    private ActiveCraftPlugin plugin;

    public InvalidLanguageException(String message, Language language, ActiveCraftPlugin plugin) {
        super(message);
        this.language = language;
        this.plugin = plugin;
    }

    public InvalidLanguageException(Language language, ActiveCraftPlugin plugin) {
        this("Invalid language.", language, plugin);
    }
}
