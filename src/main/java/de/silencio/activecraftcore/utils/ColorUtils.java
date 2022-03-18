package de.silencio.activecraftcore.utils;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.Arrays;
import java.util.Random;

public class ColorUtils {

    public static ChatColor[] getColorsOnly() {
        return (ChatColor[]) ArrayUtils.subarray(ChatColor.values(), 0, ChatColor.values().length - 6);
    }

    public static ChatColor getRandomColor() {
        return getColorsOnly()[new Random().nextInt(getColorsOnly().length)];
    }

    public static ChatColor getColorByName(String name) {
        for (ChatColor color : getColorsOnly())
            if (name.equalsIgnoreCase(color.name())) return color;
        return null;
    }

    public static String replaceColorAndFormat(String message) {
        return replaceColorAndFormat('&', message);
    }

    public static String replaceColorAndFormat(char altColorCode, String message) {
        message = replaceHex(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String removeColorAndFormat(String string) {
        return string.replaceAll("§x(§[a-fA-F0-9]){6}", "")
                .replace("§0", "")
                .replace("§1", "")
                .replace("§2", "")
                .replace("§3", "")
                .replace("§4", "")
                .replace("§5", "")
                .replace("§6", "")
                .replace("§7", "")
                .replace("§8", "")
                .replace("§9", "")
                .replace("§a", "")
                .replace("§b", "")
                .replace("§c", "")
                .replace("§d", "")
                .replace("§e", "")
                .replace("§f", "")
                .replace("§k", "")
                .replace("§l", "")
                .replace("§m", "")
                .replace("§n", "")
                .replace("§o", "")
                .replace("§r", "")
                .replace("§x", "");
    }


    public static Color bukkitColorFromString(String string) {
        int[] rgbArray = getRGB(switch (string.toLowerCase()) {
            case "green" -> "#5E7C16";
            case "black" -> "#1D1D21";
            case "blue" -> "#3C44AA";
            case "lime" -> "#80C71F";
            case "cyan" -> "#169C9C";
            case "red" -> "#B02E26";
            case "magenta" -> "#C74EBD";
            case "pink" -> "#F38BAA";
            case "orange" -> "#F9801D";
            case "light_gray" -> "#9D9D97";
            case "gray" -> "#474F52";
            case "light_blue" -> "#3AB3DA";
            case "purple" -> "#8932B8";
            case "yellow" -> "#FED83D";
            case "white" -> "#F9FFFE";
            case "brown" -> "#835432";
            case "pepega_green" -> "#0aad1b";
            default -> "#000000";
        });

        return Color.fromRGB(rgbArray[0], rgbArray[1], rgbArray[2]);
    }

    public static int[] getRGB(String rgb) {
        rgb = rgb.replace("#", "");
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++) {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }

    public static String replaceHex(String input) {
        return replaceHex('&', input);
    }

    public static String replaceHex(char altColorCode, String input) {
        String[] inputArray = input.split(altColorCode + "#");
        StringBuilder outputStringBuilder = new StringBuilder();
        outputStringBuilder.append(inputArray[0]);
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i].length() < 6) continue;
            String substring = inputArray[i].substring(0, 6);
            String rest = inputArray[i].length() > 5 ? inputArray[i].substring(6) : "";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("§x");
            for (char strChar : substring.toCharArray()) {
                String s = String.valueOf(strChar);
                stringBuilder.append("§").append(s);
            }
            outputStringBuilder.append(stringBuilder).append(rest);
        }
        return outputStringBuilder.toString();
    }

}
