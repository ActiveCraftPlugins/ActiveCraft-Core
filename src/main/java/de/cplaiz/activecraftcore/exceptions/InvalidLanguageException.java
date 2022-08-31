package de.cplaiz.activecraftcore.exceptions;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.messages.Language;
import lombok.Getter;

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
