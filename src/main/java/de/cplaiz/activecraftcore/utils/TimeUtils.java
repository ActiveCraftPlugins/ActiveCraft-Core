package de.cplaiz.activecraftcore.utils;

import java.util.Date;

public class TimeUtils {

    public static Date addFromStringToDate(String input) {
        if (input == null) return null;
        input = input.replace(" ", "");
        input = input.replace("d", "d ");
        input = input.replace("w", "w ");
        input = input.replace("M", "M ");
        input = input.replace("y", "y ");
        input = input.replace("h", "h ");
        input = input.replace("m", "m ");

        String[] inputArray = input.split(" ");

        long dayMillis = 0;
        long monthMillis = 0;
        long weekMillis = 0;
        long yearMillis = 0;
        long hourMillis = 0;
        long minuteMillis = 0;

        for (String inputPart : inputArray) {
            inputPart = inputPart.trim();
            switch (inputPart.charAt(inputPart.length() - 1)) {
                case 'd' -> {
                    inputPart = inputPart.replace("d", "");
                    try {
                        dayMillis = Long.parseLong(inputPart) * 24 * 60 * 60 * 1000;
                    } catch (NumberFormatException ignored) {
                    }
                }
                case 'M' -> {
                    inputPart = inputPart.replace("M", "");
                    try {
                        monthMillis = Long.parseLong(inputPart) * 30 * 24 * 60 * 60 * 1000;
                    } catch (NumberFormatException ignored) {
                    }
                }
                case 'w' -> {
                    inputPart = inputPart.replace("w", "");
                    try {
                        weekMillis = Long.parseLong(inputPart) * 7 * 24 * 60 * 60 * 1000;
                    } catch (NumberFormatException ignored) {
                    }
                }
                case 'y' -> {
                    inputPart = inputPart.replace("y", "");
                    try {
                        yearMillis = Long.parseLong(inputPart) * 365 * 24 * 60 * 60 * 1000;
                        int leapDays = Integer.parseInt(inputPart) / 4;
                        yearMillis += (long) leapDays * 24 * 60 * 60 * 1000;
                    } catch (NumberFormatException ignored) {
                    }
                }
                case 'h' -> {
                    inputPart = inputPart.replace("h", "");
                    try {
                        hourMillis = Long.parseLong(inputPart) * 60 * 60 * 1000;
                    } catch (NumberFormatException ignored) {
                    }
                }
                case 'm' -> {
                    inputPart = inputPart.replace("m", "");
                    try {
                        minuteMillis = Long.parseLong(inputPart) * 60 * 1000;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        Date nowDate = new Date();
        long nowMillis = nowDate.getTime();
        Date finalDate = null;
        if (minuteMillis != 0 || hourMillis != 0 || dayMillis != 0 || weekMillis != 0 || monthMillis != 0 || yearMillis != 0) {
            finalDate = new Date(minuteMillis + hourMillis + dayMillis + weekMillis + monthMillis + yearMillis + nowMillis);
        }
        return finalDate;
    }

    public static String getRemainingAsString(Date date) {
        String expirationString;
        if (date != null) {
            long expirationTime = date.getTime();
            Date nowDate = new Date();
            expirationTime = expirationTime - nowDate.getTime();
            expirationTime = expirationTime / 1000;
            if (expirationTime < 86400) {
                expirationString = IntegerUtils.shortInteger((int) expirationTime);
            } else {
                expirationString = expirationTime / 86400 + " days";
            }
        } else expirationString = "never";
        return expirationString;
    }
}
