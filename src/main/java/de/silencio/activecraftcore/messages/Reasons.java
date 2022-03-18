package de.silencio.activecraftcore.messages;

public class Reasons extends ActiveCraftCoreMessage {


    public static String HACKING() {
        return getMessage(MessageType.REASON, "hacking");
    }

    public static String BOTTING() {
        return getMessage(MessageType.REASON, "botting");
    }

    public static String UNAUTHORIZED_ALTERNATE_ACCOUNT() {
        return getMessage(MessageType.REASON, "unauthorized-alt-account");
    }

    public static String SPAM() {
        return getMessage(MessageType.REASON, "spam");
    }

    public static String ABUSIVE_LANGUAGE() {
        return getMessage(MessageType.REASON, "abusive-language");
    }

    public static String STEALING() {
        return getMessage(MessageType.REASON, "stealing");
    }

    public static String GRIEFING() {
        return getMessage(MessageType.REASON, "griefing");
    }

    public static String MODERATOR_BANNED() {
        return getMessage(MessageType.REASON, "moderator-banned");
    }

    public static String MODERATOR_WARNED() {
        return getMessage(MessageType.REASON, "moderator-warned");
    }

    public static String MODERATOR_KICKED() {
        return getMessage(MessageType.REASON, "moderator-kicked");
    }
}
