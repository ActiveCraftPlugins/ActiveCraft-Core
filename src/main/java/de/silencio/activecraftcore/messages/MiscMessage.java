package de.silencio.activecraftcore.messages;

public class MiscMessage extends ActiveCraftCoreMessage {

    public static String DEFAULT_MUTE_REMOVE() {
        return getMessage(MessageType.MISC, "default-mute-remove");
    }

    public static String FILE_NOT_FOUND() {
        return getMessage(MessageType.MISC, "file-not-found");
    }

    public static String SAME_IP() {
        return getMessage(MessageType.MISC, "same-ip");
    }

}
