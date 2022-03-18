package de.silencio.activecraftcore.messages;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.utils.config.FileConfig;
import lombok.Getter;
import lombok.Setter;

public class ActiveCraftCoreMessage {

    @Getter
    @Setter
    private static FileConfig fileConfig = new FileConfig("messages.yml");


    protected static String getMessage(MessageType type, String input) {
        return fileConfig.getString(ActiveCraftCore.getLanguage().name().toLowerCase()
                + "." + type.name().toLowerCase()
                + "." + input,
                "INVALID_STRING");
    }

    protected static String getMessage(MessageType type, CommandType cmdType, String input) {
        return fileConfig.getString(ActiveCraftCore.getLanguage().name().toLowerCase()
                + "." + type.name().toLowerCase()
                + "." + cmdType.name().toLowerCase().replace("_", "-")
                + "." + input,
                "INVALID_STRING");
    }
}