package de.cplaiz.activecraftcore.messages;

public class Reasons extends MessageCollection {

    public static String HACKING() {
        return getRawMessage("reason.hacking");
    }

    public static String BOTTING() {
        return getRawMessage("reason.botting");
    }

    public static String UNAUTHORIZED_ALTERNATE_ACCOUNT() {
        return getRawMessage("reason.unauthorized-alt-account");
    }

    public static String SPAM() {
        return getRawMessage("reason.spam");
    }

    public static String ABUSIVE_LANGUAGE() {
        return getRawMessage("reason.abusive-language");
    }

    public static String STEALING() {
        return getRawMessage("reason.stealing");
    }

    public static String GRIEFING() {
        return getRawMessage("reason.griefing");
    }

    public static String MODERATOR_BANNED() {
        return getRawMessage("reason.moderator-banned");
    }

    public static String MODERATOR_WARNED() {
        return getRawMessage("reason.moderator-warned");
    }

    public static String MODERATOR_KICKED() {
        return getRawMessage("reason.moderator-kicked");
    }
}
