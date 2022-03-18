package de.silencio.activecraftcore.messages;

public class Durations extends ActiveCraftCoreMessage {

    public static String MINUTES_15() {
        return getMessage(MessageType.DURATION, "15m");
    }

    public static String HOUR_1() {
        return getMessage(MessageType.DURATION, "1h");
    }

    public static String HOURS_8() {
        return getMessage(MessageType.DURATION, "8h");
    }

    public static String DAY_1() {
        return getMessage(MessageType.DURATION, "1d");
    }

    public static String DAYS_7() {
        return getMessage(MessageType.DURATION, "7d");
    }

    public static String MONTH_1() {
        return getMessage(MessageType.DURATION, "1M");
    }

    public static String PERMANENT() {
        return getMessage(MessageType.DURATION, "permanent");
    }
}
